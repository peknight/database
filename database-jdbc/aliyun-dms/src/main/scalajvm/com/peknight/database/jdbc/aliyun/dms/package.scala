package com.peknight.database.jdbc.aliyun

import java.sql.{SQLException, SQLFeatureNotSupportedException}
import scala.util.matching.Regex

package object dms:
  private[dms] def notSupported[T]: T = throw new SQLFeatureNotSupportedException()

  private[dms] def handleUnwrap[T](iface: Class[T]): T =
    if iface.isInstance(this) then iface.cast(this)
    else throw new SQLException(s"Cannot unwrap to ${iface.getName}")

  def toRegex(pattern: String): Regex =
    val replace = pattern.map {
      case '%' => ".*"
      case '_' => "."
      case ch if "\\.^$*+?{}[]|()".contains(ch) => s"\\$ch"
      case ch => s"$ch"
    }
    new Regex(s"^$replace$$")
end dms