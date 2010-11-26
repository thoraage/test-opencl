package com.arktekk.scalacltest

/**
 * @author Thor Åge Eldby (thoraageeldby@gmail.com)
 */

object Timer {
  def time[T](f: => T): T = time("Time: ")(f)
  def time[T](msg: String)(f: => T): T = {
    val start = System.currentTimeMillis
    val v = f
    println(msg + (System.currentTimeMillis - start))
    v
  }
}