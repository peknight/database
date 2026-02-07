package com.peknight.database.config

import cats.{Applicative, Show}
import com.comcast.ip4s.{Port, port}
import com.peknight.codec.Codec
import com.peknight.codec.config.given
import com.peknight.codec.cursor.Cursor
import com.peknight.codec.derivation.EnumCodecDerivation
import com.peknight.codec.sum.StringType

enum DatabaseType(val port: Port, val driver: String):
  case postgresql extends DatabaseType(port"5432", "org.postgresql.Driver")
  case mysql extends DatabaseType(port"3306", "com.mysql.cj.jdbc.Driver")
end DatabaseType
object DatabaseType:
  given stringCodecDatabaseType[F[_]: Applicative]: Codec[F, String, String, DatabaseType] =
    EnumCodecDerivation.unsafeDerivedStringCodecEnum[F, DatabaseType]

  given codecDatabaseTypeS[F[_]: Applicative, S: {StringType, Show}]: Codec[F, S, Cursor[S], DatabaseType] =
    Codec.codecS[F, S, DatabaseType]
end DatabaseType
