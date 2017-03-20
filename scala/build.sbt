name := """scala-new"""
organization := "ch.eike.springer"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.8"

libraryDependencies += filters
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "1.5.1" % Test
libraryDependencies += "com.typesafe.akka" %% s"akka-testkit" % "2.4.14" % Test
// playTest :akka-testkit_${scalaVersion}:${akkaVersion}"


// Adds additional packages into Twirl
//TwirlKeys.templateImports += "ch.eike.springer.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "ch.eike.springer.binders._"
