package models

import java.util.UUID

import ch.eike.springer.domain.DocumentWatermark

/**
  * Created by Tanemahuta on 18.03.17.
  */
trait WatermarkMessage;

case class SubmitWatermark(id: UUID) extends WatermarkMessage;
case class WatermarkComplete(id: UUID, watermark: DocumentWatermark) extends WatermarkMessage;

