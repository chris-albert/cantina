package io.lbert.data

import java.time.Instant
import zio.ZIO
import zio.clock._
import io.lbert.Error

case class Event[A](
  id  : AggregateId,
  a   : A,
  time: Instant
)

object Event {

  def apply[A](
    id: AggregateId,
    a : A
  ): ZIO[Clock, Error, Event[A]] =
    currentDateTime
      .map(dt => Event(id, a, dt.toInstant))
      .mapError(Error.TimeError)

}