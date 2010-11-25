package com.arktekk.scalacltest

import Timer._

/**
 * @author Thor Åge Eldby (thoraageeldby@gmail.com)
 */
trait TextRenderer {

  def main(args: Array[String]): Unit = {
    time { render }
  }

  def render
  def width: Int
  def height: Int
}
