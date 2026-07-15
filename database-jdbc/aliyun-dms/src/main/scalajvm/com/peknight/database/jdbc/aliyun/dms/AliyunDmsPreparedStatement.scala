package com.peknight.database.jdbc.aliyun.dms

import cats.effect.IO
import cats.effect.std.AtomicCell
import cats.effect.unsafe.implicits.global
import com.peknight.database.jdbc.aliyun.dms.AliyunDmsStatement.State

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
case class AliyunDmsPreparedStatement(connection: AliyunDmsConnection,
                                      rawSql: String,
                                      parametersCell: AtomicCell[IO, Map[Int, Any]],
                                      stateCell: AtomicCell[IO, State])
  extends AliyunDmsStatement with PreparedStatement:

  def executeQuery(): ResultSet = buildSql.flatMap(handleExecuteQuery).unsafeRunSync()

  def executeUpdate(): Int = buildSql.flatMap(handleExecuteUpdate).unsafeRunSync()

  def setNull(parameterIndex: Int, sqlType: Int): Unit =
    parametersCell.update(_.removed(parameterIndex)).unsafeRunSync()

  def setBoolean(parameterIndex: Int, x: Boolean): Unit = setValue(parameterIndex, x)

  def setByte(parameterIndex: Int, x: Byte): Unit = setValue(parameterIndex, x)

  def setShort(parameterIndex: Int, x: Short): Unit =
    setValue(parameterIndex, x)

  def setInt(parameterIndex: Int, x: Int): Unit =
    setValue(parameterIndex, x)

  def setLong(parameterIndex: Int, x: Long): Unit =
    setValue(parameterIndex, x)

  def setFloat(parameterIndex: Int, x: Float): Unit =
    setValue(parameterIndex, x)

  def setDouble(parameterIndex: Int, x: Double): Unit =
    setValue(parameterIndex, x)

  def setBigDecimal(parameterIndex: Int, x: java.math.BigDecimal): Unit =
    setValue(parameterIndex, x)

  def setString(parameterIndex: Int, x: String): Unit =
    setValue(parameterIndex, x)

  def setBytes(parameterIndex: Int, x: Array[Byte]): Unit =
    throw new SQLFeatureNotSupportedException("setBytes not supported")

  def setDate(parameterIndex: Int, x: Date): Unit =
    setValue(parameterIndex, x)

  def setTime(parameterIndex: Int, x: Time): Unit =
    setValue(parameterIndex, x)

  def setTimestamp(parameterIndex: Int, x: Timestamp): Unit =
    setValue(parameterIndex, x)

  def setAsciiStream(parameterIndex: Int, x: InputStream, length: Int): Unit = notSupported

  def setUnicodeStream(parameterIndex: Int, x: InputStream, length: Int): Unit = notSupported

  def setBinaryStream(parameterIndex: Int, x: InputStream, length: Int): Unit = notSupported

  def clearParameters(): Unit =
    parametersCell.set(Map.empty[Int, Any]).unsafeRunSync()

  def setObject(parameterIndex: Int, x: Any, targetSqlType: Int): Unit = setValue(parameterIndex, x)

  def setObject(parameterIndex: Int, x: Any): Unit = setValue(parameterIndex, x)

  def execute(): Boolean = buildSql.flatMap(handleExecute).unsafeRunSync()

  def addBatch(): Unit = notSupportBatch

  def setCharacterStream(parameterIndex: Int, reader: Reader, length: Int): Unit = notSupported

  def setRef(parameterIndex: Int, x: Ref): Unit = notSupported

  def setBlob(parameterIndex: Int, x: Blob): Unit = notSupported

  def setClob(parameterIndex: Int, x: Clob): Unit = notSupported

  def setArray(parameterIndex: Int, x: sql.Array): Unit = notSupported

  def getMetaData: ResultSetMetaData = null

  def setDate(parameterIndex: Int, x: Date, cal: Calendar): Unit =
    (Option(x), Option(cal)) match
      case (Some(date), Some(calendar)) =>
        calendar.setTime(date)
        setValue(parameterIndex, String.format("%04d-%02d-%02d", cal.get(Calendar.YEAR),
          cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH)))
      case _ => parametersCell.update(_.removed(parameterIndex)).unsafeRunSync()

  def setTime(parameterIndex: Int, x: Time, cal: Calendar): Unit =
    (Option(x), Option(cal)) match
      case (Some(time), Some(calendar)) =>
        calendar.setTime(time)
        setValue(parameterIndex, String.format("%02d:%02d:%02d", cal.get(Calendar.HOUR_OF_DAY),
          cal.get(Calendar.MINUTE), cal.get(Calendar.SECOND)))
      case _ => parametersCell.update(_.removed(parameterIndex)).unsafeRunSync()

  def setTimestamp(parameterIndex: Int, x: Timestamp, cal: Calendar): Unit =
    (Option(x), Option(cal)) match
      case (Some(timestamp), Some(calendar)) =>
        calendar.setTime(timestamp)
        setValue(parameterIndex, String.format("%04d-%02d-%02d %02d:%02d:%02d", cal.get(Calendar.YEAR),
          cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.HOUR_OF_DAY),
          cal.get(Calendar.MINUTE), cal.get(Calendar.SECOND)))
      case _ => parametersCell.update(_.removed(parameterIndex)).unsafeRunSync()

  def setNull(parameterIndex: Int, sqlType: Int, typeName: String): Unit = setNull(parameterIndex, sqlType)

  def setURL(parameterIndex: Int, x: URL): Unit = setValue(parameterIndex, x)

  def getParameterMetaData: ParameterMetaData = null

  def setRowId(parameterIndex: Int, x: RowId): Unit = notSupported

  def setNString(parameterIndex: Int, value: String): Unit = setString(parameterIndex, value)

  def setNCharacterStream(parameterIndex: Int, value: Reader, length: Long): Unit = notSupported

  def setNClob(parameterIndex: Int, value: NClob): Unit = notSupported

  def setClob(parameterIndex: Int, reader: Reader, length: Long): Unit = notSupported

  def setBlob(parameterIndex: Int, inputStream: InputStream, length: Long): Unit = notSupported

  def setNClob(parameterIndex: Int, reader: Reader, length: Long): Unit = notSupported

  def setSQLXML(parameterIndex: Int, xmlObject: SQLXML): Unit = notSupported

  def setObject(parameterIndex: Int, x: Any, targetSqlType: Int, scaleOrLength: Int): Unit =
    setObject(parameterIndex, x)

  def setAsciiStream(parameterIndex: Int, x: InputStream, length: Long): Unit = notSupported

  def setBinaryStream(parameterIndex: Int, x: InputStream, length: Long): Unit = notSupported

  def setCharacterStream(parameterIndex: Int, reader: Reader, length: Long): Unit = notSupported

  def setAsciiStream(parameterIndex: Int, x: InputStream): Unit = notSupported

  def setBinaryStream(parameterIndex: Int, x: InputStream): Unit = notSupported

  def setCharacterStream(parameterIndex: Int, reader: Reader): Unit = notSupported

  def setNCharacterStream(parameterIndex: Int, value: Reader): Unit = notSupported

  def setClob(parameterIndex: Int, reader: Reader): Unit = notSupported

  def setBlob(parameterIndex: Int, inputStream: InputStream): Unit = notSupported

  def setNClob(parameterIndex: Int, reader: Reader): Unit = notSupported

  private def buildSql: IO[String] =
    parametersCell.get.map { parameters =>
      val sb = new StringBuilder(rawSql.length + 64)
      rawSql.foldLeft[Int](1) {
        case (paramIdx, '?') =>
          sb.append(formatValue(parameters.get(paramIdx)))
          paramIdx + 1
        case (paramIdx, ch) =>
          sb.append(ch)
          paramIdx
      }
      sb.toString()
    }

  private def formatValue(value: Option[Any]): String =
    value match
      case None => "NULL"
      case Some(v: String) => s"'$v'"
      case Some(v: (Boolean | Number)) => v.toString
      case Some(v) => s"'${escapeString(v.toString)}'"

  private def escapeString(value: String): String = value.replace("\\", "\\\\").replace("'", "''")

  private def setValue(parameterIndex: Int, x: Any): Unit =
    parametersCell.update(_ + (parameterIndex -> x)).unsafeRunSync()
end AliyunDmsPreparedStatement
object AliyunDmsPreparedStatement:
  def apply(connection: AliyunDmsConnection, rawSql: String): IO[AliyunDmsPreparedStatement] =
    for
      stateCell <- AtomicCell[IO].of(State())
      parametersCell <- AtomicCell[IO].of(Map.empty[Int, Any])
    yield
      AliyunDmsPreparedStatement(connection, rawSql, parametersCell, stateCell)
end AliyunDmsPreparedStatement
