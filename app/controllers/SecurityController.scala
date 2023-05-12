package controllers

import models.Security
import play.api.mvc._
import services.DataService

import javax.inject._
import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class SecurityController @Inject()(val controllerComponents: ControllerComponents,
                                   val data: DataService) extends BaseController with CrudOperations {

  def getAll(): Action[AnyContent] = Action.async {
    val future = data.getAllSecurities()
    future.map(resultList => if (resultList.nonEmpty) Ok(Security.toXml(resultList)) else NotFound)
  }

  def create(): Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
    data.createSecurity(Security.parseXml(request.body.asXml.get.head))
      .map(result => if (result) Created("Complete") else BadRequest)
  }

  def getBySecid(secid: String): Action[AnyContent] = Action.async {
    data.getSecurityBySecid(secid).map(result => if (result.nonEmpty) Ok(Security.toXml(result.get)) else NotFound)
  }

  def updateBySecid(secid: String): Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
    data.updateSecurityBySecid(secid, Security.parseXml(request.body.asXml.get.head))
      .map(result => if (result) Accepted("Updated") else BadRequest)
  }

  def deleteBySecid(secid: String): Action[AnyContent] = Action.async {
    data.deleteSecuritybySecid(secid).map(result => if (result) Accepted("Delete") else NotFound)
  }
}
