package com.arktekk.scalacltest

import java.awt.image.BufferedImage
import Timer._
import javax.swing._
import java.util.Random

/**
 * @author Thor Åge Eldby (thoraageeldby@gmail.com)
 */
trait SwingRenderer extends Renderer {
  val f = new JFrame("SwingRenderer")
  val icon = new ImageIcon(new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB))
  f.getContentPane().add("Center", new JScrollPane(new JLabel(icon)))
  f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
  f.pack()
  f.setVisible(true)
  val random = new Random

  def draw: Unit = {
    val intBuffer = time("Rendering: ") {render}
    val image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
    val pixels = new Array[Int](width * height)
    intBuffer.get(pixels)
    image.setRGB(0, 0, width, height, pixels, 0, width)
    SwingUtilities.invokeLater(new Runnable() {
      def run: Unit = {
        icon.setImage(image)
        f.repaint()
      }
    })
  }

}