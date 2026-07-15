package com.peknight.database.jdbc.aliyun

import java.sql.{SQLException, SQLFeatureNotSupportedException}

package object dms:
  private[dms] def notSupported[T]: T = throw new SQLFeatureNotSupportedException()

  private[dms] def handleUnwrap[T](iface: Class[T]): T =
    if iface.isInstance(this) then iface.cast(this)
    else throw new SQLException(s"Cannot unwrap to ${iface.getName}")
end dms