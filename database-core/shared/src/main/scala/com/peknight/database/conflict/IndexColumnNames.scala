package com.peknight.database.conflict

trait IndexColumnNames[A] extends ConflictTarget[A]:
  def columns: Seq[String]
end IndexColumnNames
object IndexColumnNames:
  def apply[A](using indexColumnNames: IndexColumnNames[A]): IndexColumnNames[A] = indexColumnNames
end IndexColumnNames
