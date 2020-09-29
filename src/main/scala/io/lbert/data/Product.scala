package io.lbert.data

object Product {

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
