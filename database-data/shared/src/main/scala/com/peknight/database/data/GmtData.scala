package com.peknight.database.data

import java.time.ZonedDateTime

case class GmtData[A](data: A, gmtCreate: ZonedDateTime, gmtModified: ZonedDateTime)
