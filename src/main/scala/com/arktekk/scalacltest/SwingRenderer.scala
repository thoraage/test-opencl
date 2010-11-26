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
    val intBuffer = time("Rendering: ") { render }
    val image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
    for (x <- 0 until width; y <- 0 until height) {
      val value = intBuffer.get(x + y * width)
      def crazyNormalize(v: Int) = v + v * 256 + v * 65536
      if (random.nextBoolean)
        image.setRGB(x, y, crazyNormalize(x * 256 / width))
      else
        image.setRGB(x, y, crazyNormalize(value / 256))
    }
    SwingUtilities.invokeLater(new Runnable() {
      def run: Unit = {
        icon.setImage(image)
        f.repaint()
      }
    })
  }

}