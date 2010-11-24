package com.arktekk.scalacltest

//import scalacl._
import com.nativelibs4java.util.{NIOUtils, IOUtils}
import com.nativelibs4java.opencl.{CLMem, CLKernel, JavaCL}

object MatrMultTest {
  def main(args: Array[String]): Unit = {
    time {clCalcPi}
  }

  def time[T](f: => T): T = {
    val start = System.currentTimeMillis
    val v = f
    println("Time: " + (System.currentTimeMillis - start))
    v
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

  private def multiplyMatrix: Unit = {
    val n = 1000
    val a = Array.tabulate(n, n)((i, j) => (i + j) * 1.0)
    val b = Array.tabulate(n, n)((i, j) => (i + j) * 1.0)
    val o = Array.ofDim[Double](n, n)
    for (i <- 0 until n; j <- 0 until n) {
      var tot = 0.0
      for (k <- 0 until n)
        tot += a(i)(k) * b(k)(j)

      o(i)(j) = tot
    }
    println("Avg: " + (o.foldLeft(0.0) {(v, a) => a.sum + v} / (n * n)))
  }

  //implicit val clContext = new ScalaCLContext

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

  /*private def scalacl1: Unit = {
    class DoStuff(i: Dim) extends Program(i) {
      val a = new FloatsVar(None)
      val b = new FloatsVar(None)
      var output = new FloatsVar(None)
      content = output := new IfExpr(a > new Float1(5), a, b)
    }

    val n = 10;
    val prog = new DoStuff(Dim(n))
    prog.a.write(1 to n)
    prog.b.write(11 to n + 10)
    prog.exec
    for (idx <- 0 until prog.output.size)
      print(prog.output.get(idx) + ", ")
    println
  }*/

}
