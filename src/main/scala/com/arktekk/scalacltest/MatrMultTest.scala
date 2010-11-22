package com.arktekk.scalacltest


object MatrMultTest {

  def main(args: Array[String]): Unit = {
    val start = System.currentTimeMillis
    multiplyMatrix
    println("Time: " + (System.currentTimeMillis - start))
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
  }

}
