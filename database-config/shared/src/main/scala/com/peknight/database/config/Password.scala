package com.peknight.database.config

import cats.{Applicative, Show}
import com.peknight.codec.Codec
import com.peknight.codec.cursor.Cursor
import com.peknight.codec.sum.StringType

case class Password(value: String):
  override def toString: String = "<Password>"
end Password
object Password:
  given stringCodecPassword[F[_]: Applicative]: Codec[F, String, String, Password] =
    Codec.map[F, String, String, Password](_.value)(Password.apply)

  given codecPasswordS[F[_]: Applicative, S: {StringType, Show}]: Codec[F, S, Cursor[S], Password] =
    Codec.codecS[F, S, Password]
end Password
