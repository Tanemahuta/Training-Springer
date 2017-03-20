package models

import java.util.UUID

/**
  * The abstract trait of a watermark message.
  */
trait WatermarkMessage {
  /**
    * the id of the ticket
    */
  val id : UUID
}

/**
  * A message for submitting a watermark.
  * @param id the id of the ticket
  */
case class SubmitWatermark(override val id: UUID) extends WatermarkMessage

/**
  * A message for a completed watermark.
  * @param id the id of the ticket
  * @param watermark the watermark created
  */
case class WatermarkComplete(override val id: UUID, watermark: DocumentWatermark) extends WatermarkMessage

