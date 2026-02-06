package com.peknight.database.config

import cats.effect.std.Env
import cats.{Applicative, Monad, MonadError, Show}
import com.comcast.ip4s.{Host, Port}
import com.peknight.auth.{Password, User}
import com.peknight.codec.config.given
import com.peknight.codec.cursor.Cursor
import com.peknight.codec.effect.instances.envReader.given
import com.peknight.codec.error.DecodingFailure
import com.peknight.codec.ip4s.instances.host.given
import com.peknight.codec.ip4s.instances.port.given
import com.peknight.codec.obj.Object
import com.peknight.codec.reader.Key
import com.peknight.codec.sum.*
import com.peknight.codec.{Codec, Decoder}
import com.peknight.data.Identifier
import com.peknight.query.Query
import com.peknight.query.config.given
import com.peknight.query.parser.parseToQuery
import org.http4s.Uri

case class DatabaseConfig(
                           host: Host,
                           user: User,
                           password: Password,
                           `type`: DatabaseType = DatabaseType.postgresql,
                           port: Option[Port] = None,
                           database: Option[Database] = None,
                           identifier: Option[Identifier] = None,
                           query: Query = Query.fromObject(Object.empty)
                         ):
  private lazy val queryString: String = query.mkString
  def url: Uri = Uri.unsafeFromString(s"jdbc:${`type`}://$host:${port.map(_.toString).getOrElse(`type`.port.toString)}${database.map(d => s"/$d").getOrElse("")}${if queryString.isBlank then "" else s"?$queryString"}")
end DatabaseConfig
object DatabaseConfig:

  given stringCodecQuery[F[_]: Applicative]: Codec[F, String, String, Query] =
    Codec.applicative[F, String, String, Query](_.mkString)(t => parseToQuery(t).left.map(DecodingFailure.apply))

  given codecQueryS[F[_]: Applicative, S: {StringType, Show}]: Codec[F, S, Cursor[S], Query] =
    Codec.codecS[F, S, Query]

  given codecDatabaseConfig[F[_], S](using Monad[F], ObjectType[S], NullType[S], ArrayType[S], BooleanType[S],
                                     NumberType[S], StringType[S], Show[S])
  : Codec[F, S, Cursor[S], DatabaseConfig] =
    Codec.derived[F, S, DatabaseConfig]

  given keyDecodeDatabaseConfig[F[_]](using MonadError[F, Throwable], Env[F]): Decoder[F, Key, DatabaseConfig] =
    Decoder.derivedByKey[F, DatabaseConfig]
end DatabaseConfig
