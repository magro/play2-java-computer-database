package controllers

import play.api.mvc._
import models.Computer
import scala.concurrent.ExecutionContext.Implicits.global
import play.api.libs.ws.WS

/**
 * A sample to show a scala controller with some future and
 * async stuff.
 */
object AsyncScalaApplication extends Controller {

  def asyncSlowList(page: Int, sortBy: String, order: String, filter: String) = Action { implicit request =>
    val future = scala.concurrent.Future {
      Thread.sleep(50)
      Computer.page(page, 10, sortBy, order, filter)
    }
    Async {
      future.map(items => Ok(views.html.list.render(items, sortBy, order, filter)))
    }
  }

  def nonBlockingWs() = Action { implicit request =>
    Async {
      WS.url("http://localhost:9001/orderinfo").withQueryString(("name", "ACE")).get()
        .map(response => Ok(response.body))
    }
  }

}