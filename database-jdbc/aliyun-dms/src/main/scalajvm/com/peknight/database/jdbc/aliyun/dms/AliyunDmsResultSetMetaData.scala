package com.peknight.database.jdbc.aliyun.dms

import java.sql.{ResultSetMetaData, Types}

/**
 * DMS JDBC ResultSetMetaData
 *
 * DMS ExecuteScript 不返回列类型信息，统一使用 VARCHAR
 */
case class AliyunDmsResultSetMetaData(columnNames: Vector[String]) extends ResultSetMetaData:

  def getColumnCount: Int = columnNames.size

  def isAutoIncrement(column: Int): Boolean = false

  def isCaseSensitive(column: Int): Boolean = true

  def isSearchable(column: Int): Boolean = true

  def isCurrency(column: Int): Boolean = false

  def isNullable(column: Int): Int = ResultSetMetaData.columnNullableUnknown

  def isSigned(column: Int): Boolean = false

  def getColumnDisplaySize(column: Int): Int = 0

  def getColumnLabel(column: Int): String = columnNames(column - 1)

  def getColumnName(column: Int): String = columnNames(column - 1)

  def getSchemaName(column: Int): String = ""

  def getPrecision(column: Int): Int = 0

  def getScale(column: Int): Int = 0

  def getTableName(column: Int): String = ""

  def getCatalogName(column: Int): String = ""

  def getColumnType(column: Int): Int = Types.VARCHAR

  def getColumnTypeName(column: Int): String = "VARCHAR"

  def isReadOnly(column: Int): Boolean = true

  def isWritable(column: Int): Boolean = false

  def isDefinitelyWritable(column: Int): Boolean = false

  def getColumnClassName(column: Int): String = classOf[String].getName

  def unwrap[T](iface: Class[T]): T = handleUnwrap(iface)

  def isWrapperFor(iface: Class[?]): Boolean = iface.isInstance(this)
end AliyunDmsResultSetMetaData

