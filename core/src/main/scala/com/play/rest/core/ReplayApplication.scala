package com.play.rest.core

import play.api.mvc.{Handler, RequestHeader}
import play.api._
import play.core.Router.Routes
import java.io.File
import play.core.{DevSettings, SourceMapper}
import play.utils.Threads

class ReplayApplication(private val cl: ClassLoader, private val pf: PartialFunction[RequestHeader, Handler])
  extends Application with WithDefaultPlugins {

  private[this] val _routes = new play.core.Router.Routes {

    override def routes: PartialFunction[RequestHeader, Handler] = pf

    def documentation: Seq[(String, String, String)] = ???
    private var _prefix = "/"
    def setPrefix(prefix: String) {
      _prefix = prefix
      List[(String,Routes)]().foreach {
        case (p, router) => router.setPrefix(prefix + (if(prefix.endsWith("/")) "" else "/") + p)
      }
    }
    def prefix = _prefix
  }

  override lazy val routes = Some(_routes)

  def path: File = new File(".")
  def classloader: ClassLoader = cl
  def sources: Option[SourceMapper] = None
  def mode: Mode.Mode = Mode.Prod
  def configuration: Configuration = fullConfiguration
  def global: GlobalSettings = DefaultGlobal

  private lazy val initialConfiguration = Threads.withContextClassLoader(this.classloader) {
    Configuration.load(path, Mode.Dev, this match {
      case dev: DevSettings => dev.devSettings
      case _ => Map.empty
    })
  }

  private lazy val fullConfiguration = global.onLoadConfig(initialConfiguration, path, classloader, mode)

}