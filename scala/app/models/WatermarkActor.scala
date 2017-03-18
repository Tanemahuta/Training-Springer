package models

import akka.actor.{Actor, ActorLogging}
import ch.eike.spring.domain.{BookWatermark, DocumentWatermark, JournalWatermark, Topic}
import play.api.Configuration

import scala.collection.JavaConversions._
import scala.reflect.ClassTag
import scala.util.Random

/**
  * {@link Actor} which creates a watermark.
  */
class WatermarkActor(val config: Configuration) extends Actor with ActorLogging {

  private val authors = readTestData("authors")
  private val titles = readTestData("titles")
  private val topics = readTestData("topics") map (s => if (s.trim.isEmpty) None else Option(Topic.valueOf(s)))

  override def receive: Receive = {
    // NOTE I know this is kind of lame, but it might present that I've understood how actors work
    case SubmitWatermark(id) =>
      val wm = createWatermark
      log.info("Created watermark for ticket {}: {}", id, wm)
      sender ! WatermarkComplete(id, wm)
  }

  /**
    * @return a random [[DocumentWatermark]] from our sources
    */
  private def createWatermark: DocumentWatermark = {
    val author = randomFrom(authors)
    val title = randomFrom(titles)
    val topic = randomFrom(topics)

    topic map (t => new BookWatermark(author, title, t)) getOrElse new JournalWatermark(author, title)
  }

  /**
    * Get a random item from the provided [[List]]
    *
    * @param src the source list from which to pick one
    * @tparam T the type of an item
    * @return the picked item
    */
  private def randomFrom[T: ClassTag](src: List[T]): T = src(Random.nextInt(src.size))

  private def readTestData(path : String) = asScalaBuffer(config.getList(s"testdata.$path").get.unwrapped()).toList.map(a => a.toString)

}
