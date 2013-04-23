import sbt._
import Keys._

object ReplayBuild extends Build {

  lazy val core = Project(id = "replay",
                          base = file("core"),
                          settings = Project.defaultSettings)
    .settings(
      version := "1.0-SNAPSHOT",
      scalaVersion := "2.10.1",
      libraryDependencies ++= Seq(
        "play" %% "play" % "2.1.1", 
        "net.databinder" %% "unfiltered" % "0.6.8",
        "commons-codec" % "commons-codec" % "1.4"
      ),
      resolvers ++= Seq("Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/")
    )

  lazy val sample = Project(id = "sample",
                          base = file("sample"),
                          settings = Project.defaultSettings).dependsOn(core).settings(scalaVersion := "2.10.1")  
}