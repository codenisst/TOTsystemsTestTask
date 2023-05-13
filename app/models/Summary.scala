package models

import scala.xml.Node

case class Summary(var secid: String, var regnumber: String, var name: String, var emitentTitle: String,
                   var tradedate: String, var numtrades: Int, var open: Double, var close: Double)

object Summary {
  def toXml(summaryList: List[Summary]): Node = {
    val elems = summaryList.map { summary =>
      <summary>
        <secid>{summary.secid}</secid>
        <regnumber>{summary.regnumber}</regnumber>
        <name>{summary.name}</name>
        <emitent_title>{summary.emitentTitle}</emitent_title>
        <tradedate>{summary.tradedate}</tradedate>
        <numtrades>{summary.numtrades}</numtrades>
        <open>{summary.open}</open>
        <close>{summary.close}</close>
      </summary>
    }
    val xml = <summaries>
      {elems}
    </summaries>
    xml
  }
}
