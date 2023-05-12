package controllers

import models.Summary
import play.api.mvc._
import services.DataService

import javax.inject._
import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class MainController @Inject()(val controllerComponents: ControllerComponents,
                               val data: DataService) extends BaseController {

  def importInputData(): Action[AnyContent] = Action.async {
    data.loadData().map(result => if (result) Ok("Imported") else BadRequest)
  }

  def getSummaryData(): Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
    data.getSummary().map(result => if (result.nonEmpty) Ok(Summary.toXml(result)) else NotFound)
  }
}
