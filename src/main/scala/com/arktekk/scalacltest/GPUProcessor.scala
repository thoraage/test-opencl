package com.arktekk.scalacltest

import com.nativelibs4java.util.IOUtils
import com.nativelibs4java.opencl.{CLPlatform, CLDevice, JavaCL}

/**
 * @author Thor Åge Eldby (thoraageeldby@gmail.com)
 */
trait GPUProcessor {
  def sourceFile: String

  def select: (CLPlatform, CLDevice) = {
    val instances = JavaCL.listPlatforms.map(p => p.listAllDevices(true).map(d => (p, d))).flatMap(x => x)
    instances.zipWithIndex.foreach(
      (tuple: ((CLPlatform, CLDevice), Int)) => {
        val ((platform, device), n) = tuple
        println(" [" + n + "] Platform: " + platform.getName + ", device: " + device.getName)
      })
    val instance =
      if (instances.size == 0)
        error("Unable to find any OpenCL")
      else if (instances.size == 1)
        instances(0)
      else
        instances(readInt)
    println("Selected device: " + instance._2.getName)
    instance
  }

  val context = {
    val (_, device) = select
    device.getPlatform.createContext(null, device)
  }
  val program = context.createProgram(IOUtils.readText(this.getClass.getResourceAsStream(sourceFile))).build
  val queue = context.createDefaultProfilingQueue
}