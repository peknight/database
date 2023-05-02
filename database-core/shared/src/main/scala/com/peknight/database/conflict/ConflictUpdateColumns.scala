package com.peknight.database.conflict

trait ConflictUpdateColumns[A]:
  def columns: Seq[String]
end ConflictUpdateColumns
