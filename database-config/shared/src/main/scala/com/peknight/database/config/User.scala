package com.peknight.database.config

import cats.{Applicative, Show}
import com.peknight.codec.Codec
import com.peknight.codec.cursor.Cursor
import com.peknight.codec.sum.StringType

case class User(value: String)
object User:
  given stringCodecUser[F[_]: Applicative]: Codec[F, String, String, User] =
    Codec.map[F, String, String, User](_.value)(User.apply)

  given codecUserS[F[_]: Applicative, S: {StringType, Show}]: Codec[F, S, Cursor[S], User] =
    Codec.codecS[F, S, User]
end User
