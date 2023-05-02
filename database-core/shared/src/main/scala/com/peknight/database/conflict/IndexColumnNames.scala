package com.peknight.database.conflict

trait IndexColumnNames[A] extends ConflictTarget[A]:
  def columns: Seq[String]
end IndexColumnNames
