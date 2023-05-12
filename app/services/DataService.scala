package services

import dao.{DaoInMemoryHistory, DaoInMemorySecurity, DaoInMemorySummary}
import models.{History, Security, Summary}

import java.io.File
import javax.inject._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.xml.XML

@Singleton
class DataService @Inject()(var daoHistory: DaoInMemoryHistory,
                            var daoSecurity: DaoInMemorySecurity,
                            var daoSummary: DaoInMemorySummary) {

  //TODO не забыть пофиксить парсинг некорректных xml, в которых неэкранированы спецсимволы

  def loadData(): Future[Boolean] = {
    val load1 = loadSecuritiesWithFiles()
    val load2 = loadHistoriesWithFiles()
    Future {
      if (load1 || load2) true else false
    }
  }

  def getSummary(): Future[List[Summary]] = {
    daoSummary.getAll()
  }

  def getAllHistories(): Future[List[History]] = {
    daoHistory.getAll()
  }

  def createHistory(historyFromBodyRequest: History): Future[Boolean] = {
    daoHistory.save(historyFromBodyRequest, daoSecurity.getSecuritiesFromDb())
  }

  def getHistoryBySecid(secid: String): Future[Option[History]] = {
    daoHistory.get(secid)
  }

  def updateHistoryBySecid(secid: String, updHistoryFromBodyRequest: History): Future[Boolean] = {
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

  def getSecurityBySecid(secid: String): Future[Option[Security]] = {
    daoSecurity.get(secid)
  }

  def updateSecurityBySecid(secid: String, security: Security): Future[Boolean] = {
    daoSecurity.update(secid, security, daoHistory)
  }

  def deleteSecuritybySecid(secid: String): Future[Boolean] = {
    daoSecurity.delete(secid, daoHistory)
  }

  private def loadHistoriesWithFiles(): Boolean = {
    var flag = false
    val files = new File("public/inputData/histories").listFiles.filter(_.getName.endsWith(".xml")).toSeq

    if (files.nonEmpty) {
      files.foreach(file => {
        val xml = XML.loadFile(file)
        //        val source = Source.fromFile(file)
        //        val xmlString = try source.mkString finally source.close()
        //        val xml = XML.loadString(xmlString)

        val newHistories: List[History] = (xml \\ "row")
          .filter(row => row.toString().contains("SECID"))
          .map { row =>
            History(
              secid = (row \ "@SECID").text,
              tradedate = (row \ "@TRADEDATE").text,
              numtrades = (row \ "@NUMTRADES").text.toInt,
              open = (row \ "@OPEN").text.toDoubleOption,
              close = (row \ "@CLOSE").text.toDoubleOption
            )
          }.toList

        if (daoHistory.saveAll(newHistories, daoSecurity.getSecuritiesFromDb())) flag = true
      })
    }
    flag
  }

  private def loadSecuritiesWithFiles(): Boolean = {
    var flag = false
    val files = new File("public/inputData/securities").listFiles.filter(_.getName.endsWith(".xml")).toSeq

    if (files.nonEmpty) {
      files.foreach(file => {
        val xml = XML.loadFile(file)
        //        val source = Source.fromFile(file)
        //        var xmlString = (try source.mkString finally source.close())
        //        xmlString = Utility.escape(xmlString)
        //        println(xmlString)
        //        val xml = XML.loadString(xmlString)

        val newSecurity: List[Security] = (xml \\ "row")
          .map(row =>
            Security(
              id = (row \ "@id").text.toLong,
              secid = (row \ "@secid").text,
              regnumber = (row \ "@regnumber").text,
              name = (row \ "@name").text,
              emitentTitle = (row \ "@emitent_title").text
            )
          ).toList

        if (daoSecurity.saveAll(newSecurity)) flag = true
      })
    }
    flag
  }

  //  private def loadSecurity(): Unit = {
  //    try {
  //      val files = new File("public/inputData/securities").listFiles.filter(_.getName.endsWith(".xml")).toSeq
  //
  //      if (files.nonEmpty) {
  //        files.foreach(file => {
  //          val xml = XML.loadFile(file)
  //
  //          val newSecurity: Option[Security] = (XML.loadString(xml.toString()) \\ "row")
  //            .map(row =>
  //              Security(
  //                id = (row \ "@id").text.toLong,
  //                secid = (row \ "@secid").text,
  //                regnumber = (row \ "@regnumber").text,
  //                name = (row \ "@name").text,
  //                emitentTitle = (row \ "@emitent_title").text
  //              )
  //            ).headOption
  //
  //          if (newSecurity.isDefined && !securities.contains(newSecurity.get)) {
  //            securities = newSecurity.get :: securities
  //          }
  //        })
  //      }
  //    }
  //  }
}