package dao

import com.google.inject.Singleton
import models.{History, Security}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@Singleton
class DaoInMemoryHistory() {

  private var histories: List[History] = List()

  def saveAll(listHistories: List[History], listSecuritiesFromDB: List[Security]): Boolean = {
    var flag: Boolean = false
    listHistories.foreach(history => {
      if (!histories.exists(_.secid == history.secid) &&
        listSecuritiesFromDB.exists(_.secid == history.secid)) {
        histories = history :: histories
        flag = true
      }
    })
    flag
  }

  def save(history: History, listSecuritiesFromDB: List[Security]): Future[Boolean] = {
    if (!histories.exists(_.secid == history.secid) &&
      listSecuritiesFromDB.exists(_.secid == history.secid)) {
      histories = history :: histories
      return Future {
        true
      }
    }
    Future {
      false
    }
  }

  def update(secid: String, history: History): Future[Boolean] = {
    if (histories.exists(_.secid == secid)) {
      val index = histories.filter(h => h.secid.equals(secid))
        .map(h => histories.indexOf(h)).head
      histories = histories.updated(index, history)
      return Future {
        true
      }
    }
    Future {
      false
    }
  }

  def get(secid: String): Future[Option[History]] = {
    Future {
      histories.find(_.secid == secid)
    }
  }

  def getAll(): Future[List[History]] = {
    Future {
      histories
    }
  }

  def delete(secid: String): Future[Boolean] = {
    if (histories.exists(_.secid == secid)) {
      histories = histories.filterNot(_.secid == secid)
      return Future {
        true
      }
    }
    Future {
      false
    }
  }

  def updateSecid(oldSecid: String, newSecid: String): Unit = {
    val oldHistory = histories.find(_.secid.equals(oldSecid))
    if (oldHistory.nonEmpty) {
      val history = oldHistory.head
      val index = histories.indexOf(history)
      history.secid = newSecid
      histories = histories.updated(index, history)
    }
  }

  def getHistoryFromDb(): List[History] = {
    histories
  }
}
