package io.lbert.event

import io.lbert.data.AggregateId
import zio._
import zio.clock._
import zio.random._
import zio.console._
import zio.test._
import zio.test.Assertion._
import zio.test.environment._

object EventRepositorySpec extends DefaultRunnableSpec {

  case class Test(t: String)

  private def algebra(repo: UManaged[EventRepository[AggregateId, Test]]) =
    suite("EventRepository Algebra")(
      testM("Law: Doing nothing means empty events and ids")(
        repo.use(r =>
          for {
            id     <- AggregateId.create
            events <- r.eventsById(id)
            ids    <- r.getAllIds
          } yield assert(events)(equalTo(Nil))
            .both(assert(ids)(equalTo(Nil)))
        )
      ),
      testM("Law: Adding one item will return it and the id")(
        repo.use(r =>
          for {
            id     <- AggregateId.create
            item1   = Test("item1")
            _      <- r.storeEvent(id, item1)
            events <- r.eventsById(id)
            ids    <- r.getAllIds
          } yield assert(events)(equalTo(List(item1)))
            .both(assert(ids)(equalTo(List(id))))
        )
      ),
      testM("Law: Adding two items will return them in the correct order")(
        repo.use(r =>
          for {
            id      <- AggregateId.create
            item1    = Test("item1")
            item2    = Test("item2")
            _       <- r.storeEvent(id, item1)
            _       <- r.storeEvent(id, item2)
            events1 <- r.eventsById(id)
            events2 <- r.eventsById(id)
            ids     <- r.getAllIds
          } yield assert(events1)(equalTo(List(item1, item2)))
            .both(assert(events2)(equalTo(List(item1, item2))))
            .both(assert(ids)(equalTo(List(id))))
        )
      ),
      testM("Law: Adding two diff events will return both ids")(
        repo.use(r =>
          for {
            id1   <- AggregateId.create
            id2   <- AggregateId.create
            item1  = Test("item1")
            item2  = Test("item2")
            _     <- r.storeEvent(id1, item1)
            _     <- r.storeEvent(id2, item2)
            ids   <- r.getAllIds
          } yield assert(ids.sortBy(_.toString))(equalTo(List(id1, id2).sortBy(_.toString)))
        )
      )
  )

  override def spec =
    algebra(EventRepository.inMem[AggregateId, Test])
}
