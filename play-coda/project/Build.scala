import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "play-coda"
  val appVersion      = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    // Add your project dependencies here,
    javaCore,
    javaJdbc,
    javaEbean
  )

  val main = play.Project(appName, appVersion, appDependencies).settings(
    requireJs += "main.js",
    requireNativePath := Some("/usr/local/lib/node_modules/requirejs/bin/r.js")
  )

}
