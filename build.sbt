name := "play-elastic-template"

version := "1.0"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  "com.evojam" %% "play-elastic4s" % "0.2.1-SNAPSHOT",
  "net.codingwell" %% "scala-guice" % "4.0.1"
)

resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"

routesGenerator := InjectedRoutesGenerator


fork in run := true