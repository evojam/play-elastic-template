name := "play-elastic-template"

version := "1.2"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  "com.evojam" %% "play-elastic4s" % "0.3.1"
)

resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"

// Workaround of https://github.com/sbt/sbt/issues/2054:
resolvers += Resolver.url("Typesafe Ivy releases", url("https://repo.typesafe.com/typesafe/ivy-releases"))(Resolver.ivyStylePatterns)

routesGenerator := InjectedRoutesGenerator


fork in run := true
