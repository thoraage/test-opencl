import sbt._

class Project(info: ProjectInfo) extends DefaultProject(info) with AutoCompilerPlugins {

  val nativelibs4javaRepo = "NativeLibs4Java Repository" at "http://nativelibs4java.sourceforge.net/maven/"
  val m2local = "M2 Local" at "file:///Users/thoraageeldby/.m2/repository"

  val scalacl = "com.nativelibs4java" % "scalacl" % "1.0-SNAPSHOT" withSources
  val scalaclPlugin = compilerPlugin("com.nativelibs4java" % "scalacl-compiler-plugin" % "1.0-CUSTOM-SNAPSHOT") 

}
