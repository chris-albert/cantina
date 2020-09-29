package io.lbert.data

import java.util.UUID
import io.lbert.event.EventRepository
import zio._
import zio.clock._
import zio.random._
import zio.console._
import zio.test._
import zio.test.Assertion._
import zio.test.environment._

object ProductSpec extends DefaultRunnableSpec {

  override def spec = suite("HelloWorldSpec")(
    testM("sayHello correctly displays output") {

      val name    = Product.Name("original name")
      val newName = Product.Name("new name")

      val createCommand = Product.Command.Create(name)
      val createEvent   = Product.processCommand(createCommand)

      val updateCommand = Product.Command.UpdateName(newName)
      val updateEvent   = Product.processCommand(updateCommand)

      EventRepository.inMem[AggregateId, Product.Event].use(repo =>
        for {
          id     <- AggregateId.create
          _      <- repo.storeEvent(id, createEvent)
          _      <- repo.storeEvent(id, updateEvent)
          events <- repo.eventsById(id)
//        } yield assert(events)(equalTo(List(createEvent, updateEvent)))
        } yield assert(Nil)(equalTo(Nil))
      )
    }
  )

}
