name := """take-home-engineering-challenge"""
organization := "com.github.jrodbeta"

version := "0.1-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.13.0"

libraryDependencies += guice
libraryDependencies ++= Seq(
  "org.apache.commons" % "commons-csv" % "1.7",
  "org.apache.lucene" % "lucene-core" % "8.2.0",
  "org.locationtech.spatial4j" % "spatial4j" % "0.7",
  "org.apache.lucene" % "lucene-spatial" % "8.2.0",
  "org.apache.lucene" % "lucene-spatial-extras" % "8.2.0",
"org.scalatestplus.play" %% "scalatestplus-play" % "4.0.3" % Test,
  "org.mockito" % "mockito-core" % "2.7.19" % Test
)
