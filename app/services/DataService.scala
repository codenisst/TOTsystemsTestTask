package services

import dao.{DaoHistory, DaoSecurity, DaoSummary}
import models.{History, Security, Summary}

import java.io.File
import javax.inject._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.io.Source
import scala.util.Success
import scala.xml.XML

@Singleton
class DataService @Inject()(var daoHistory: DaoHistory,
                            var daoSecurity: DaoSecurity,
                            var daoSummary: DaoSummary,
                            var httpDispatcher: HttpDispatcher) {

  def loadData(): Future[Boolean] = {
    loadSecuritiesWithFiles()
    loadHistoriesWithFiles()
    Future {
      true
    }
  }

  def getSummary(): Future[List[Summary]] = {
    daoSummary.getAll()
  }

  def getAllHistories(): Future[List[History]] = {
    daoHistory.getAll()
  }

  def createHistory(historyFromBodyRequest: History): Future[Boolean] = {
    daoHistory.save(historyFromBodyRequest)
  }

  def getHistoryBySecid(secid: String): Future[List[History]] = {
    daoHistory.get(secid)
  }

  def updateHistoryBySecid(secid: String, updHistoryFromBodyRequest: History): Future[Boolean] = {
    if (!updHistoryFromBodyRequest.secid.equals(secid)) return Future {
      false
    }
    daoHistory.update(secid, updHistoryFromBodyRequest)
  }

  def deleteHistorybySecid(secid: String): Future[Boolean] = {
    daoHistory.delete(secid)
  }

  def getAllSecurities(): Future[List[Security]] = {
    daoSecurity.getAll()
  }

  def createSecurity(securityFromBodyRequest: Security): Future[Boolean] = {
    daoSecurity.save(securityFromBodyRequest)
  }

  def getSecurityBySecid(secid: String): Future[List[Security]] = {
    daoSecurity.get(secid)
  }

  def updateSecurityBySecid(secid: String, security: Security): Future[Boolean] = {
    daoSecurity.update(secid, security)
  }

  def deleteSecuritybySecid(secid: String): Future[Boolean] = {
    daoSecurity.delete(secid)
  }

  def loadSecWithInputFile(file: File): Unit = {
    writeSec(file)
  }

  def loadHisWithInputFile(file: File): Unit = {
    writeHis(file)
  }

  private def loadHistoriesWithFiles(): Unit = {
    val files = new File("public/inputData/histories").listFiles.filter(_.getName.endsWith(".xml")).toSeq

    if (files.nonEmpty) {
      files.foreach(file => {
        val xml = XML.loadString(fixXml(file))

        val newHistories: List[History] = (xml \\ "row")
          .filter(row => row.toString().contains("SECID"))
          .map { row =>
            History.parseFromXmlFile(row)
          }.toList

        daoHistory.saveAll(newHistories)
      })
    }
  }

  private def loadSecuritiesWithFiles(): Unit = {
    val files = new File("public/inputData/securities").listFiles.filter(_.getName.endsWith(".xml")).toSeq

    if (files.nonEmpty) {
      files.foreach(file => {
        writeSec(file)
      })
    }
  }

  private def fixXml(xmlFile: File): String = {
    val source = Source.fromFile(xmlFile)
    fixXmlString(try source.mkString finally source.close())
  }

  private def fixXmlString(xmlStringInput: String): String = {
    var xmlString = xmlStringInput
    val values = xmlString.split("^<\\w+|(\\s\\w+=\")|([\"]\\s[A-z\\d]+?=\")|\"\\s/>|\"/>|\">")
    values.foreach(s => {
      if (s.contains("\"")) xmlString = xmlString.replaceAll(s, s.replaceAll("\"", "&quot;"))
      if (!s.contains("&amp;") && s.contains("&")) xmlString = xmlString.replaceAll(s, s.replaceAll("&", "&amp;"))
    })
    xmlString
  }

  private def writeSec(file: File): Unit = {
    val xml = XML.loadString(fixXml(file))

    val newSecurity: List[Security] = (xml \\ "row")
      .map(row =>
        Security.parseFromXmlFile(row)
      ).toList

    daoSecurity.saveAll(newSecurity)
  }

  private def writeHis(file: File): Unit = {
    val xml = XML.loadString(fixXml(file))

    val newHistory: List[History] = (xml \\ "row")
      .map(row =>
        History.parseFromXmlFile(row)
      ).toList

    var responsesSec = List[String]()

    val request = newHistory.map(history =>
      Future {
        httpDispatcher.sendRequest(history.secid)
      }.map(result => responsesSec = result :: responsesSec)
    )

    val futureResult = Future.sequence(request)

    futureResult.onComplete {
      case Success(results) =>
        var newSec = List[Security]()
        responsesSec.foreach(response => {
          val xml = XML.loadString(fixXmlString(response))

          val newSecurities: List[Security] = (xml \\ "row")
            .filter(row => row.toString().contains("secid"))
            .map { row =>
              Security.parseFromXmlFile(row)
            }.toList.filter(sec => newHistory.exists(his => sec.secid == his.secid))

          newSecurities.foreach(nSec => if (!newSec.contains(nSec)) newSec = nSec :: newSec)
        })
        daoSecurity.saveAll(newSec).map(f => daoHistory.saveAll(newHistory))
      case _ => println("An error occurred")
    }
  }
}