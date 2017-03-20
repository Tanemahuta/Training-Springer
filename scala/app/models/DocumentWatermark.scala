package models

import models.Topic.Topic

/**
  * Topic enumeration
  */
object Topic extends Enumeration {
  type Topic = Value
  val Science, Business, Media = Value
}

/**
  * Watermark base trait
  */
trait DocumentWatermark {
  /**
    * @return the author of the document
    */
  def author: String

  /**
    * @return the title of the document
    */
  def title: String
}

/**
  * A journal watermark
  *
  * @param author the author of the journal
  * @param title  the tile of the journal
  */
case class JournalWatermark(override val author: String, override val title: String) extends DocumentWatermark

/**
  * A book watermark
  *
  * @param author the author of the book
  * @param title  the title of the book
  * @param topic  the topic of the book
  */
case class BookWatermark(override val author: String, override val title: String, topic: Topic) extends DocumentWatermark
