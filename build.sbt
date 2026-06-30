import com.peknight.build.gav.*
import com.peknight.build.sbt.*

commonSettings

lazy val database = rootProject
  .settings(name := "database")
  .settings(publish / skip := true)
  .aggregate(databaseCore.projectRefs *)
  .aggregate(databaseConfig.projectRefs *)

lazy val databaseCore = (projectMatrix in file("database-core"))
  .settings(name := "database-core")
  .jvmPlatform(scalaVersions = Seq(scala.scala3.version))
  .jsPlatform(scalaVersions = Seq(scala.scala3.version))

lazy val databaseConfig = (projectMatrix in file("database-config"))
  .settings(name := "database-config")
  .settings(libraryDependencies ++= dependencies(
    peknight.data,
    peknight.auth,
    peknight.codec.effect,
    peknight.query,
    peknight.codec.ip4s,
    peknight.codec.http4s,
  ))
  .jvmPlatform(scalaVersions = Seq(scala.scala3.version))
  .jsPlatform(scalaVersions = Seq(scala.scala3.version))
