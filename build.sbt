name := """java-app"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  javaJdbc,
  cache,
  javaWs,
  "org.json"%"org.json"%"chargebee-1.0",
  "org.powermock" % "powermock-api-mockito" % "1.6.5"
)
