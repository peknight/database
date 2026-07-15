package com.peknight.database.jdbc.aliyun.dms

import cats.effect.IO
import cats.effect.std.AtomicCell
import cats.effect.unsafe.implicits.global
import cats.syntax.option.*
import com.peknight.codec.Decoder
import com.peknight.codec.number.Number
import com.peknight.database.jdbc.aliyun.dms.AliyunDmsResultSet.State

import java.io.{InputStream, Reader}
import java.net.URL
import java.sql.{Array as _, *}
import java.util.Calendar
import java.{sql, util}
import scala.annotation.tailrec
import scala.util.{Failure, Success, Try}

/**
 * DMS JDBC ResultSet
 *
 * 内部持有 rows: List[Map[String, Object]] + columnNames: List[String]
 */
case class AliyunDmsResultSet(
                               statement: Option[Statement],
                               columnNames: Vector[String],
                               rows: List[Map[String, Any]],
                               stateCell: AtomicCell[IO, State]
                             ) extends ResultSet:

  def next(): Boolean =
    checkClosed[Boolean] { state =>
      val next = relative(state, 1)
      (next, next.pendingRows.nonEmpty)
    }

  def close(): Unit = stateCell.update(state => state.copy(closed = true)).unsafeRunSync()

  def wasNull(): Boolean = stateCell.get.map(_.lastReadWasNull).unsafeRunSync()

  def getString(columnIndex: Int): String = getValueOrNull(columnIndex)(_.toString)

  def getBoolean(columnIndex: Int): Boolean =
    getValue(rawValue(columnIndex))(t => Decoder.toBooleanOption(t).getOrElse(false)).getOrElse(false)

  def getByte(columnIndex: Int): Byte = getNumber(columnIndex, 0.toByte)(_.toByte)

  def getShort(columnIndex: Int): Short = getNumber(columnIndex, 0.toShort)(_.toShort)

  def getInt(columnIndex: Int): Int = getNumber(columnIndex, 0)(_.toInt)

  def getLong(columnIndex: Int): Long = getNumber(columnIndex, 0L)(_.toLong)

  def getFloat(columnIndex: Int): Float = getNumber(columnIndex, 0.0F)(_.toFloat.some)

  def getDouble(columnIndex: Int): Double = getNumber(columnIndex, 0.0)(_.toDouble.some)

  def getBigDecimal(columnIndex: Int, scale: Int): java.math.BigDecimal =
    getNumberOrNull(columnIndex)(_.toBigDecimal.map(_.bigDecimal.setScale(scale)))

  def getBytes(columnIndex: Int): Array[Byte] = notSupported

  def getDate(columnIndex: Int): Date = getValueOrNull(columnIndex)(value => Date.valueOf(value.toString))

  def getTime(columnIndex: Int): Time = getValueOrNull(columnIndex)(value => Time.valueOf(value.toString))

  def getTimestamp(columnIndex: Int): Timestamp =
    getValueOrNull(columnIndex)(value => Timestamp.valueOf(value.toString))

  def getAsciiStream(columnIndex: Int): InputStream = notSupported

  def getUnicodeStream(columnIndex: Int): InputStream = notSupported

  def getBinaryStream(columnIndex: Int): InputStream = notSupported

  def getString(columnLabel: String): String = getValueOrNull(columnLabel)(_.toString)

  def getBoolean(columnLabel: String): Boolean =
    getValue(rawValue(columnLabel))(t => Decoder.toBooleanOption(t).getOrElse(false)).getOrElse(false)

  def getByte(columnLabel: String): Byte = getNumber(columnLabel, 0.toByte)(_.toByte)

  def getShort(columnLabel: String): Short = getNumber(columnLabel, 0.toShort)(_.toShort)

  def getInt(columnLabel: String): Int = getNumber(columnLabel, 0)(_.toInt)

  def getLong(columnLabel: String): Long = getNumber(columnLabel, 0L)(_.toLong)

  def getFloat(columnLabel: String): Float = getNumber(columnLabel, 0.0F)(_.toFloat.some)

  def getDouble(columnLabel: String): Double = getNumber(columnLabel, 0.0)(_.toDouble.some)

  def getBigDecimal(columnLabel: String, scale: Int): java.math.BigDecimal =
    getNumberOrNull(columnLabel)(_.toBigDecimal.map(_.bigDecimal.setScale(scale)))

  def getBytes(columnLabel: String): Array[Byte] = notSupported

  def getDate(columnLabel: String): Date = getValueOrNull(columnLabel)(value => Date.valueOf(value.toString))

  def getTime(columnLabel: String): Time = getValueOrNull(columnLabel)(value => Time.valueOf(value.toString))

  def getTimestamp(columnLabel: String): Timestamp =
    getValueOrNull(columnLabel)(value => Timestamp.valueOf(value.toString))

  def getAsciiStream(columnLabel: String): InputStream = notSupported

  def getUnicodeStream(columnLabel: String): InputStream = notSupported

  def getBinaryStream(columnLabel: String): InputStream = notSupported

  def getWarnings: SQLWarning = null

  def clearWarnings(): Unit = ()

  def getCursorName: String = notSupported

  def getMetaData: ResultSetMetaData = checkClosed((_, AliyunDmsResultSetMetaData(columnNames)))

  def getObject(columnIndex: Int): AnyRef = getValueOrNull(columnIndex)(_.asInstanceOf[AnyRef])

  def getObject(columnLabel: String): AnyRef = getValueOrNull(columnLabel)(_.asInstanceOf[AnyRef])

  def findColumn(columnLabel: String): Int =
    val index = columnNames.indexOf(columnLabel)
    if index < 0 then throw new SQLException(s"Column not found: $columnLabel")
    else index + 1

  def getCharacterStream(columnIndex: Int): Reader = notSupported

  def getCharacterStream(columnLabel: String): Reader = notSupported

  def getBigDecimal(columnIndex: Int): java.math.BigDecimal =
    getNumberOrNull(columnIndex)(_.toBigDecimal.map(_.bigDecimal))

  def getBigDecimal(columnLabel: String): java.math.BigDecimal =
    getNumberOrNull(columnLabel)(_.toBigDecimal.map(_.bigDecimal))

  def isBeforeFirst: Boolean = rows.nonEmpty && stateCell.get.map(!_.started).unsafeRunSync()

  def isAfterLast: Boolean = stateCell.get.map(_.pendingRows.isEmpty).unsafeRunSync()

  def isFirst: Boolean = stateCell.get.map(state => state.started && state.consumedRows.isEmpty).unsafeRunSync()

  def isLast: Boolean =
    stateCell.get.map(state => (rows.isEmpty && !state.started) || state.pendingRows.size == 1).unsafeRunSync()

  def beforeFirst(): Unit =
    checkClosed[Unit](state => (State(rows, Nil, false, state.lastReadWasNull, state.closed), ()))

  def afterLast(): Unit =
    checkClosed[Unit](state => (State(Nil, rows.reverse, true, state.lastReadWasNull, state.closed), ()))

  def first(): Boolean =
    checkClosed[Boolean](state => (state.copy(pendingRows = rows, consumedRows = Nil, started = true), rows.nonEmpty))

  def last(): Boolean =
    checkClosed[Boolean] { state =>
      rows.reverse match
        case head :: tail => (state.copy(pendingRows = head :: Nil, tail, started = true), true)
        case _ => (state, false)
    }

  def getRow: Int =
    stateCell.get.map(state => if !state.started || state.pendingRows.isEmpty then 0 else state.consumedRows.size + 1)
      .unsafeRunSync()

  def absolute(row: Int): Boolean =
    checkClosed[Boolean] { state =>
      val r = if row < 0 then rows.size + row + 1 else row
      if r <= 0 then (State(rows, Nil, false, state.lastReadWasNull, state.closed), false)
      else if r > rows.size then (State(Nil, rows.reverse, true, state.lastReadWasNull, state.closed), false)
      else
        val next = relative(State(rows, Nil, true, state.lastReadWasNull, state.closed), r - 1)
        (next, next.started && next.pendingRows.nonEmpty)
    }

  def relative(rows: Int): Boolean =
    checkClosed[Boolean] { state =>
      val next = relative(state, rows)
      (next, next.started && next.pendingRows.nonEmpty)
    }

  def previous(): Boolean =
    checkClosed[Boolean] { state =>
      val next = relative(state, -1)
      (next, next.started)
    }

  def setFetchDirection(direction: Int): Unit = ()

  def getFetchDirection: Int = ResultSet.FETCH_FORWARD

  def setFetchSize(rows: Int): Unit = ()

  def getFetchSize: Int = 0

  def getType: Int = ResultSet.TYPE_SCROLL_INSENSITIVE

  def getConcurrency: Int = ResultSet.CONCUR_READ_ONLY

  def rowUpdated(): Boolean = false

  def rowInserted(): Boolean = false

  def rowDeleted(): Boolean = false

  def updateNull(columnIndex: Int): Unit = readOnly

  def updateBoolean(columnIndex: Int, x: Boolean): Unit = readOnly

  def updateByte(columnIndex: Int, x: Byte): Unit = readOnly

  def updateShort(columnIndex: Int, x: Short): Unit = readOnly

  def updateInt(columnIndex: Int, x: Int): Unit = readOnly

  def updateLong(columnIndex: Int, x: Long): Unit = readOnly

  def updateFloat(columnIndex: Int, x: Float): Unit = readOnly

  def updateDouble(columnIndex: Int, x: Double): Unit = readOnly

  def updateBigDecimal(columnIndex: Int, x: java.math.BigDecimal): Unit = readOnly

  def updateString(columnIndex: Int, x: String): Unit = readOnly

  def updateBytes(columnIndex: Int, x: Array[Byte]): Unit = readOnly

  def updateDate(columnIndex: Int, x: Date): Unit = readOnly

  def updateTime(columnIndex: Int, x: Time): Unit = readOnly

  def updateTimestamp(columnIndex: Int, x: Timestamp): Unit = readOnly

  def updateAsciiStream(columnIndex: Int, x: InputStream, length: Int): Unit = readOnly

  def updateBinaryStream(columnIndex: Int, x: InputStream, length: Int): Unit = readOnly

  def updateCharacterStream(columnIndex: Int, x: Reader, length: Int): Unit = readOnly

  def updateObject(columnIndex: Int, x: Any, scaleOrLength: Int): Unit = readOnly

  def updateObject(columnIndex: Int, x: Any): Unit = readOnly

  def updateNull(columnLabel: String): Unit = readOnly

  def updateBoolean(columnLabel: String, x: Boolean): Unit = readOnly

  def updateByte(columnLabel: String, x: Byte): Unit = readOnly

  def updateShort(columnLabel: String, x: Short): Unit = readOnly

  def updateInt(columnLabel: String, x: Int): Unit = readOnly

  def updateLong(columnLabel: String, x: Long): Unit = readOnly

  def updateFloat(columnLabel: String, x: Float): Unit = readOnly

  def updateDouble(columnLabel: String, x: Double): Unit = readOnly

  def updateBigDecimal(columnLabel: String, x: java.math.BigDecimal): Unit = readOnly

  def updateString(columnLabel: String, x: String): Unit = readOnly

  def updateBytes(columnLabel: String, x: Array[Byte]): Unit = readOnly

  def updateDate(columnLabel: String, x: Date): Unit = readOnly

  def updateTime(columnLabel: String, x: Time): Unit = readOnly

  def updateTimestamp(columnLabel: String, x: Timestamp): Unit = readOnly

  def updateAsciiStream(columnLabel: String, x: InputStream, length: Int): Unit = readOnly

  def updateBinaryStream(columnLabel: String, x: InputStream, length: Int): Unit = readOnly

  def updateCharacterStream(columnLabel: String, reader: Reader, length: Int): Unit = readOnly

  def updateObject(columnLabel: String, x: Any, scaleOrLength: Int): Unit = readOnly

  def updateObject(columnLabel: String, x: Any): Unit = readOnly

  def insertRow(): Unit = readOnly

  def updateRow(): Unit = readOnly

  def deleteRow(): Unit = readOnly

  def refreshRow(): Unit = notSupported

  def cancelRowUpdates(): Unit = readOnly

  def moveToInsertRow(): Unit = notSupported

  def moveToCurrentRow(): Unit = notSupported

  def getStatement: Statement = statement.orNull

  def getObject(columnIndex: Int, map: util.Map[String, Class[?]]): AnyRef = getObject(columnIndex)

  def getRef(columnIndex: Int): Ref = notSupported

  def getBlob(columnIndex: Int): Blob = notSupported

  def getClob(columnIndex: Int): Clob = notSupported

  def getArray(columnIndex: Int): sql.Array = notSupported

  def getObject(columnLabel: String, map: util.Map[String, Class[?]]): AnyRef = getObject(columnLabel)

  def getRef(columnLabel: String): Ref = notSupported

  def getBlob(columnLabel: String): Blob = notSupported

  def getClob(columnLabel: String): Clob = notSupported

  def getArray(columnLabel: String): sql.Array = notSupported

  def getDate(columnIndex: Int, cal: Calendar): Date = getDate(columnIndex)

  def getDate(columnLabel: String, cal: Calendar): Date = getDate(columnLabel)

  def getTime(columnIndex: Int, cal: Calendar): Time = getTime(columnIndex)

  def getTime(columnLabel: String, cal: Calendar): Time = getTime(columnLabel)

  def getTimestamp(columnIndex: Int, cal: Calendar): Timestamp = getTimestamp(columnIndex)

  def getTimestamp(columnLabel: String, cal: Calendar): Timestamp = getTimestamp(columnLabel)

  def getURL(columnIndex: Int): URL = notSupported

  def getURL(columnLabel: String): URL = notSupported

  def updateRef(columnIndex: Int, x: Ref): Unit = notSupported

  def updateRef(columnLabel: String, x: Ref): Unit = notSupported

  def updateBlob(columnIndex: Int, x: Blob): Unit = notSupported

  def updateBlob(columnLabel: String, x: Blob): Unit = notSupported

  def updateClob(columnIndex: Int, x: Clob): Unit = notSupported

  def updateClob(columnLabel: String, x: Clob): Unit = notSupported

  def updateArray(columnIndex: Int, x: sql.Array): Unit = notSupported

  def updateArray(columnLabel: String, x: sql.Array): Unit = notSupported

  def getRowId(columnIndex: Int): RowId = notSupported

  def getRowId(columnLabel: String): RowId = notSupported

  def updateRowId(columnIndex: Int, x: RowId): Unit = notSupported

  def updateRowId(columnLabel: String, x: RowId): Unit = notSupported

  def getHoldability: Int = ResultSet.HOLD_CURSORS_OVER_COMMIT

  def isClosed: Boolean = stateCell.get.map(_.closed).unsafeRunSync()

  def updateNString(columnIndex: Int, nString: String): Unit = readOnly

  def updateNString(columnLabel: String, nString: String): Unit = readOnly

  def updateNClob(columnIndex: Int, nClob: NClob): Unit = readOnly

  def updateNClob(columnLabel: String, nClob: NClob): Unit = readOnly

  def getNClob(columnIndex: Int): NClob = notSupported

  def getNClob(columnLabel: String): NClob = notSupported

  def getSQLXML(columnIndex: Int): SQLXML = notSupported

  def getSQLXML(columnLabel: String): SQLXML = notSupported

  def updateSQLXML(columnIndex: Int, xmlObject: SQLXML): Unit = readOnly

  def updateSQLXML(columnLabel: String, xmlObject: SQLXML): Unit = readOnly

  def getNString(columnIndex: Int): String = getString(columnIndex)

  def getNString(columnLabel: String): String = getString(columnLabel)

  def getNCharacterStream(columnIndex: Int): Reader = notSupported

  def getNCharacterStream(columnLabel: String): Reader = notSupported

  def updateNCharacterStream(columnIndex: Int, x: Reader, length: Long): Unit = readOnly

  def updateNCharacterStream(columnLabel: String, reader: Reader, length: Long): Unit = readOnly

  def updateAsciiStream(columnIndex: Int, x: InputStream, length: Long): Unit = readOnly

  def updateBinaryStream(columnIndex: Int, x: InputStream, length: Long): Unit = readOnly

  def updateCharacterStream(columnIndex: Int, x: Reader, length: Long): Unit = readOnly

  def updateAsciiStream(columnLabel: String, x: InputStream, length: Long): Unit = readOnly

  def updateBinaryStream(columnLabel: String, x: InputStream, length: Long): Unit = readOnly

  def updateCharacterStream(columnLabel: String, reader: Reader, length: Long): Unit = readOnly

  def updateBlob(columnIndex: Int, inputStream: InputStream, length: Long): Unit = readOnly

  def updateBlob(columnLabel: String, inputStream: InputStream, length: Long): Unit = readOnly

  def updateClob(columnIndex: Int, reader: Reader, length: Long): Unit = readOnly

  def updateClob(columnLabel: String, reader: Reader, length: Long): Unit = readOnly

  def updateNClob(columnIndex: Int, reader: Reader, length: Long): Unit = readOnly

  def updateNClob(columnLabel: String, reader: Reader, length: Long): Unit = readOnly

  def updateNCharacterStream(columnIndex: Int, x: Reader): Unit = readOnly

  def updateNCharacterStream(columnLabel: String, reader: Reader): Unit = readOnly

  def updateAsciiStream(columnIndex: Int, x: InputStream): Unit = readOnly

  def updateBinaryStream(columnIndex: Int, x: InputStream): Unit = readOnly

  def updateCharacterStream(columnIndex: Int, x: Reader): Unit = readOnly

  def updateAsciiStream(columnLabel: String, x: InputStream): Unit = readOnly

  def updateBinaryStream(columnLabel: String, x: InputStream): Unit = readOnly

  def updateCharacterStream(columnLabel: String, reader: Reader): Unit = readOnly

  def updateBlob(columnIndex: Int, inputStream: InputStream): Unit = readOnly

  def updateBlob(columnLabel: String, inputStream: InputStream): Unit = readOnly

  def updateClob(columnIndex: Int, reader: Reader): Unit = readOnly

  def updateClob(columnLabel: String, reader: Reader): Unit = readOnly

  def updateNClob(columnIndex: Int, reader: Reader): Unit = readOnly

  def updateNClob(columnLabel: String, reader: Reader): Unit = readOnly

  def getObject[T](columnIndex: Int, `type`: Class[T]): T = getObject[T](rawValue(columnIndex), `type`)

  def getObject[T](columnLabel: String, `type`: Class[T]): T = getObject[T](rawValue(columnLabel), `type`)

  def unwrap[T](iface: Class[T]): T = handleUnwrap(iface)

  def isWrapperFor(iface: Class[?]): Boolean = iface.isInstance(this)

  private def checkClosed(state: State): IO[Unit] =
    if state.closed then IO.raiseError(new SQLException("ResultSet is closed")) else IO.unit

  private def checkClosed[A](f: State => (State, A)): A =
    stateCell.evalModify[A](state => checkClosed(state).map(_ => f(state))).unsafeRunSync()

  private def rawValue(columnIndex: Int): IO[Option[Any]] =
    if columnIndex < 1 || columnIndex > columnNames.size then
      IO.raiseError(new SQLException(s"Invalid column index: $columnIndex"))
    else
      Try(columnNames.apply(columnIndex - 1)) match
        case Success(columnLabel) => rawValue(columnLabel)
        case Failure(t) => IO.raiseError(new SQLException(s"Invalid column index: $columnIndex", t))

  private def rawValue(columnLabel: String): IO[Option[Any]] =
    stateCell.evalModify[Option[Any]] { state =>
      for
        _ <- checkClosed(state)
        _ <-
          if !state.started || state.pendingRows.isEmpty then IO.raiseError(new SQLException("No current row"))
          else IO.unit
      yield
        val value: Option[Any] = state.pendingRows.headOption.flatMap(_.get(columnLabel))
        (state.copy(lastReadWasNull = value.isEmpty), value)
    }

  private def getValue[A](rawValue: IO[Option[Any]])(f: Any => A): Option[A] =
    rawValue.flatMap {
      case Some(value) => IO(Try(f(value).some).toEither).rethrow
      case _ => IO(none[A])
    }.unsafeRunSync()

  private def getValueOrNull[A](columnIndex: Int)(f: Any => A)(using ev: Null <:< A): A =
    getValue(rawValue(columnIndex))(f).orNull

  private def getValueOrNull[A](columnLabel: String)(f: Any => A)(using ev: Null <:< A): A =
    getValue(rawValue(columnLabel))(f).orNull

  private def getObject[T](rawValue: IO[Option[Any]], `type`: Class[T]): T =
    if `type` == null then throw new SQLException("Type cannot be null")
    else rawValue.flatMap {
      case Some(value) if `type`.isInstance(value) => IO(Try(`type`.cast(value).some).toEither).rethrow
      case Some(value) => IO.raiseError(new SQLException(s"Cannot convert ${value.getClass.getName} to ${`type`.getName}"))
      case _ => IO(none[T])
    }.unsafeRunSync().getOrElse(null.asInstanceOf[T])

  private def getNumber[A](rawValue: IO[Option[Any]])(f: Number => Option[A]): Option[A] =
    rawValue.flatMap {
      case Some(value) => IO(Number.parse(value).map(_.flatMap(f))).rethrow
      case _ => IO(none[A])
    }.unsafeRunSync()

  private def getNumber[A](columnIndex: Int, default: => A)(f: Number => Option[A]): A =
    getNumber(rawValue(columnIndex))(f).getOrElse(default)

  private def getNumber[A](columnLabel: String, default: => A)(f: Number => Option[A]): A =
    getNumber(rawValue(columnLabel))(f).getOrElse(default)

  private def getNumberOrNull[A](columnIndex: Int)(f: Number => Option[A])(using ev: Null <:< A): A =
    getNumber(rawValue(columnIndex))(f).orNull

  private def getNumberOrNull[A](columnLabel: String)(f: Number => Option[A])(using ev: Null <:< A): A =
    getNumber(rawValue(columnLabel))(f).orNull

  @tailrec
  private def relative(state: State, row: Int): State =
    if row == 0 then state
    else if row > 0 then
      state match
        case State(pendingRows, consumedRows, false, lastReadWasNull, closed) =>
          relative(State(pendingRows, consumedRows, true, lastReadWasNull, closed), row - 1)
        case State(head :: tail, consumedRows, true, lastReadWasNull, closed) =>
          relative(State(tail, head :: consumedRows, true, lastReadWasNull, closed), row - 1)
        case _ => state
    else
      state match
        case State(pendingRows, head :: tail, true, lastReadWasNull, closed) =>
          relative(State(head :: pendingRows, tail, true, lastReadWasNull, closed), row + 1)
        case State(pendingRows, _, true, lastReadWasNull, closed) =>
          relative(State(pendingRows, Nil, false, lastReadWasNull, closed), row + 1)
        case _ => state
  end relative

  private def readOnly[T]: T = throw new SQLFeatureNotSupportedException("ResultSet is read-only")
end AliyunDmsResultSet

object AliyunDmsResultSet:
  private[dms] case class State(pendingRows: List[Map[String, Any]],
                                consumedRows: List[Map[String, Any]] = Nil,
                                started: Boolean = false,
                                lastReadWasNull: Boolean = false,
                                closed: Boolean = false)
  def apply(statement: Option[Statement], columnNames: Vector[String], rows: List[Map[String, Any]])
  : IO[AliyunDmsResultSet] =
    AtomicCell[IO]
      .of(State(rows))
      .map(stateCell => AliyunDmsResultSet(statement, columnNames, rows, stateCell))

  def empty(columnNames: String*): AliyunDmsResultSet = apply(None, Vector.from(columnNames), Nil).unsafeRunSync()
end AliyunDmsResultSet