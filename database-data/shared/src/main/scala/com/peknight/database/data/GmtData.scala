package com.peknight.database.data

import java.time.LocalDateTime

case class GmtData[A](data: A, gmtCreate: LocalDateTime, gmtModified: LocalDateTime)
