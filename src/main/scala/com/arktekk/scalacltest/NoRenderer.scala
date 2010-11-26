package com.arktekk.scalacltest

/**
 * @author Thor Åge Eldby (thoraageeldby@gmail.com)
 */

trait NoRenderer extends Renderer {
  def draw: Unit = {
    render
  }
}