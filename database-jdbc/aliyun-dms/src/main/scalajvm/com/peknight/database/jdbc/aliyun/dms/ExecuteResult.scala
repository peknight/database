package com.peknight.database.jdbc.aliyun.dms

/**
 * ExecuteScript 结果封装
 */
case class ExecuteResult(columnNames: Vector[String], rows: List[Map[String, Any]], rowCount: Long)
