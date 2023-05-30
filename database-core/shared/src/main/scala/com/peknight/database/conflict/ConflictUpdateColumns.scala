package com.peknight.database.conflict

trait ConflictUpdateColumns[A]:
  def columns: Seq[String]
end ConflictUpdateColumns
object ConflictUpdateColumns:
  def apply[A](using conflictUpdateColumns: ConflictUpdateColumns[A]): ConflictUpdateColumns[A] = conflictUpdateColumns
end ConflictUpdateColumns
