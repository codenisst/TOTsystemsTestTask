package dao.repositories

import models.History
import slick.lifted.{ProvenShape, Tag}
import slick.jdbc.SQLiteProfile.api._

class HistoryRepo(tag: Tag) extends Table[History](tag, "history"){

  def secid: Rep[String] = column[String]("secid")

  def tradedate: Rep[String] = column[String]("tradedate")

  def numtrades: Rep[Int] = column[Int]("numtrades")

  def open: Rep[Double] = column[Double]("open")

  def close: Rep[Double] = column[Double]("close")

  override def * : ProvenShape[History] = (secid, tradedate, numtrades, open, close).mapTo[History]
}
