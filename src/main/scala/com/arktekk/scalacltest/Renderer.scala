package com.arktekk.scalacltest

import java.nio.IntBuffer

/**
 * @author Thor Åge Eldby (thoraageeldby@gmail.com)
 */

trait Renderer {
  def render: IntBuffer
  def width: Int
  def height: Int
  def draw: Unit
}