package controllers

import play.api.mvc.{Action, AnyContent}

trait CrudOperations {

  def getAll(): Action[AnyContent]

  def create(): Action[AnyContent]

  def getBySecid(secid: String): Action[AnyContent]

  def updateOrDeleteBySecid(secid: String): Action[AnyContent]
}
