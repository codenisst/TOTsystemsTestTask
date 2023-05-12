package models

import scala.xml.Node

case class Security(var id: Long, var secid: String, var regnumber: String, var name: String,
                    var emitentTitle: String)

object Security{
  def toXml(securityList: List[Security]): Node = {
    val elems = securityList.map { security =>
      <security>
        <id>{security.id}</id>
        <secid>{security.secid}</secid>
        <regnumber>{security.regnumber}</regnumber>
        <name>{security.name}</name>
        <emitent_title>{security.emitentTitle}</emitent_title>
      </security>
    }
    val xml = <securities>
      {elems}
    </securities>
    xml
  }

  def toXml(security: Security): Node = {
    val xml =
      <security>
        <id>{security.id}</id>
        <secid>{security.secid}</secid>
        <regnumber>{security.regnumber}</regnumber>
        <name>{security.name}</name>
        <emitent_title>{security.emitentTitle}</emitent_title>
      </security>
    xml
  }

  def parseXml(strXml: Node): Security = {
    val id = (strXml \ "id").text.toLong
    val secid = (strXml \ "secid").text
    val regnumber = (strXml \ "regnumber").text
    val name = (strXml \ "name").text
    val emitentTitle = (strXml \ "emitent_title").text
    Security(id, secid, regnumber, name, emitentTitle)
  }
}
