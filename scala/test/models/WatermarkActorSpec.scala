package models

import java.util.UUID

import akka.actor.{ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestKit}
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}
import play.api.Configuration
import play.api.inject.guice.GuiceApplicationBuilder

import scala.concurrent.duration._

/**
  * Created by Tanemahuta on 19.03.17.
  */
class WatermarkActorSpec extends TestKit(ActorSystem("Watermark")) with ImplicitSender with WordSpecLike with Matchers with BeforeAndAfterAll {

  val application = new GuiceApplicationBuilder().configure(Configuration("testdata.author" -> List("testAuthor"),
    "testdata.titles" -> List("testTitle"), "testdata.topics" -> List("Business"))).build

  override def afterAll {
    TestKit.shutdownActorSystem(system)
  }

  "The watermark actor" must {

    val id = UUID.randomUUID()

    "send back a watermarked document" in {
      val echo = system.actorOf(Props(classOf[models.WatermarkActor], application.configuration))
      echo ! SubmitWatermark(id)
      expectMsgPF(100 millis) {
        case WatermarkComplete(actualId, _) => actualId == id
      }
    }

  }
}

