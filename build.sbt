ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.2.1"

ThisBuild / organization := "com.peknight"

lazy val commonSettings = Seq(
  scalacOptions ++= Seq(
    "-feature",
    "-deprecation",
    "-unchecked",
    "-Xfatal-warnings",
    "-language:strictEquality",
    "-Xmax-inlines:64"
  ),
)

lazy val database = (project in file("."))
  .aggregate(
    databaseCore.jvm,
    databaseCore.js,
    databaseData.jvm,
    databaseData.js,
  )
  .enablePlugins(JavaAppPackaging)
  .settings(commonSettings)
  .settings(
    name := "database",
  )

lazy val databaseCore = (crossProject(JSPlatform, JVMPlatform) in file("database-core"))
  .settings(commonSettings)
  .settings(
    name := "database-core",
    libraryDependencies ++= Seq(
    ),
  )

lazy val databaseData = (crossProject(JSPlatform, JVMPlatform) in file("database-data"))
  .settings(commonSettings)
  .settings(
    name := "database-data",
    libraryDependencies ++= Seq(
    ),
  )
