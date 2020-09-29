package io.lbert.data


case class WithId[Id, Value](id: Id, value: Value) {
  def toTuple: (Id, Value) = id -> value
}

object WithId {

  def fromTuple[Id, Value](t: (Id, Value)): WithId[Id, Value] =
    WithId(t._1, t._2)
}
