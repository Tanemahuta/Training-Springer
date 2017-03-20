package models

import java.util.UUID

import akka.actor.{Actor, ActorLogging, Props}
import models.WatermarkSaveActor.StorageFunction

/**
  * Companion object for the actor which saves processed watermarks.
  */
object WatermarkSaveActor {
  /**
    * The storage function to be used to store watermarks
    */
  type StorageFunction = (UUID, DocumentWatermark) => Unit

  /**
    * Create properties for the actor creation.
    * @param store the storage function
    * @return the properties for the actor factory
    */
  def props(store: StorageFunction) = Props(classOf[WatermarkSaveActor], store)

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