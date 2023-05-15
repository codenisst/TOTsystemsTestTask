package models

import scala.util.Try
import scala.xml.Node

case class History(secid: String, tradedate: String, numtrades: Int,
                   open: Double, close: Double) extends Serializable {
  override def toString: String = {
    s"$secid, $tradedate, $numtrades, $open, $close"
  }
}

object History {
  def parseFromForm(formData: Option[Map[String, Seq[String]]]): History = {
    History(
      formData.get("secid").head,
      formData.get("tradedate").head,
      if (!formData.get("numtrades").head.equals("") &&
        formData.get("numtrades").head.forall(_.isDigit)) formData.get("numtrades").head.toInt else 0,
      if (!formData.get("open").head.equals("") &&
        Try(formData.get("open").head).isSuccess) formData.get("open").head.toDouble else 0.0,
      if (!formData.get("close").head.equals("") &&
        Try(formData.get("close").head).isSuccess) formData.get("close").head.toDouble else 0.0
    )
  }

  def parseFromXmlFile(row: Node): History = {
    History(
      secid = (row \ "@SECID").text,
      tradedate = (row \ "@TRADEDATE").text,
      numtrades = if ((row \ "@NUMTRADES").text.nonEmpty) (row \ "@NUMTRADES").text.toInt else 0,
      open = if ((row \ "@OPEN").text.nonEmpty) (row \ "@OPEN").text.toDouble else 0.0,
      close = if ((row \ "@CLOSE").text.nonEmpty) (row \ "@CLOSE").text.toDouble else 0.0
    )
  }
}
