package com.arktekk.scalacltest

import com.nativelibs4java.util.NIOUtils
import com.nativelibs4java.opencl.CLMem

/**
 * @author Thor Åge Eldby (thoraageeldby@gmail.com)
 */

trait FrameGPUProcessor extends GPUProcessor {
  def width: Int
  def height: Int
  def workSize: Int
  def method: String
  def render = {
    val bufLength = width * height
    val nioBytes = NIOUtils.directBytes(bufLength, context.getByteOrder())
    val resultsBuffer = context.createByteBuffer(CLMem.Usage.Output, nioBytes, false)
    val kernel = program.createKernel(method, width.asInstanceOf[AnyRef], workSize.asInstanceOf[AnyRef], resultsBuffer)
    kernel.enqueueNDRange(queue, Array(bufLength / workSize), Array(1))
    queue.finish
    nioBytes
  }
}