//package dao
//
//import models.Security
//
//import javax.inject.Singleton
//import scala.concurrent.ExecutionContext.Implicits.global
//import scala.concurrent.Future
//
//@Singleton
//class DaoInMemorySecurity() {
//
//  private var securities: List[Security] = List()
//
//  def saveAll(listSecurities: List[Security]): Boolean = {
//    var flag: Boolean = false
//    listSecurities.foreach(security =>
//      if (!securities.exists(_.secid == security.secid)) {
//        securities = security :: securities
//        flag = true
//      }
//    )
//    flag
//  }
//
//  def save(security: Security): Future[Boolean] = {
//    if (!securities.exists(_.secid == security.secid) && !securities.exists(_.id == security.id)) {
//      securities = security :: securities
//      return Future {
//        true
//      }
//    }
//    Future {
//      false
//    }
//  }
//
//  def update(secid: String, security: Security, daoInMemoryHistory: DaoInMemoryHistory): Future[Boolean] = {
//    if (securities.exists(_.secid == secid)) {
//      val index = securities.filter(s => s.secid.equals(secid))
//        .map(s => securities.indexOf(s)).head
//      securities = securities.updated(index, security)
//      daoInMemoryHistory.updateSecid(secid, security.secid)
//      return Future {
//        true
//      }
//    }
//    Future {
//      false
//    }
//  }
//
//  def get(secid: String): Future[Option[Security]] = {
//    Future {
//      securities.find(_.secid == secid)
//    }
//  }
//
//  def getAll(): Future[List[Security]] = {
//    Future {
//      securities
//    }
//  }
//
//  def delete(secid: String, daoInMemoryHistory: DaoInMemoryHistory): Future[Boolean] = {
//    if (securities.exists(_.secid == secid)) {
//      securities = securities.filterNot(_.secid == secid)
//      daoInMemoryHistory.delete(secid)
//      return Future {
//        true
//      }
//    }
//    Future {
//      false
//    }
//  }
//
//  def getSecuritiesFromDb(): List[Security] = {
//    securities
//  }
//}
