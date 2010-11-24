package com.arktekk.scalacltest

object RenderTest {

  def pain(args: Array[String]): Unit = {
    val start = System.currentTimeMillis
    draw
    println("Time: " + (System.currentTimeMillis - start))
  }

  def draw: Unit = {
    val height = 30
    val width = 80
    val screen = Array.ofDim[Byte](height, width)
    for (yIdx <- 0 until screen.size) {
      for (xIdx <- 0 until screen(yIdx).size) {
	val x: Double = (yIdx - (height / 2.0)) / (height / 2.0)
	val y: Double = (xIdx - (width / 2.0)) / (width / 2.0)
	screen(yIdx)(xIdx) = if (yIdx > height / 4.0 && xIdx > width / 2.0) 1 else 0
      }
    }
    screen.foreach {
      row => println(row.foldLeft("") { (n, s) => n + s })
    }
  }

}
