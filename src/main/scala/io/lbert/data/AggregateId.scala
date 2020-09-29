package io.lbert.data

import java.util.UUID
import zio.{IO, UIO}

case class AggregateId(
  uuid: UUID
)

object AggregateId {

  def create: UIO[AggregateId] =
    IO.effectTotal(AggregateId(UUID.randomUUID()))
}
