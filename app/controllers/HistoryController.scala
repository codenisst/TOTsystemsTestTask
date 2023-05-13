package controllers

import models.History
import play.api.mvc._
import services.DataService

import javax.inject._
import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class HistoryController @Inject()(val controllerComponents: ControllerComponents,
                                  val data: DataService) extends BaseController with CrudOperations {

  def getAll(): Action[AnyContent] = Action.async {
    val future = data.getAllHistories()
    future.map(resultList => if (resultList.nonEmpty) Ok(History.toXml(resultList)) else NotFound)
  }

  def create(): Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
    data.createHistory(History.parseXml(request.body.asXml.get.head))
      .map(result => if (result) Created("Complete") else BadRequest)
  }

  def getBySecid(secid: String): Action[AnyContent] = Action.async {
    data.getHistoryBySecid(secid).map(result => if (result.nonEmpty) Ok(History.toXml(result)) else NotFound)
  }

  def updateBySecid(secid: String): Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
    data.updateHistoryBySecid(secid, History.parseXml(request.body.asXml.get.head))
      .map(result => if (result) Accepted("Updated") else BadRequest)
  }

  def deleteBySecid(secid: String): Action[AnyContent] = Action.async {
    data.deleteHistorybySecid(secid).map(result => if (result) Accepted("Delete") else NotFound)
  }
}
