package controllers

import models.Security
import play.api.mvc._
import services.DataService

import javax.inject._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@Singleton
class SecurityController @Inject()(val controllerComponents: ControllerComponents,
                                   val data: DataService) extends BaseController with CrudOperations {

  def getAll(): Action[AnyContent] = Action.async {
    val future = data.getAllSecurities()
    future.map(resultList => Ok(views.html.summary(Map("security" -> resultList).toList)))
  }

  def getCreatePage(): Action[AnyContent] = Action.async {
    Future {
      Ok(views.html.new_security())
    }
  }

  def create(): Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
    val formData = request.body.asFormUrlEncoded
    data.createSecurity(Security.parseFromForm(formData))
      .map(result => if (result) Created("Complete") else BadRequest)
  }

  def getBySecid(secid: String): Action[AnyContent] = Action.async {
    data.getSecurityBySecid(secid).map(result => if (result.nonEmpty) Ok(views.html.info(result.head)) else NotFound)
  }

  def updateOrDeleteBySecid(secid: String): Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
    request.body.asFormUrlEncoded.flatMap(_.get("_method")).flatMap(_.headOption).map {
      case "DELETE" =>
        data.deleteSecuritybySecid(secid).map(result => if (result) Accepted("Delete") else NotFound)
      case "POST" =>
        val formData = request.body.asFormUrlEncoded
        data.updateSecurityBySecid(secid, Security.parseFromForm(formData))
          .map(result => if (result) Accepted("Updated") else BadRequest)
      case _ => Future {
        BadRequest
      }
    }.get
  }
}
