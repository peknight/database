package com.peknight.database.jdbc.aliyun.dms

import com.peknight.error.Error

import java.sql.SQLException

case class AliyunDmsException(override val message: String, errorCode: Option[String] = None,
                              override val cause: Option[Error] = None) extends SQLException with Error
