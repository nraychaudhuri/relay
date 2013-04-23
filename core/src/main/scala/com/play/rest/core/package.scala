package com.play.rest

import play.api.mvc.{Handler, RequestHeader}
import unfiltered.request.HttpRequest

package object core {

  type ReplayRoutes = PartialFunction[HttpRequest[RequestHeader], Handler]

}
