package com.play.rest.core

import play.api.mvc.{Cookies, Handler, RequestHeader}
import play.core.ApplicationProvider
import play.api.{DefaultGlobal, GlobalSettings, Play}
import java.io.{InputStream, Reader, File}
import unfiltered.request.HttpRequest
import unfiltered.Cookie

class RestApplicationProvider(routes: PartialFunction[RequestHeader, Handler], global: GlobalSettings, classLoader: ClassLoader) extends ApplicationProvider {
  val application = new ReplayApplication(routes, global, classLoader)
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

  @scala.deprecated("use the unfiltered.request.Cookies request extractor instead")
  def cookies: Seq[Cookie] = {
    val header = rh.headers.get(play.api.http.HeaderNames.COOKIE)
    val cookies: Seq[Cookie] = header.map(Cookies.decode(_)).getOrElse(Seq.empty).map { c =>
      Cookie(c.name, c.value, c.domain, Option(c.path), c.maxAge, Option(c.secure), c.httpOnly)
    }
    cookies
  }

  def isSecure: Boolean = false

  def inputStream: InputStream = throw new UnsupportedOperationException("Cannot use with Play")
  def reader: Reader = throw new UnsupportedOperationException("Cannot use with Play")

}

object PlayServer {

  private def mapper(routes: ReplayRoutes): PartialFunction[RequestHeader, Handler] = {
    case r => routes(new PlayHttpRequest(r))
  }

  def start(routes: ReplayRoutes,
            port: Int = 9000,
            global: GlobalSettings = DefaultGlobal,
            classLoader: ClassLoader = this.getClass.getClassLoader) = new play.core.server.NettyServer(new RestApplicationProvider(mapper(routes), global, classLoader), port)

}
