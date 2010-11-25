package com.arktekk.scalacltest

import Timer._

import com.nativelibs4java.util.{NIOUtils, IOUtils}
import com.nativelibs4java.opencl.{CLMem, CLKernel, JavaCL}

object PiCalculationTest {

  def main(args: Array[String]): Unit = {
    time {calcPi}
  }

  val iterations = 500000000
  
  def calcPi(): Unit = {
    var pi = 0.0f
    for (idx <- 1 until iterations if idx % 2 == 1) {
      if (idx % 4 == 1)
        pi += 1.0f / idx
      else
        pi -= 1.0f / idx
    }
    println("a = " + pi * 4.0f)
  }

  private def clCalcPi: Unit = {
    val context = JavaCL.createBestContext
    val code = IOUtils.readText(this.getClass.getResourceAsStream("calcPi.cl"))
    val program = context.createProgram(code).build
    val parts = 500
    val results = NIOUtils.directFloats(parts, context.getByteOrder())
    val buffer = context.createFloatBuffer(CLMem.Usage.Output, results, false)
    val kernel: CLKernel = program.createKernel("calcPi", 1.asInstanceOf[AnyRef], 20.asInstanceOf[AnyRef], (iterations / parts).asInstanceOf[AnyRef], buffer)
    val queue = context.createDefaultProfilingQueue
    kernel.enqueueNDRange(queue, Array(parts), Array(1))
    queue.finish
    val seq = for (idx <- 0 until parts) yield results.get(idx)
    println("pi = " + seq.sum * 4.0f)
  }

}
