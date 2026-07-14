package com.peknight.database.jdbc.aliyun.dms

import java.sql.{ResultSetMetaData, SQLException, Types}

/**
 * DMS JDBC ResultSetMetaData
 *
 * DMS ExecuteScript 不返回列类型信息，统一使用 VARCHAR
 */
case class AliyunDmsResultSetMetaData(columnNames: Vector[String]) extends ResultSetMetaData:

  def getColumnCount: Int = columnNames.size

  def isAutoIncrement(column: Int) = false

  def isCaseSensitive(column: Int) = true

  def isSearchable(column: Int) = true

  def isCurrency(column: Int) = false

  def isNullable(column: Int): Int = ResultSetMetaData.columnNullableUnknown

  def isSigned(column: Int) = false

  def getColumnDisplaySize(column: Int) = 0

  def getColumnLabel(column: Int): String = columnNames(column - 1)

  def getColumnName(column: Int): String = columnNames(column - 1)

  def getSchemaName(column: Int) = ""

  def getPrecision(column: Int) = 0

  def getScale(column: Int) = 0

  def getTableName(column: Int) = ""

  def getCatalogName(column: Int) = ""

  def getColumnType(column: Int): Int = Types.VARCHAR

  def getColumnTypeName(column: Int) = "VARCHAR"

  def isReadOnly(column: Int) = true

  def isWritable(column: Int) = false

  def isDefinitelyWritable(column: Int) = false

  def getColumnClassName(column: Int): String = classOf[String].getName

  def unwrap[T](iface: Class[T]): T =
    if iface.isInstance(this) then iface.cast(this)
    else throw new SQLException(s"Cannot unwrap to ${iface.getName}")

  def isWrapperFor(iface: Class[?]): Boolean = iface.isInstance(this)
end AliyunDmsResultSetMetaData

