package com.arktekk.scalacltest

import com.nativelibs4java.util.IOUtils
import com.nativelibs4java.opencl.JavaCL

/**
 * @author Thor Åge Eldby (thoraageeldby@gmail.com)
 */

trait GPUProcessor {
  def sourceFile: String 
  val context = JavaCL.createBestContext
  val program = context.createProgram(IOUtils.readText(this.getClass.getResourceAsStream(sourceFile))).build
  val queue = context.createDefaultProfilingQueue
}