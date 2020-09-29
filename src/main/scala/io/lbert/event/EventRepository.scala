package io.lbert.event

import zio.stm.{TMap, ZSTM}
import zio.{IO, UManaged}

trait EventRepository[Id, A] {
  def storeEvent(id: Id, event: A): IO[Error, Unit]
  def eventsById(id: Id): IO[Error, List[A]]
  def getAllIds: IO[Error, List[Id]]
}

object EventRepository {

  def inMem[Id, A]: UManaged[EventRepository[Id, A]] =
    TMap.empty[Id, List[A]].map(store =>
      new EventRepository[Id, A] {

        override def storeEvent(
          id: Id,
          event: A
        ): IO[Error, Unit] =
          store.get(id).flatMap {
            case Some(list) =>
              store.put(id, event :: list)
            case None =>
              store.put(id, List(event))
          }.commit

        override def eventsById(
          id: Id
        ): IO[Error, List[A]] =
          store.get(id)
            .flatMap {
              case Some(value) =>
                ZSTM.succeed(value.reverse)
              case None =>
                ZSTM.unit.map(_ => Nil)
            }.commit

        override def getAllIds: IO[Error, List[Id]] =
          store.keys.commit
      }
    ).commit.toManaged_

}
