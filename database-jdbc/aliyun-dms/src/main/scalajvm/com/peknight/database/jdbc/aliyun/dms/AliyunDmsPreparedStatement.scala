package com.peknight.database.jdbc.aliyun.dms

import java.io.{InputStream, Reader}
import java.net.URL
import java.sql
import java.sql.{Array as _, *}
import java.util.Calendar

/**
 * DMS JDBC PreparedStatement
 *
 * 参数在客户端拼接到 SQL，DMS 不支持服务端参数化
 */
case class AliyunDmsPreparedStatement() extends AliyunDmsStatement with PreparedStatement:
  def executeQuery(): ResultSet = ???

  def executeUpdate(): Int = ???

  def setNull(parameterIndex: Int, sqlType: Int): Unit = ???

  def setBoolean(parameterIndex: Int, x: Boolean): Unit = ???

  def setByte(parameterIndex: Int, x: Byte): Unit = ???

  def setShort(parameterIndex: Int, x: Short): Unit = ???

  def setInt(parameterIndex: Int, x: Int): Unit = ???

  def setLong(parameterIndex: Int, x: Long): Unit = ???

  def setFloat(parameterIndex: Int, x: Float): Unit = ???

  def setDouble(parameterIndex: Int, x: Double): Unit = ???

  def setBigDecimal(parameterIndex: Int, x: java.math.BigDecimal): Unit = ???

  def setString(parameterIndex: Int, x: String): Unit = ???

  def setBytes(parameterIndex: Int, x: Array[Byte]): Unit = ???

  def setDate(parameterIndex: Int, x: Date): Unit = ???

  def setTime(parameterIndex: Int, x: Time): Unit = ???

  def setTimestamp(parameterIndex: Int, x: Timestamp): Unit = ???

  def setAsciiStream(parameterIndex: Int, x: InputStream, length: Int): Unit = ???

  def setUnicodeStream(parameterIndex: Int, x: InputStream, length: Int): Unit = ???

  def setBinaryStream(parameterIndex: Int, x: InputStream, length: Int): Unit = ???

  def clearParameters(): Unit = ???

  def setObject(parameterIndex: Int, x: Any, targetSqlType: Int): Unit = ???

  def setObject(parameterIndex: Int, x: Any): Unit = ???

  def execute(): Boolean = ???

  def addBatch(): Unit = ???

  def setCharacterStream(parameterIndex: Int, reader: Reader, length: Int): Unit = ???

  def setRef(parameterIndex: Int, x: Ref): Unit = ???

  def setBlob(parameterIndex: Int, x: Blob): Unit = ???

  def setClob(parameterIndex: Int, x: Clob): Unit = ???

  def setArray(parameterIndex: Int, x: sql.Array): Unit = ???

  def getMetaData: ResultSetMetaData = ???

  def setDate(parameterIndex: Int, x: Date, cal: Calendar): Unit = ???

  def setTime(parameterIndex: Int, x: Time, cal: Calendar): Unit = ???

  def setTimestamp(parameterIndex: Int, x: Timestamp, cal: Calendar): Unit = ???

  def setNull(parameterIndex: Int, sqlType: Int, typeName: String): Unit = ???

  def setURL(parameterIndex: Int, x: URL): Unit = ???

  def getParameterMetaData: ParameterMetaData = ???

  def setRowId(parameterIndex: Int, x: RowId): Unit = ???

  def setNString(parameterIndex: Int, value: String): Unit = ???

  def setNCharacterStream(parameterIndex: Int, value: Reader, length: Long): Unit = ???

  def setNClob(parameterIndex: Int, value: NClob): Unit = ???

  def setClob(parameterIndex: Int, reader: Reader, length: Long): Unit = ???

  def setBlob(parameterIndex: Int, inputStream: InputStream, length: Long): Unit = ???

  def setNClob(parameterIndex: Int, reader: Reader, length: Long): Unit = ???

  def setSQLXML(parameterIndex: Int, xmlObject: SQLXML): Unit = ???

  def setObject(parameterIndex: Int, x: Any, targetSqlType: Int, scaleOrLength: Int): Unit = ???

  def setAsciiStream(parameterIndex: Int, x: InputStream, length: Long): Unit = ???

  def setBinaryStream(parameterIndex: Int, x: InputStream, length: Long): Unit = ???

  def setCharacterStream(parameterIndex: Int, reader: Reader, length: Long): Unit = ???

  def setAsciiStream(parameterIndex: Int, x: InputStream): Unit = ???

  def setBinaryStream(parameterIndex: Int, x: InputStream): Unit = ???

  def setCharacterStream(parameterIndex: Int, reader: Reader): Unit = ???

  def setNCharacterStream(parameterIndex: Int, value: Reader): Unit = ???

  def setClob(parameterIndex: Int, reader: Reader): Unit = ???

  def setBlob(parameterIndex: Int, inputStream: InputStream): Unit = ???

  def setNClob(parameterIndex: Int, reader: Reader): Unit = ???
end AliyunDmsPreparedStatement
object AliyunDmsPreparedStatement:
  def apply(connection: AliyunDmsConnection, rawSql: String): AliyunDmsPreparedStatement = AliyunDmsPreparedStatement()
end AliyunDmsPreparedStatement
