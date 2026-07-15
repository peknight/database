package com.peknight.database.jdbc.aliyun.dms

import cats.effect.IO

import scala.jdk.CollectionConverters.*
import com.peknight.codec.number.Number
import cats.effect.unsafe.implicits.global
import com.peknight.database.jdbc.aliyun.dms.AliyunDmsDriver.urlPrefix
import org.http4s.Uri

import java.sql.{Connection, Driver, DriverManager, DriverPropertyInfo, SQLException}
import java.util.Properties
import java.util.logging.Logger
import scala.util.Try

/**
 * DMS JDBC Driver
 *
 * URL 格式: jdbc:aliyun-dms://&lt;regionId&gt;/&lt;databaseId&gt;
 * 默认 regionId 为 cn-hangzhou，可省略: jdbc:aliyun-dms:///&lt;databaseId&gt;
 * Properties: user=AccessKeyId, password=AccessKeySecret
 * 查询参数（?key=value）可覆盖 URL 中的 regionId
 */
class AliyunDmsDriver extends Driver:
  def connect(url: String, info: Properties): Connection =
    if acceptsURL(url) then
      val merged: Properties = Option(info).map { i =>
        val properties = new Properties()
        properties.putAll(i)
        properties
      }.getOrElse(new Properties)
      val io =
        for
          uri <- IO(Uri.fromString(url).left.map(parseFailure => new SQLException(s"Invalid URL: $url", parseFailure)))
            .rethrow
          regionId = uri.query.params.get("regionId").orElse(uri.host.map(_.value)).getOrElse("cn-hangzhou")
          segment <- IO(uri.path.segments.headOption.toRight(new SQLException(s"Missing databaseId in URL: $url")))
            .rethrow
          decoded <- IO(Try(segment.decoded()).toEither.left.map(e => new SQLException(s"Invalid databaseId in URL: $url", e)))
            .rethrow
          databaseId <- IO(Number.fromString(decoded).flatMap(_.toInt)
            .toRight(new SQLException(s"Invalid databaseId in URL: $url"))).rethrow
          properties = buildProperties(info, uri)
          accessKeyId <- IO(Option(properties.getProperty("user"))
            .filter(_.nonEmpty)
            .toRight(new SQLException("AccessKeyId (user) is required"))).rethrow
          accessKeySecret <- IO(Option(properties.getProperty("password"))
            .filter(_.nonEmpty)
            .toRight(new SQLException("AccessKeySecret (password) is required"))).rethrow
          client <- AliyunDmsClient[IO](accessKeyId, accessKeySecret, regionId)
          connection <- AliyunDmsConnection(client, databaseId)
        yield
          connection
      io.unsafeRunSync()
    else null

  def acceptsURL(url: String): Boolean = Option(url).exists(_.startsWith(urlPrefix))

  def getPropertyInfo(url: String, info: Properties): Array[DriverPropertyInfo] =
    val userProp = new DriverPropertyInfo("user", Option(info).flatMap(i => Option(i.getProperty("user"))).orNull)
    userProp.required = true
    val passwordProp = new DriverPropertyInfo("password", Option(info).flatMap(i => Option(i.getProperty("password"))).orNull)
    passwordProp.required = true
    val regionProp = new DriverPropertyInfo("regionId", Option(info).flatMap(i => Option(i.getProperty("regionId")))
      .getOrElse("cn-hangzhou"))
    regionProp.required = false;
    regionProp.description = "Region ID, also can be specified in URL path: jdbc:aliyun-dms://<regionId>/<databaseId>";
    Array(userProp, passwordProp, regionProp)
  end getPropertyInfo

  def getMajorVersion: Int = 0

  def getMinorVersion: Int = 1

  def jdbcCompliant(): Boolean = false

  def getParentLogger: Logger = notSupported

  private def buildProperties(info: Properties, uri: Uri): Properties =
    val properties = new Properties()
    Option(info).foreach(properties.putAll)
    properties.putAll(uri.params.asJava)
    properties
end AliyunDmsDriver
object AliyunDmsDriver:
  private[dms] val urlPrefix = "jdbc:aliyun-dms://"
  IO.blocking(Try(DriverManager.registerDriver(new AliyunDmsDriver)).toEither.left.map(e =>
    new RuntimeException("Failed to register DmsDriver", e)
  )).unsafeRunSync()
end AliyunDmsDriver
