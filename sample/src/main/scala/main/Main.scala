package main

import com.play.rest.core._
import unfiltered.request._
import play.api.mvc.{Action, Results}

object Main extends App {

  val routes: ReplayRoutes = {
    case r@GET(Path("/")) =>
      controllers.Application.index
    case r@GET(Path(Seg("assets" :: file))) =>
      controllers.Assets.at(path = "/public", file.mkString("/"))
    case _ => Action(Results.NotFound)
  }

  Replay(routes, 9000)
}

