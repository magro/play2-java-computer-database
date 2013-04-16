import sbt._
import Keys._
import play.Project._
import com.typesafe.sbteclipse.plugin.EclipsePlugin._

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
    EclipseKeys.withSource := true,
    EclipseKeys.executionEnvironment := Some(EclipseExecutionEnvironment.JavaSE17),
    javacOptions ++= Seq("-source", "1.7"),

    requireJs += "main.js",
    requireNativePath := Some("/usr/local/lib/node_modules/requirejs/bin/r.js")
  )

}
