package com.arktekk.scalacltest

import java.nio.ByteBuffer

/**
 * @author Thor Åge Eldby (thoraageeldby@gmail.com)
 */
trait TextRenderer extends Renderer {
  def render: ByteBuffer
  def width: Int
  def height: Int
  def draw: Unit = {
    val nioBytes = render
    val length = width * height
    val seq = for (idx <- 0 until length) yield nioBytes.get(idx)
    for (idx <- 0 until seq.size) {
      if (idx != 0 && idx % width == 0) {
        println
      }
      print(seq(idx).toString)
    }
    println
  }
}
