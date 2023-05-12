package controllers

import play.api.mvc.{Action, AnyContent}

trait CrudOperations {

  def getAll(): Action[AnyContent]

  def create(): Action[AnyContent]

  def getBySecid(secid: String): Action[AnyContent]

  def updateBySecid(secid: String): Action[AnyContent]

  def deleteBySecid(secid: String): Action[AnyContent]

}
