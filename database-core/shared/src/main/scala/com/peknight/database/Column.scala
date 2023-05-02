package com.peknight.database

case class Column[A](name: String) extends Expr[A]