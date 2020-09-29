package io.lbert.data

case class Entity[Id, Value](id: Id, value: Value) {
  def toTuple: (Id, Value) = id -> value
}

object Entity {

  def fromTuple[Id, Value](t: (Id, Value)): Entity[Id, Value] =
    Entity(t._1, t._2)
}
