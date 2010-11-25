package com.arktekk.scalacltest

import com.nativelibs4java.util.NIOUtils
import com.nativelibs4java.opencl.CLMem

/**
 * @author Thor Åge Eldby (thoraageeldby@gmail.com)
 */

trait FrameGPUProcessor extends GPUProcessor {
  def method: String
  def width: Int
  def height: Int
  val bufLength = width * height
  def workSize: Int
  val nioBytes = NIOUtils.directBytes(bufLength, context.getByteOrder())
  val resultsBuffer = context.createByteBuffer(CLMem.Usage.Output, nioBytes, false)
  val kernel = program.createKernel(method, width.asInstanceOf[AnyRef], workSize.asInstanceOf[AnyRef], resultsBuffer)
  kernel.enqueueNDRange(queue, Array(bufLength / workSize), Array(1))
  queue.finish
  val seq = for (idx <- 0 until bufLength) yield nioBytes.get(idx)
  for (idx <- 0 until seq.size) {
    if (idx != 0 && idx % width == 0) {
      println
    }
    print(seq(idx).toString)
  }
  println
}