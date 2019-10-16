name := """take-home-engineering-challenge"""
organization := "com.github.jrodbeta"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.13.0"

libraryDependencies += guice
libraryDependencies ++= Seq(
  "org.apache.commons" % "commons-csv" % "1.7",
  "org.scalatestplus.play" %% "scalatestplus-play" % "4.0.3" % Test,
  "org.mockito" % "mockito-core" % "2.7.19" % Test
)

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.github.jrodbeta.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.github.jrodbeta.binders._"