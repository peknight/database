ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.7.1"

ThisBuild / organization := "com.peknight"

ThisBuild / versionScheme := Some("early-semver")

ThisBuild / publishTo := {
  val nexus = "https://nexus.peknight.com/repository"
  if (isSnapshot.value)
    Some("snapshot" at s"$nexus/maven-snapshots/")
  else
    Some("releases" at s"$nexus/maven-releases/")
}

ThisBuild / credentials ++= Seq(
  Credentials(Path.userHome / ".sbt" / ".credentials")
)

ThisBuild / resolvers ++= Seq(
  "Pek Nexus" at "https://nexus.peknight.com/repository/maven-public/",
)

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
