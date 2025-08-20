import com.peknight.build.gav.*
import com.peknight.build.sbt.*

commonSettings

lazy val database = (project in file("."))
  .settings(name := "database")
  .aggregate(
    databaseCore.jvm,
    databaseCore.js,
    databaseCore.native,
    databaseData.jvm,
    databaseData.js,
    databaseData.native,
  )

lazy val databaseCore = (crossProject(JVMPlatform, JSPlatform, NativePlatform) in file("database-core"))
  .settings(name := "database-core")

lazy val databaseData = (crossProject(JVMPlatform, JSPlatform, NativePlatform) in file("database-data"))
  .settings(name := "database-data")
