package com.peknight.database.jdbc.aliyun.dms

import java.sql.{Connection, Driver, DriverPropertyInfo}
import java.util.Properties
import java.util.logging.Logger

/**
 * DMS JDBC Driver
 *
 * URL 格式: jdbc:aliyun-dms://&lt;regionId&gt;/&lt;databaseId&gt;
 * 默认 regionId 为 cn-hangzhou，可省略: jdbc:aliyun-dms:///&lt;databaseId&gt;
 * Properties: user=AccessKeyId, password=AccessKeySecret
 * 查询参数（?key=value）可覆盖 URL 中的 regionId
 */
case class AliyunDmsDriver() extends Driver:
  def connect(url: String, info: Properties): Connection = ???

  def acceptsURL(url: String): Boolean = ???

  def getPropertyInfo(url: String, info: Properties): Array[DriverPropertyInfo] = ???

  def getMajorVersion: Int = ???

  def getMinorVersion: Int = ???

  def jdbcCompliant(): Boolean = ???

  def getParentLogger: Logger = ???
end AliyunDmsDriver
object AliyunDmsDriver:

end AliyunDmsDriver
