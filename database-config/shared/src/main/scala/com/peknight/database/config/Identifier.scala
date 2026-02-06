package com.peknight.database.config

import cats.{Applicative, Show}
import com.peknight.codec.Codec
import com.peknight.codec.cursor.Cursor
import com.peknight.codec.sum.StringType

case class Identifier(value: String)
object Identifier:
  given stringCodecIdentifier[F[_]: Applicative]: Codec[F, String, String, Identifier] =
    Codec.map[F, String, String, Identifier](_.value)(Identifier.apply)

  given codecIdentifierS[F[_]: Applicative, S: {StringType, Show}]: Codec[F, S, Cursor[S], Identifier] =
    Codec.codecS[F, S, Identifier]
end Identifier
