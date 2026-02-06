package com.peknight.database.config

import cats.{Applicative, Show}
import com.peknight.codec.Codec
import com.peknight.codec.cursor.Cursor
import com.peknight.codec.sum.StringType

case class Database(value: String)
object Database:
  given stringCodecDatabase[F[_]: Applicative]: Codec[F, String, String, Database] =
    Codec.map[F, String, String, Database](_.value)(Database.apply)

  given codecDatabaseS[F[_]: Applicative, S: {StringType, Show}]: Codec[F, S, Cursor[S], Database] =
    Codec.codecS[F, S, Database]
end Database
