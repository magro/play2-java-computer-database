package utils

import play.mvc.Http
import play.mvc.Http.Context;
import play.api.mvc.RequestHeader

/**
 * Helper class for request context related things. Is written
 * in scala so that it's possible to access the implicit request.
 */
object RequestHelper {

  def flashMsg(key: String)(implicit request: RequestHeader): Option[String] = {
    if (request != null) {
      // Scala implicit request
      request.flash.get(key)
    }
    // Java context

    val javaRequestContext = Http.Context.current.get()
    if (javaRequestContext != null) {
      Option(javaRequestContext.flash().get(key))
    } else {
      // Scala implicit request
      request.flash.get(key)
    }
  }
}