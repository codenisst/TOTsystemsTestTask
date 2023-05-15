package models

import scala.xml.Node

case class Security(id: Int, secid: String, regnumber: String, name: String,
                    emitentTitle: String) extends Serializable {
  override def toString: String = {
    s"$id, $secid, $regnumber, $name, $emitentTitle"
  }
}

object Security {
  def parseFromXmlFile(row: Node): Security = {
    Security(
      id = (row \ "@id").text.toInt,
      secid = (row \ "@secid").text,
      regnumber = (row \ "@regnumber").text,
      name = (row \ "@name").text,
      emitentTitle = (row \ "@emitent_title").text
    )
  }

  def parseFromForm(formData: Option[Map[String, Seq[String]]]): Security = {
    Security(
      if (!formData.get("id").head.equals("") &&
        formData.get("id").head.forall(_.isDigit)) formData.get("id").head.toInt else 0,
      if (!formData.get("secid").head.equals("")) formData.get("secid").head else "test",
      formData.get("regnumber").head,
      formData.get("name").head,
      formData.get("emitentTitle").head
    )
  }
}