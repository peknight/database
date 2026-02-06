import com.peknight.build.gav.*
import com.peknight.build.sbt.*

commonSettings

lazy val database = (project in file("."))
  .settings(name := "database")
  .aggregate(
    databaseCore.jvm,
    databaseCore.js,
    databaseConfig.jvm,
    databaseConfig.js,
  )

lazy val databaseCore = (crossProject(JVMPlatform, JSPlatform) in file("database-core"))
  .settings(name := "database-core")

lazy val databaseConfig = (crossProject(JVMPlatform, JSPlatform) in file("database-config"))
  .settings(name := "database-config")
  .settings(crossDependencies(
    peknight.codec.effect,
    peknight.query,
    peknight.codec.ip4s,
    peknight.codec.http4s,
  ))
