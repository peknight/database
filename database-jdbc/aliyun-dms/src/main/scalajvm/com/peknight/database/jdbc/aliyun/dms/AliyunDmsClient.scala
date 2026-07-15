package com.peknight.database.jdbc.aliyun.dms

import cats.MonadError
import cats.effect.Sync
import cats.syntax.applicative.*
import cats.syntax.applicativeError.*
import cats.syntax.flatMap.*
import cats.syntax.functor.*
import cats.syntax.monadError.*
import cats.syntax.option.*
import com.aliyun.dms_enterprise20181101.Client
import com.aliyun.dms_enterprise20181101.models.ListColumnsResponseBody.ListColumnsResponseBodyColumnListColumn
import com.aliyun.dms_enterprise20181101.models.ListTablesResponseBody.ListTablesResponseBodyTableListTable
import com.aliyun.dms_enterprise20181101.models.SearchDatabaseResponseBody.SearchDatabaseResponseBodySearchDatabaseListSearchDatabase
import com.aliyun.dms_enterprise20181101.models.{ExecuteScriptRequest, ListColumnsRequest, ListTablesRequest, SearchDatabaseRequest}
import com.aliyun.teaopenapi.models.Config
import com.peknight.database.jdbc.aliyun.dms.AliyunDmsClient.handleError
import com.peknight.error.Error

import scala.jdk.CollectionConverters.*

/**
 * 封装阿里云 DMS SDK Client
 */
case class AliyunDmsClient(client: Client, regionId: String):
  def executeScript[F[_]: Sync](databaseId: Int, sql: String): F[ExecuteResult] =
    val request = new ExecuteScriptRequest().setDbId(databaseId).setLogic(false).setScript(sql)
    for
      response <- Sync[F].blocking(Option(client.executeScript(request))).handleError("DMS API call failed")
      body = response.flatMap(resp => Option(resp.getBody))
      success = body.flatMap(b => Option(b.getSuccess)).exists(_.booleanValue())
      _ <-
        if !success then
          val message = body.flatMap(b => Option(b.getErrorMessage)).getOrElse("")
          val errorCode = body.flatMap(b => Option(b.getErrorCode)).filter(_.nonEmpty)
          AliyunDmsException(message, errorCode).raiseError[F, Unit]
        else ().pure[F]
      results = body.flatMap(b => Option(b.getResults)).map(_.asScala.toList).getOrElse(Nil)
      res <- if results.isEmpty then ExecuteResult(Vector.empty, Nil, 0).pure[F] else
        val result = results.head
        val success = Option(result.getSuccess).exists(_.booleanValue())
        if !success then
          AliyunDmsException(s"DNS ExecuteScript failed${Option(result.getMessage).filter(_.nonEmpty).map(m => s": $m").getOrElse("")}")
            .raiseError[F, ExecuteResult]
        else
          val columnNames = Option(result.getColumnNames).map(_.asScala.toVector).getOrElse(Vector.empty)
          val rows = Option(result.getRows).map(_.asScala.toList.map(_.asScala.toMap)).getOrElse(Nil)
          val rowCount = Option(result.getRowCount).map(_.longValue()).getOrElse(rows.size.toLong)
          ExecuteResult(columnNames, rows, rowCount).pure[F]
    yield
      res

  def searchDatabase[F[_]: Sync](searchKey: String): F[List[SearchDatabaseResponseBodySearchDatabaseListSearchDatabase]] =
    val request = new SearchDatabaseRequest().setSearchKey(searchKey).setPageNumber(1).setPageSize(100)
    Sync[F].blocking(Option(client.searchDatabase(request))).handleError("SearchDatabase failed").map { response =>
      val option =
        for
          resp <- response
          body <- Option(resp.getBody)
          searchDatabaseList <- Option(body.getSearchDatabaseList)
          res <- Option(searchDatabaseList.getSearchDatabase)
        yield
          res.asScala.toList
      option.getOrElse(Nil)
    }

  def listTables[F[_]: Sync](databaseId: Int, searchName: Option[String]): F[List[ListTablesResponseBodyTableListTable]] =
    val request = new ListTablesRequest()
      .setDatabaseId(databaseId.toString)
      .setPageNumber(1)
      .setPageSize(200)
      .setSearchName(searchName.filter(_.nonEmpty).orNull)
    Sync[F].blocking(Option(client.listTables(request))).handleError("ListTables failed").map { response =>
      val option =
        for
          resp <- response
          body <- Option(resp.getBody)
          tableList <- Option(body.getTableList)
          table <- Option(tableList.getTable)
        yield
          table.asScala.toList
      option.getOrElse(Nil)
    }

  def listColumns[F[_]: Sync](tableId: Long): F[List[ListColumnsResponseBodyColumnListColumn]] =
    val request = new ListColumnsRequest().setTableId(tableId.toString).setLogic(false)
    Sync[F].blocking(Option(client.listColumns(request))).handleError("ListColumns failed").map { response =>
      val option =
        for
          resp <- response
          body <- Option(resp.getBody)
          columnList <- Option(body.getColumnList)
          column <- Option(columnList.getColumn)
        yield
          column.asScala.toList
      option.getOrElse(Nil)
    }

end AliyunDmsClient
object AliyunDmsClient:
  def apply[F[_]: Sync](accessKeyId: String, accessKeySecret: String, regionId: String): F[AliyunDmsClient] =
    Sync[F].blocking(AliyunDmsClient(
      new Client(new Config().setAccessKeyId(accessKeyId).setAccessKeySecret(accessKeySecret).setRegionId(regionId)),
      regionId)
    ).handleError("Failed to initialize DMS client")

  extension [F[_], A] (fa: F[A])
    private[dms] def handleError(message: String)(using MonadError[F, Throwable]): F[A] =
      fa.attempt
        .map(either => either.left.map {
          case e: AliyunDmsException => e
          case e =>
            val msg = s"$message${Option(e.getMessage).filter(_.nonEmpty).map(m => s": $m").getOrElse("")}"
            AliyunDmsException(msg, cause = Error(e).some)
        })
        .rethrow
  end extension
end AliyunDmsClient
