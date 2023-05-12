package dao

import com.google.inject.Singleton
import models.Summary

import javax.inject._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@Singleton
class DaoInMemorySummary @Inject()(val daoHistory: DaoInMemoryHistory, val daoSecurity: DaoInMemorySecurity) {

  def getAll(): Future[List[Summary]] = {
    Future {
      var resultList: List[Summary] = List.empty[Summary]

      daoSecurity.getSecuritiesFromDb().foreach(security => {
        daoHistory.getHistoryFromDb().foreach(history => {
          if (security.secid.equals(history.secid)) {
            resultList = Summary(security.secid, security.regnumber, security.name, security.emitentTitle,
              history.tradedate, history.numtrades, history.open, history.close) :: resultList
          }
        })
      })

      resultList
    }
  }
}
