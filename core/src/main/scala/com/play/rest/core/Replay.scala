package com.play.rest.core

import play.api.mvc.{Handler, RequestHeader}
import play.core.ApplicationProvider
import play.api.Play
import java.io.{InputStream, Reader, File}
import unfiltered.request.HttpRequest
import unfiltered.Cookie


class RestApplicationProvider(routes: PartialFunction[RequestHeader, Handler]) extends ApplicationProvider {
  val application = new ReplayApplication(this.getClass.getClassLoader, routes)
  Play.start(application)
  def get = Right(application)
  def path = new File(".")
}

class PlayHttpRequest(rh: RequestHeader) extends HttpRequest(rh) {

  def protocol: String = rh.version

  def method: String = rh.method.toUpperCase

  def uri: String = rh.uri

  def parameterNames: Iterator[String] = rh.queryString.map(_._1).toIterator

  def parameterValues(param: String): Seq[String] = rh.queryString.getOrElse(param, Seq.empty[String])

  def headerNames: Iterator[String] = rh.headers.toMap.map(_._1).toIterator

  def headers(name: String): Iterator[String] = rh.headers.toMap.getOrElse(name, Seq.empty[String]).toIterator

  def remoteAddr: String = rh.remoteAddress

  def inputStream: InputStream = ???

  def reader: Reader = ???

  @scala.deprecated("use the unfiltered.request.Cookies request extractor instead")
  def cookies: Seq[Cookie] = ???

  def isSecure: Boolean = ???

}

object Replay {

  def mapper(routes: ReplayRoutes): PartialFunction[RequestHeader, Handler] = {
    case r => routes(new PlayHttpRequest(r))
  }

  def apply(routes: ReplayRoutes, port: Int) = new play.core.server.NettyServer(new RestApplicationProvider(mapper(routes)), port)

}
