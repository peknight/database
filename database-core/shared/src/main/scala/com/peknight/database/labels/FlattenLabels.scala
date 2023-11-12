package com.peknight.database.labels

trait FlattenLabels[A]:
  def labels: List[String]
end FlattenLabels

object FlattenLabels extends FlattenLabelsInstances:
  def apply[A](using flattenLabels: FlattenLabels[A]): FlattenLabels[A] = flattenLabels
end FlattenLabels
