package controllers

import java.util.UUID
import javax.inject.Inject

import akka.actor.{ActorSystem, Props}
import models.{DocumentWatermark, SubmitWatermark, WatermarkActor, WatermarkSaveActor}
import models.WatermarkSaveActor.StorageFunction
import play.api.mvc._
import play.api.{Configuration, Logger}
import views.html

import scala.concurrent.Future

/**
  * A logging action
  */
object LoggingAction extends ActionBuilder[Request] {
  def invokeBlock[A](request: Request[A], block: (Request[A]) => Future[Result]) = {
    Logger.debug(s"Calling action for request: $request")
    block(request)
  }
}

/**
  * Created by Tanemahuta on 18.03.17.
  */
class WatermarkController @Inject() (val config: Configuration) extends Controller {


  private implicit val actorSystem = ActorSystem.create("MyActorSystem")
  private val saveActor = actorSystem.actorOf(Props(classOf[WatermarkSaveActor], WatermarkController.storeFun))
  private val watermarker = actorSystem.actorOf(Props(classOf[WatermarkActor], config))

  def submit = LoggingAction {
    val id = UUID.randomUUID()
    watermarker tell(SubmitWatermark(id), saveActor)
    Ok(html.Watermark.submitted(id))
  }

  def index = LoggingAction {
    Ok(html.Watermark.index())
  }

  def query = Action { request =>
    val id = request.getQueryString("id") map (UUID.fromString(_))
    Logger.info(s"Requested watermark $id")
    val watermark = id flatMap (WatermarkController.storage.get(_))
    Logger.info(s"Watermark: $watermark")
    Ok(html.Watermark.query(watermark))
  }

}

object WatermarkController {
  // I know, a bad way around, but I did not want to go for injecting singletons here
  private var storage = Map.empty[UUID, DocumentWatermark]

  private[WatermarkController] val storeFun: StorageFunction = (id, watermark) => {
    WatermarkController.storage += (id -> watermark)
    Logger.info(s"Stored document watermark for ticket $id")
  }

}
