package models

import scala.xml.Node

case class History(secid: String, tradedate: String, numtrades: Int,
                   open: Double, close: Double) extends Serializable

object History {
  def toXml(historyList: List[History]): Node = {
    val elems = historyList.map{history =>
      <history>
        <secid>{history.secid}</secid>
        <tradedate>{history.tradedate}</tradedate>
        <numtrades>{history.numtrades}</numtrades>
        <open>{history.open}</open>
        <close>{history.close}</close>
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
        <open>{history.open}</open>
        <close>{history.close}</close>
      </history>
    xml
  }

  def parseXml(strXml: Node): History = {
    val secid = (strXml \ "secid").text
    val tradedate = (strXml \ "tradedate").text
    val numtrades = (strXml \ "numtrades").text.toInt
    val open = if ((strXml \ "open").text.nonEmpty) (strXml \ "open").text.toDouble else 0.0
    val close = if ((strXml \ "close").text.nonEmpty) (strXml \ "close").text.toDouble else 0.0
    History(secid, tradedate, numtrades, open, close)
  }
}
