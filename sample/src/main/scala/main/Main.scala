package main

import com.play.rest.core._
import unfiltered.request._
import play.api.mvc.{Action, Results}

object Main extends App {

  val routes: ReplayRoutes = {
    case GET(Path("/")) =>
      controllers.Application.index
    case GET(Path("/akka")) =>
      controllers.Application.akkaAction
    case GET(Path(Seg("assets" :: file))) =>
      controllers.Assets.at(path = "/public", file.mkString("/"))
    case _ => Action(Results.NotFound)
  }

  PlayServer.start(routes)
}

