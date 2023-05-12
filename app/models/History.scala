package models

import scala.xml.Node

case class History(var secid: String, var tradedate: String, var numtrades: Int,
                   var open: Option[Double], var close: Option[Double])

object History {
  def toXml(historyList: List[History]): Node = {
    val elems = historyList.map{history =>
      <history>
        <secid>{history.secid}</secid>
        <tradedate>{history.tradedate}</tradedate>
        <numtrades>{history.numtrades}</numtrades>
        <open>{if (history.open.nonEmpty) history.open.get else ""}</open>
        <close>{if (history.close.nonEmpty) history.close.get else ""}</close>
      </history>
    }
    val xml = <histories>{elems}</histories>
    xml
  }

  def toXml(history: History): Node = {
    val xml =
      <history>
        <secid>{history.secid}</secid>
        <tradedate>{history.tradedate}</tradedate>
        <numtrades>{history.numtrades}</numtrades>
        <open>{if (history.open.nonEmpty) history.open.get else ""}</open>
        <close>{if (history.close.nonEmpty) history.close.get else ""}</close>
      </history>
    xml
  }

  def parseXml(strXml: Node): History = {
      val secid = (strXml \ "secid").text
      val tradedate = (strXml \ "tradedate").text
      val numtrades = (strXml \ "numtrades").text.toInt
      val open = (strXml \ "open").text.toDoubleOption
      val close = (strXml \ "close").text.toDoubleOption
      History(secid, tradedate, numtrades, open, close)
    }

//  def parseXmlFromFile()
}
