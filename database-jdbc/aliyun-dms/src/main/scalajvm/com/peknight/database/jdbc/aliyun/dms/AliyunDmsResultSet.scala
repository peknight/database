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

  def getByte(columnIndex: Int): Byte = ???

  def getShort(columnIndex: Int): Short = ???

  def getInt(columnIndex: Int): Int = getNumber(columnIndex, 0)(_.toInt)

  def getLong(columnIndex: Int): Long = getNumber(columnIndex, 0L)(_.toLong)

  def getFloat(columnIndex: Int): Float = ???

  def getDouble(columnIndex: Int): Double = getNumber(columnIndex, 0.0)(_.toDouble.some)

  def getBigDecimal(columnIndex: Int, scale: Int): java.math.BigDecimal = ???

  def getBytes(columnIndex: Int): Array[Byte] = ???

  def getDate(columnIndex: Int): Date = ???

  def getTime(columnIndex: Int): Time = ???

  def getTimestamp(columnIndex: Int): Timestamp = ???

  def getAsciiStream(columnIndex: Int): InputStream = ???

  def getUnicodeStream(columnIndex: Int): InputStream = ???

  def getBinaryStream(columnIndex: Int): InputStream = ???

  def getString(columnLabel: String): String = getValueOrNull(columnLabel)(_.toString)

  def getBoolean(columnLabel: String): Boolean =
    getValue(rawValue(columnLabel))(t => Decoder.toBooleanOption(t).getOrElse(false)).getOrElse(false)

  def getByte(columnLabel: String): Byte = ???

  def getShort(columnLabel: String): Short = ???

  def getInt(columnLabel: String): Int = getNumber(columnLabel, 0)(_.toInt)

  def getLong(columnLabel: String): Long = getNumber(columnLabel, 0L)(_.toLong)

  def getFloat(columnLabel: String): Float = ???

  def getDouble(columnLabel: String): Double = getNumber(columnLabel, 0.0)(_.toDouble.some)

  def getBigDecimal(columnLabel: String, scale: Int): java.math.BigDecimal = ???

  def getBytes(columnLabel: String): Array[Byte] = ???

  def getDate(columnLabel: String): Date = ???

  def getTime(columnLabel: String): Time = ???

  def getTimestamp(columnLabel: String): Timestamp = ???

  def getAsciiStream(columnLabel: String): InputStream = ???

  def getUnicodeStream(columnLabel: String): InputStream = ???

  def getBinaryStream(columnLabel: String): InputStream = ???

  def getWarnings: SQLWarning = ???

  def clearWarnings(): Unit = ???

  def getCursorName: String = ???

  def getMetaData: ResultSetMetaData = checkClosed((_, AliyunDmsResultSetMetaData(columnNames)))

  def getObject(columnIndex: Int): AnyRef = getValueOrNull(columnIndex)(_.asInstanceOf[AnyRef])

  def getObject(columnLabel: String): AnyRef = getValueOrNull(columnLabel)(_.asInstanceOf[AnyRef])

  def findColumn(columnLabel: String): Int =
    val index = columnNames.indexOf(columnLabel)
    if index < 0 then throw new SQLException(s"Column not found: $columnLabel")
    else index + 1

  def getCharacterStream(columnIndex: Int): Reader = ???

  def getCharacterStream(columnLabel: String): Reader = ???

  def getBigDecimal(columnIndex: Int): java.math.BigDecimal = ???

  def getBigDecimal(columnLabel: String): java.math.BigDecimal = ???

  def isBeforeFirst: Boolean = ???

  def isAfterLast: Boolean = ???

  def isFirst: Boolean = ???

  def isLast: Boolean = ???

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

  def getRow: Int = ???

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

  def setFetchDirection(direction: Int): Unit = ???

  def getFetchDirection: Int = ???

  def setFetchSize(rows: Int): Unit = ???

  def getFetchSize: Int = ???

  def getType: Int = ???

  def getConcurrency: Int = ???

  def rowUpdated(): Boolean = ???

  def rowInserted(): Boolean = ???

  def rowDeleted(): Boolean = ???

  def updateNull(columnIndex: Int): Unit = ???

  def updateBoolean(columnIndex: Int, x: Boolean): Unit = ???

  def updateByte(columnIndex: Int, x: Byte): Unit = ???

  def updateShort(columnIndex: Int, x: Short): Unit = ???

  def updateInt(columnIndex: Int, x: Int): Unit = ???

  def updateLong(columnIndex: Int, x: Long): Unit = ???

  def updateFloat(columnIndex: Int, x: Float): Unit = ???

  def updateDouble(columnIndex: Int, x: Double): Unit = ???

  def updateBigDecimal(columnIndex: Int, x: java.math.BigDecimal): Unit = ???

  def updateString(columnIndex: Int, x: String): Unit = ???

  def updateBytes(columnIndex: Int, x: Array[Byte]): Unit = ???

  def updateDate(columnIndex: Int, x: Date): Unit = ???

  def updateTime(columnIndex: Int, x: Time): Unit = ???

  def updateTimestamp(columnIndex: Int, x: Timestamp): Unit = ???

  def updateAsciiStream(columnIndex: Int, x: InputStream, length: Int): Unit = ???

  def updateBinaryStream(columnIndex: Int, x: InputStream, length: Int): Unit = ???

  def updateCharacterStream(columnIndex: Int, x: Reader, length: Int): Unit = ???

  def updateObject(columnIndex: Int, x: Any, scaleOrLength: Int): Unit = ???

  def updateObject(columnIndex: Int, x: Any): Unit = ???

  def updateNull(columnLabel: String): Unit = ???

  def updateBoolean(columnLabel: String, x: Boolean): Unit = ???

  def updateByte(columnLabel: String, x: Byte): Unit = ???

  def updateShort(columnLabel: String, x: Short): Unit = ???

  def updateInt(columnLabel: String, x: Int): Unit = ???

  def updateLong(columnLabel: String, x: Long): Unit = ???

  def updateFloat(columnLabel: String, x: Float): Unit = ???

  def updateDouble(columnLabel: String, x: Double): Unit = ???

  def updateBigDecimal(columnLabel: String, x: java.math.BigDecimal): Unit = ???

  def updateString(columnLabel: String, x: String): Unit = ???

  def updateBytes(columnLabel: String, x: Array[Byte]): Unit = ???

  def updateDate(columnLabel: String, x: Date): Unit = ???

  def updateTime(columnLabel: String, x: Time): Unit = ???

  def updateTimestamp(columnLabel: String, x: Timestamp): Unit = ???

  def updateAsciiStream(columnLabel: String, x: InputStream, length: Int): Unit = ???

  def updateBinaryStream(columnLabel: String, x: InputStream, length: Int): Unit = ???

  def updateCharacterStream(columnLabel: String, reader: Reader, length: Int): Unit = ???

  def updateObject(columnLabel: String, x: Any, scaleOrLength: Int): Unit = ???

  def updateObject(columnLabel: String, x: Any): Unit = ???

  def insertRow(): Unit = ???

  def updateRow(): Unit = ???

  def deleteRow(): Unit = ???

  def refreshRow(): Unit = ???

  def cancelRowUpdates(): Unit = ???

  def moveToInsertRow(): Unit = ???

  def moveToCurrentRow(): Unit = ???

  def getStatement: Statement = statement.orNull

  def getObject(columnIndex: Int, map: util.Map[String, Class[?]]): AnyRef = ???

  def getRef(columnIndex: Int): Ref = ???

  def getBlob(columnIndex: Int): Blob = ???

  def getClob(columnIndex: Int): Clob = ???

  def getArray(columnIndex: Int): sql.Array = ???

  def getObject(columnLabel: String, map: util.Map[String, Class[?]]): AnyRef = ???

  def getRef(columnLabel: String): Ref = ???

  def getBlob(columnLabel: String): Blob = ???

  def getClob(columnLabel: String): Clob = ???

  def getArray(columnLabel: String): sql.Array = ???

  def getDate(columnIndex: Int, cal: Calendar): Date = ???

  def getDate(columnLabel: String, cal: Calendar): Date = ???

  def getTime(columnIndex: Int, cal: Calendar): Time = ???

  def getTime(columnLabel: String, cal: Calendar): Time = ???

  def getTimestamp(columnIndex: Int, cal: Calendar): Timestamp = ???

  def getTimestamp(columnLabel: String, cal: Calendar): Timestamp = ???

  def getURL(columnIndex: Int): URL = ???

  def getURL(columnLabel: String): URL = ???

  def updateRef(columnIndex: Int, x: Ref): Unit = ???

  def updateRef(columnLabel: String, x: Ref): Unit = ???

  def updateBlob(columnIndex: Int, x: Blob): Unit = ???

  def updateBlob(columnLabel: String, x: Blob): Unit = ???

  def updateClob(columnIndex: Int, x: Clob): Unit = ???

  def updateClob(columnLabel: String, x: Clob): Unit = ???

  def updateArray(columnIndex: Int, x: sql.Array): Unit = ???

  def updateArray(columnLabel: String, x: sql.Array): Unit = ???

  def getRowId(columnIndex: Int): RowId = ???

  def getRowId(columnLabel: String): RowId = ???

  def updateRowId(columnIndex: Int, x: RowId): Unit = ???

  def updateRowId(columnLabel: String, x: RowId): Unit = ???

  def getHoldability: Int = ???

  def isClosed: Boolean = stateCell.get.map(_.closed).unsafeRunSync()

  def updateNString(columnIndex: Int, nString: String): Unit = ???

  def updateNString(columnLabel: String, nString: String): Unit = ???

  def updateNClob(columnIndex: Int, nClob: NClob): Unit = ???

  def updateNClob(columnLabel: String, nClob: NClob): Unit = ???

  def getNClob(columnIndex: Int): NClob = ???

  def getNClob(columnLabel: String): NClob = ???

  def getSQLXML(columnIndex: Int): SQLXML = ???

  def getSQLXML(columnLabel: String): SQLXML = ???

  def updateSQLXML(columnIndex: Int, xmlObject: SQLXML): Unit = ???

  def updateSQLXML(columnLabel: String, xmlObject: SQLXML): Unit = ???

  def getNString(columnIndex: Int): String = ???

  def getNString(columnLabel: String): String = ???

  def getNCharacterStream(columnIndex: Int): Reader = ???

  def getNCharacterStream(columnLabel: String): Reader = ???

  def updateNCharacterStream(columnIndex: Int, x: Reader, length: Long): Unit = ???

  def updateNCharacterStream(columnLabel: String, reader: Reader, length: Long): Unit = ???

  def updateAsciiStream(columnIndex: Int, x: InputStream, length: Long): Unit = ???

  def updateBinaryStream(columnIndex: Int, x: InputStream, length: Long): Unit = ???

  def updateCharacterStream(columnIndex: Int, x: Reader, length: Long): Unit = ???

  def updateAsciiStream(columnLabel: String, x: InputStream, length: Long): Unit = ???

  def updateBinaryStream(columnLabel: String, x: InputStream, length: Long): Unit = ???

  def updateCharacterStream(columnLabel: String, reader: Reader, length: Long): Unit = ???

  def updateBlob(columnIndex: Int, inputStream: InputStream, length: Long): Unit = ???

  def updateBlob(columnLabel: String, inputStream: InputStream, length: Long): Unit = ???

  def updateClob(columnIndex: Int, reader: Reader, length: Long): Unit = ???

  def updateClob(columnLabel: String, reader: Reader, length: Long): Unit = ???

  def updateNClob(columnIndex: Int, reader: Reader, length: Long): Unit = ???

  def updateNClob(columnLabel: String, reader: Reader, length: Long): Unit = ???

  def updateNCharacterStream(columnIndex: Int, x: Reader): Unit = ???

  def updateNCharacterStream(columnLabel: String, reader: Reader): Unit = ???

  def updateAsciiStream(columnIndex: Int, x: InputStream): Unit = ???

  def updateBinaryStream(columnIndex: Int, x: InputStream): Unit = ???

  def updateCharacterStream(columnIndex: Int, x: Reader): Unit = ???

  def updateAsciiStream(columnLabel: String, x: InputStream): Unit = ???

  def updateBinaryStream(columnLabel: String, x: InputStream): Unit = ???

  def updateCharacterStream(columnLabel: String, reader: Reader): Unit = ???

  def updateBlob(columnIndex: Int, inputStream: InputStream): Unit = ???

  def updateBlob(columnLabel: String, inputStream: InputStream): Unit = ???

  def updateClob(columnIndex: Int, reader: Reader): Unit = ???

  def updateClob(columnLabel: String, reader: Reader): Unit = ???

  def updateNClob(columnIndex: Int, reader: Reader): Unit = ???

  def updateNClob(columnLabel: String, reader: Reader): Unit = ???

  def getObject[T](columnIndex: Int, `type`: Class[T]): T = ???

  def getObject[T](columnLabel: String, `type`: Class[T]): T = ???

  def unwrap[T](iface: Class[T]): T = ???

  def isWrapperFor(iface: Class[?]): Boolean = ???

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

  private def getNumber[A](rawValue: IO[Option[Any]], default: => A)(f: Number => Option[A]): A =
    rawValue.flatMap {
      case Some(value) => IO(Number.parse(value).map(_.flatMap(f))).rethrow
      case _ => IO(none[A])
    }.unsafeRunSync().getOrElse(default)

  private def getNumber[A](columnIndex: Int, default: => A)(f: Number => Option[A]): A =
    getNumber(rawValue(columnIndex), default)(f)

  private def getNumber[A](columnLabel: String, default: => A)(f: Number => Option[A]): A =
    getNumber(rawValue(columnLabel), default)(f)

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
end AliyunDmsResultSet

object AliyunDmsResultSet:
  private[dms] case class State(pendingRows: List[Map[String, Any]],
                                consumedRows: List[Map[String, Any]] = Nil,
                                started: Boolean = false,
                                lastReadWasNull: Boolean = false,
                                closed: Boolean = false)
  def apply(statement: Option[Statement], columnNames: Vector[String], rows: List[Map[String, Any]]): AliyunDmsResultSet =
    AtomicCell[IO]
      .of(State(rows))
      .map(stateCell => AliyunDmsResultSet(statement, columnNames, rows, stateCell))
      .unsafeRunSync()
end AliyunDmsResultSet