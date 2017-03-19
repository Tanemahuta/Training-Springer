package models

import java.util.UUID

import akka.actor.{Actor, ActorLogging}
import ch.eike.springer.domain.DocumentWatermark
import models.WatermarkSaveActor.StorageFunction

object WatermarkSaveActor {
  type StorageFunction = (UUID, DocumentWatermark) => Unit;
}

/**
  * Just another actor to proof, I've understood this.
  * @param store the function to be used to store saved watermarks
  */
class WatermarkSaveActor(private val store: StorageFunction) extends Actor with ActorLogging {

  override def receive: Receive = {
    case WatermarkComplete(id, watermark) =>
      store(id, watermark)
      log.info("Watermark for id {} added: {}", id, watermark)
  }
}