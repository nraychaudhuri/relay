package controllers

import play.api.mvc._

import play.api.libs.concurrent._
import play.api.Play.current

object Application extends Controller {

  def index = Action {
    Ok("hey it works for play")
  }

  def akkaAction = Action { request =>
    Async {
      Akka.future {
       Ok("Response for " + request.uri) 
      }   
    } 
  }
}
