package controllers

import play.api.libs.Files
import play.api.mvc._
import services.DataService

import javax.inject._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@Singleton
class MainController @Inject()(val controllerComponents: ControllerComponents,
                               val data: DataService) extends BaseController {

  def getImportPage(): Action[AnyContent] = Action.async { implicit request =>
    val param = request.getQueryString("type")

    if (param.isEmpty) {
      Future {
        BadRequest
      }
    } else {
      param.get match {
        case "sec" => Future {
          Ok(views.html.import_page("sec"))
        }
        case "his" => Future {
          Ok(views.html.import_page("his"))
        }
      }
    }
  }

  def importSecWithFile(): Action[MultipartFormData[Files.TemporaryFile]] = Action.async(parse.multipartFormData) { implicit request =>
    data.loadSecWithInputFile(request.body.files.head.ref.toFile)
    Future.successful(Redirect(routes.MainController.getSummaryData()))
  }

  def importHisWithFile(): Action[MultipartFormData[Files.TemporaryFile]] = Action.async(parse.multipartFormData) { implicit request =>
    data.loadHisWithInputFile(request.body.files.head.ref.toFile)
    Future.successful(Redirect(routes.MainController.getSummaryData()))
  }

  def importFromAttachedFiles(): Action[AnyContent] = Action.async {
    data.loadData().map(result => if (result) Ok("Imported") else BadRequest)
  }

  def getSummaryData(): Action[AnyContent] = Action.async {
    data.getSummary().map(result => if (result.nonEmpty) Ok(views.html.index(result)) else Ok(views.html.index(List())))
  }
}
