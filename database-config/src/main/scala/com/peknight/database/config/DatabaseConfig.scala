package com.peknight.database.config

import cats.{Applicative, Monad, Show}
import com.comcast.ip4s.{Host, Port}
import com.peknight.auth.{Password, User}
import com.peknight.codec.config.given
import com.peknight.codec.cursor.Cursor
import com.peknight.codec.error.DecodingFailure
import com.peknight.codec.ip4s.instances.host.given
import com.peknight.codec.ip4s.instances.port.given
import com.peknight.codec.obj.Object
import com.peknight.codec.reader.{Key, Reader}
import com.peknight.codec.sum.*
import com.peknight.codec.{Codec, Decoder}
import com.peknight.data.Identifier
import com.peknight.query.Query
import com.peknight.query.config.given
import com.peknight.query.parser.parseToQuery
import org.http4s.Uri

import java.net.URLEncoder
import java.nio.charset.StandardCharsets.UTF_8

case class DatabaseConfig(
                           host: Option[Host] = None,
                           user: Option[User] = None,
                           password: Option[Password] = None,
                           `type`: DatabaseType = DatabaseType.postgresql,
                           port: Option[Port] = None,
                           database: Option[Database] = None,
                           query: Query = Query.fromObject(Object.empty),
                           identifier: Option[Identifier] = None,
                           poolSize: Int = 32
                         ):
  private lazy val queryString: String = query.mkString
  def remotePort: Option[Port] = port.orElse(`type`.port)
  private def hostPort: String =
    (host, remotePort) match
      case (Some(h), Some(p)) => s"$h:$p"
      case (Some(h), _) => s"$h"
      case (_, Some(p)) => s":$p"
      case _ => ""
  def url: Uri =
    val userPassword: String = (user, password) match
      case (Some(u), Some(p)) => s"$u:${URLEncoder.encode(p.value, UTF_8)}@"
      case (Some(u), _) => s"$u@"
      case (_, Some(p)) => s":${URLEncoder.encode(p.value, UTF_8)}@"
      case _ => ""
    Uri.unsafeFromString(s"${`type`}://$userPassword$hostPort${database.map(d => s"/$d").getOrElse("")}${if queryString.isBlank then "" else s"?$queryString"}")
  def jdbcUrl: Uri = Uri.unsafeFromString(s"jdbc:${`type`}://$hostPort${database.map(d => s"/$d").getOrElse("")}${if queryString.isBlank then "" else s"?$queryString"}")
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

  given keyDecodeDatabaseConfig[F[_]](using Monad[F], Reader[F, String]): Decoder[F, Key, DatabaseConfig] =
    Decoder.derivedByKey[F, DatabaseConfig]
end DatabaseConfig
