package controllers

import org.scalatestplus.play._
import play.api.Configuration
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.test._
import play.api.test.Helpers._

/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 *
 * For more information, see https://www.playframework.com/documentation/latest/ScalaTestingWithScalaTest
 */
class WatermarkControllerSpec extends PlaySpec with OneAppPerTest {

  val application = new GuiceApplicationBuilder().configure(Configuration("testdata.author" -> List("testAuthor"),
    "testdata.titles" -> List("testTitle"), "testdata.topics" -> List("Business"))).build

  "WatermarkController GET" should {

    "render the index page from a new instance of controller" in {
      val controller = new WatermarkController(app.configuration)
      val home = controller.index().apply(FakeRequest())

      status(home) mustBe OK
      contentType(home) mustBe Some("text/html")
      contentAsString(home) must include ("Welcome to your submission.")
    }

  }
}
