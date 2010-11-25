package com.arktekk.scalacltest

/**
 * @author Thor Åge Eldby (thoraageeldby@gmail.com)
 */

class Other {
  
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