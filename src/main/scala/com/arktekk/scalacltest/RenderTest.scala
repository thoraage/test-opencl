package com.arktekk.scalacltest

/**
 * @author Thor Åge Eldby (thoraageeldby@gmail.com)
 */
object RenderTest extends TextRenderer {
  def height = 30
  def width = 80

  def render: Unit = gpuRenderScene

  def cpuRender: Unit = {
    val screen = Array.ofDim[Byte](height, width)
    for (yIdx <- 0 until screen.size) {
      for (xIdx <- 0 until screen(yIdx).size) {
        val x: Double = (yIdx - (height / 2.0)) / (height / 2.0)
        val y: Double = (xIdx - (width / 2.0)) / (width / 2.0)
        screen(yIdx)(xIdx) = if (yIdx > height / 4.0 && xIdx > width / 2.0) 1 else 0
      }
    }
    screen.foreach {
      row => println(row.foldLeft("") {(n, s) => n + s})
    }
  }

  def gpuSceneRenderDemo: Unit = new FrameGPUProcessor {
    def sourceFile = "sceneRenderDemo.cl"
    def method = "sceneRender"
    def width = RenderTest.width
    def height = RenderTest.height
    def workSize = width / 2    
  }

  def gpuRenderScene: Unit = new FrameGPUProcessor {
    def sourceFile = "sceneRender.cl"
    def method = "sceneRender"
    def width = RenderTest.width
    def height = RenderTest.height
    def workSize = width / 2
  }

}
