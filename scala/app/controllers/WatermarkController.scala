package controllers

import java.util.UUID
import javax.inject.Inject

import akka.actor.{Actor, ActorLogging, ActorSystem, Props}
import ch.eike.spring.domain.DocumentWatermark
import models.WatermarkSaveActor.StorageFunction
import models.{SubmitWatermark, WatermarkActor, WatermarkComplete, WatermarkSaveActor}
import play.api.{Application, Configuration, Play}
import play.api.mvc._
import views.html

/**
  * Created by Tanemahuta on 18.03.17.
  */
class WatermarkController @Inject() (implicit val config: Configuration) extends Controller {

  private var storage = Map.empty[UUID, DocumentWatermark]
  private val storeFun: StorageFunction = (id, watermark) => storage += (id -> watermark)

  private implicit val actorSystem = ActorSystem.create("MyActorSystem")
  private val saveActor = actorSystem.actorOf(Props(classOf[WatermarkSaveActor], storeFun))
  private val watermarker = actorSystem.actorOf(Props(classOf[WatermarkActor], config))

  def submit = Action {
    val id = UUID.randomUUID()
    watermarker tell(SubmitWatermark(id), saveActor)
    Ok(html.Watermark.submitted(id))
  }

  def index = Action {
    Ok(html.Watermark.index())
  }

  def query = Action { request =>
    val watermark = request.getQueryString("id") map (UUID.fromString(_)) flatMap (storage.get(_))
    Ok(html.Watermark.query(watermark))
  }

}
