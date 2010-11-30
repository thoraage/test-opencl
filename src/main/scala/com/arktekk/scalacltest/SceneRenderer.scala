package com.arktekk.scalacltest

import Timer._

/**
 * @author Thor Åge Eldby (thoraageeldby@gmail.com)
 */
object SceneRenderToDevNull extends SceneRenderer(1200, 900) with NoRenderer {
  def main(args: Array[String]): Unit = (0 until 5).foreach { _ => time { draw } }
}

object SceneRenderToSwing extends SceneRenderer(800, 600) with SwingRenderer {
  def main(args: Array[String]): Unit = time { draw }
}

object SceneRenderToText extends SceneRenderer(80, 40) with TextRenderer {
  def main(args: Array[String]): Unit = time { draw }
}


class SceneRenderer(w: Int, h: Int) extends FrameGPUProcessor {
  def sourceFile = "sceneRender.cl"
  def method = "sceneRender"
  def width = w
  def height = h
  def workSize = width * 10
}
