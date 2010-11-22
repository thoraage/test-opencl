import sbt._

class Project(info: ProjectInfo) extends DefaultProject(info) with AutoCompilerPlugins {

  val nativelibs4javaRepo = "NativeLibs4Java Repository" at "http://nativelibs4java.sourceforge.net/maven/"
  val scalacl = compilerPlugin("com.nativelibs4java" % "scalacl-compiler-plugin" % "0.1") 
  // or "1.0-SNAPSHOT" to get the latest development version

}
