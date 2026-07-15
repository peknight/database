package com.peknight.database.jdbc.aliyun.dms

import java.sql.{Connection, Driver, DriverPropertyInfo}
import java.util.Properties
import java.util.logging.Logger

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
