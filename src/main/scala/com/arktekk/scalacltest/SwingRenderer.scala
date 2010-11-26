package com.arktekk.scalacltest

import javax.swing.{ImageIcon, JLabel, JScrollPane, JFrame}
import java.awt.image.BufferedImage
import java.util.Random
/**
 * @author Thor Åge Eldby (thoraageeldby@gmail.com)
 */

trait SwingRenderer extends Renderer {
  def draw: Unit = {
    val intBuffer = render
    val image = new BufferedImage(width, height, BufferedImage.TYPE_USHORT_GRAY)
    //val bytes = new Array[Byte](width*height)
    //byteBuffer.get(bytes)
    val random = new Random
    var maxValue = -1000000
    var minValue = 1000000
    val color = Array(0, 0, 0)
    for (x <- 0 until width; y <- 0 until height) {
      val value = intBuffer.get(x + y * width)
      if (value > maxValue)
        maxValue = value
      if (value < minValue)
        minValue = value
      def crazyNormalize(v: Int): Int = if (v > Character.MAX_VALUE / 2) v - Character.MAX_VALUE else v
      //def crazyNormalize(v: Int): Int = v & 0xffff
      //image.setRGB(x, y, x * Character.MAX_VALUE / width)
      image.setRGB(x, y, value)
    }
    //ImageIO.write(image, "png", new File("tullball.png"))
    println("Min value: " + minValue + ", max value: " + maxValue)
    //image.setRGB(0, 0, width, height, ints, 0, width)
    //image.setData(
    //  Raster.createBandedRaster(new DataBufferByte(bytes, bytes.length),
    //                width, height, width, new Array[Int](height), 0, null))
    val f = new JFrame("SwingRenderer")
    f.getContentPane().add("Center", new JScrollPane(new JLabel(new ImageIcon(image))))
    //f.getContentPane().add("North", new JLabel(label))
    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
    f.pack()
    f.setVisible(true)
  }

}