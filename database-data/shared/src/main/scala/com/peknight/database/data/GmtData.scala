package com.peknight.database.data

import java.time.OffsetDateTime

case class GmtData[A](data: A, gmtCreate: OffsetDateTime, gmtModified: OffsetDateTime)
