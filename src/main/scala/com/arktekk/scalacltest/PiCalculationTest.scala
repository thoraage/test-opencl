package com.arktekk.scalacltest

import Timer._

import com.nativelibs4java.util.NIOUtils
import com.nativelibs4java.opencl.{CLMem, CLKernel}

object PiCalculationTest {

  def main(args: Array[String]): Unit = {
    time {gpuCalc.clCalcPi}
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

  lazy val gpuCalc = new GPUCalc

  class GPUCalc extends GPUProcessor {
    def sourceFile = "calcPi.cl"
    def clCalcPi: Unit = {
      val parts = 500
      val results = NIOUtils.directFloats(parts, context.getByteOrder())
      val buffer = context.createFloatBuffer(CLMem.Usage.Output, results, false)
      val kernel: CLKernel = program.createKernel("calcPi", 1.asInstanceOf[AnyRef], 20.asInstanceOf[AnyRef], (iterations / parts).asInstanceOf[AnyRef], buffer)
      kernel.enqueueNDRange(queue, Array(parts), Array(1))
      queue.finish
      val seq = for (idx <- 0 until parts) yield results.get(idx)
      println("pi = " + seq.sum * 4.0f)
    }
  }

}
