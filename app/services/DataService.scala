package services

import dao.{DaoHistory, DaoSecurity, DaoSummary}
import models.{History, Security, Summary}

import java.io.File
import javax.inject._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.io.Source
import scala.xml.XML

@Singleton
class DataService @Inject()(var daoHistory: DaoHistory,
                            var daoSecurity: DaoSecurity,
                            var daoSummary: DaoSummary) {

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

  private def loadHistoriesWithFiles(): Unit = {
    val files = new File("public/inputData/histories").listFiles.filter(_.getName.endsWith(".xml")).toSeq

    if (files.nonEmpty) {
      files.foreach(file => {
        val xml = XML.loadString(fixXml(file))

        val newHistories: List[History] = (xml \\ "row")
          .filter(row => row.toString().contains("SECID"))
          .map { row =>
            History(
              secid = (row \ "@SECID").text,
              tradedate = (row \ "@TRADEDATE").text,
              numtrades = (row \ "@NUMTRADES").text.toInt,
              open = if ((row \ "@OPEN").text.nonEmpty) (row \ "@OPEN").text.toDouble else 0.0,
              close = if ((row \ "@CLOSE").text.nonEmpty) (row \ "@CLOSE").text.toDouble else 0.0
            )
          }.toList

        daoHistory.saveAll(newHistories)
      })
    }
  }

  private def loadSecuritiesWithFiles(): Unit = {
    val files = new File("public/inputData/securities").listFiles.filter(_.getName.endsWith(".xml")).toSeq

    if (files.nonEmpty) {
      files.foreach(file => {

        val xml = XML.loadString(fixXml(file))

        val newSecurity: List[Security] = (xml \\ "row")
          .map(row =>
            Security(
              id = (row \ "@id").text.toInt,
              secid = (row \ "@secid").text,
              regnumber = (row \ "@regnumber").text,
              name = (row \ "@name").text,
              emitentTitle = (row \ "@emitent_title").text
            )
          ).toList

        daoSecurity.saveAll(newSecurity)
      })
    }
  }

  private def fixXml(xmlFile: File): String = {
    val source = Source.fromFile(xmlFile)
    var xmlString = try source.mkString finally source.close()
    val values = xmlString.split("^<\\w+|(\\s\\w+=\")|([\"]\\s[A-z\\d]+?=\")|\"/>|\">")
    values.foreach(s => {
      if (s.contains("\"")) xmlString = xmlString.replaceAll(s, s.replaceAll("\"", "&quot;"))
      if (s.contains("&")) xmlString = xmlString.replaceAll(s, s.replaceAll("&", "&amp;"))
    })
    xmlString
  }
}