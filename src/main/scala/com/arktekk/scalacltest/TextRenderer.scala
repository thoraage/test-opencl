package com.arktekk.scalacltest

/**
 * @author Thor Åge Eldby (thoraageeldby@gmail.com)
 */
trait TextRenderer extends Renderer {
  def draw: Unit = {
    val nioBytes = render
    val length = width * height
    val seq = for (idx <- 0 until length) yield nioBytes.get(idx)
    for (idx <- 0 until seq.size) {
      if (idx != 0 && idx % width == 0) {
        println
      }
      val v = seq(idx)
      print(if (v == 0) " " else ((((v & 0xff0000) >> 16) + ((v & 0xff00) >> 8) + (v & 0xff)) * 9 / (3 * 0xff)).toString)
    }
    println
  }
}
