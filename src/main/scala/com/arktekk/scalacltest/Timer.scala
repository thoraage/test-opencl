package com.arktekk.scalacltest

/**
 * @author Thor Åge Eldby (thoraageeldby@gmail.com)
 */

object Timer {
  def time[T](f: => T): T = {
    val start = System.currentTimeMillis
    val v = f
    println("Time: " + (System.currentTimeMillis - start))
    v
  }
}