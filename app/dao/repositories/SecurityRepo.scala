package dao.repositories

import models.Security
import slick.lifted.{ProvenShape, Tag}
import slick.jdbc.SQLiteProfile.api._

class SecurityRepo(tag: Tag) extends Table[Security](tag, "security"){

  def id: Rep[Int] = column[Int]("id", O.PrimaryKey)

  def secid: Rep[String] = column[String]("secid")

  def regnumber: Rep[String] = column[String]("regnumber")

  def name: Rep[String] = column[String]("name")

  def emitentTitle: Rep[String] = column[String]("emitent_title")

  override def * : ProvenShape[Security] = (id, secid, regnumber, name, emitentTitle).mapTo[Security]
}
