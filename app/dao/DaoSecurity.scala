package dao

import models.Security
import slick.jdbc.SQLiteProfile.api._

import javax.inject.Singleton
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Await, Future}
import scala.concurrent.duration.Duration

@Singleton
class DaoSecurity extends Dao {

  def saveAll(listSecurities: List[Security]): Unit = {
    val existingSec = Await.result(connection.run(securityTable.map(_.secid).result).map(_.toList), Duration.Inf)
    val validData = listSecurities.filter(d => !existingSec.contains(d.secid))
    connection.run(securityTable ++= validData)
  }

  def save(security: Security): Future[Boolean] = {
    executionAndChecked(securityTable += security)
  }

  def update(secid: String, security: Security): Future[Boolean] = {
    executionAndChecked(securityTable.filter(_.secid === secid).update(security))
  }

  def get(secid: String): Future[List[Security]] = {
    connection.run(securityTable.filter(_.secid === secid).result).map(vector => vector.toList)
  }

  def getAll(): Future[List[Security]] = {
    connection.run(securityTable.result).map(vector => vector.toList)
  }

  def delete(secid: String): Future[Boolean] = {
    executionAndChecked(securityTable.filter(_.secid === secid).delete)
  }
}
