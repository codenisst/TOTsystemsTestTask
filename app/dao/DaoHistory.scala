package dao

import models.History
import slick.jdbc.SQLiteProfile.api._

import javax.inject.Singleton
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

@Singleton
class DaoHistory extends Dao {

  def saveAll(listHistories: List[History]): Unit = {
    val existingSec = Await.result(connection.run(securityTable.map(_.secid).result).map(_.toList), Duration.Inf)
    val existingHis = Await.result(connection.run(historyTable.map(_.secid).result).map(_.toList), Duration.Inf)
    val validData = listHistories.filter(d => existingSec.contains(d.secid) && !existingHis.contains(d.secid))
    connection.run(historyTable ++= validData)
  }

  def save(history: History): Future[Boolean] = {
    executionAndChecked(historyTable += history)
  }

  def update(secid: String, history: History): Future[Boolean] = {
    executionAndChecked(historyTable.filter(_.secid === secid).update(history))
  }

  def get(secid: String): Future[List[History]] = {
    connection.run(historyTable.filter(_.secid === secid).result).map(vector => vector.toList)
  }

  def getAll(): Future[List[History]] = {
    connection.run(historyTable.result).map(vector => vector.toList)
  }

  def delete(secid: String): Future[Boolean] = {
    executionAndChecked(historyTable.filter(_.secid === secid).delete)
  }
}
