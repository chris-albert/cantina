package io.lbert.data

import zio.IO

trait Product {
  def getById(id: Product.Id): IO[Error, Option[ProductView]]

  def create(name: WithId[Product.Id, Product.Name]): IO[Error, Unit]
  def delete(id: Product.Id): IO[Error, Unit]

  def updateName(name: WithId[Product.Id, Product.Name]): IO[Error, Unit]
}

object Product {

  final case class Id(aggregateId: AggregateId)

  final case class Name(name: String)

  sealed trait Command

  object Command {
    final case class Create(name: Name) extends Command
    final case class UpdateName(name: Name) extends Command
    case object Remove extends Command
  }

  sealed trait Event

  object Event {
    final case class Created(name: Name) extends Event
    final case class UpdatedName(name: Name) extends Event
    case object Removed extends Event
  }

  def processCommand(command: Command): Event =
    command match {
      case Command.Create(name)     => Event.Created(name)
      case Command.UpdateName(name) => Event.UpdatedName(name)
      case Command.Remove           => Event.Removed
    }
}
