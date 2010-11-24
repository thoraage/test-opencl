import sbt._

class Project(info: ProjectInfo) extends DefaultProject(info) with AutoCompilerPlugins {

  val nativelibs4javaRepo = "NativeLibs4Java Repository" at "http://nativelibs4java.sourceforge.net/maven/"
  val m2local = "M2 Local" at "file:///Users/thoraageeldby/.m2/repository"

  //val commonsio = "commons-io" % "commons-io" % "2.0" withSources 
  val javacl = "com.nativelibs4java" % "javacl" % "1.0-beta-5" withSources 
  //val scalacl = "com.nativelibs4java" % "scalacl" % "1.0-SNAPSHOT" withSources
  //val javacl = "com.nativelibs4java" % "javacl-bridj" % "1.0-SNAPSHOT"
  //val scalacl2 = "com.nativelibs4java" % "scalacl2-bridj" % "1.0-CUSTOM-SNAPSHOT" withSources
  //val scalaclPlugin = compilerPlugin("com.nativelibs4java" % "scalacl-compiler-plugin" % "1.0-CUSTOM-SNAPSHOT") 

}
