package models

import scala.xml.Node

case class Summary(var secid: String, var regnumber: String, var name: String, var emitentTitle: String,
                   var tradedate: String, var numtrades: Int, var open: Option[Double], var close: Option[Double])

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
        <open>{if (summary.open.nonEmpty) summary.open.get else ""}</open>
        <close>{if (summary.close.nonEmpty) summary.close.get else ""}</close>
      </summary>
    }
    val xml = <summaries>
      {elems}
    </summaries>
    xml
  }
}
