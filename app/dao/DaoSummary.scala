package dao

import models.Summary
import slick.jdbc.SQLiteProfile.api._

import javax.inject.Singleton
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@Singleton
class DaoSummary extends Dao {

  def getAll(): Future[List[Summary]] = {
    val query = securityTable.join(historyTable).on(_.secid === _.secid)

    connection.run(query.result).map(rows =>
      rows.map {
        case (securityRow, historyRow) =>
          Summary(
            secid = securityRow.secid,
            regnumber = securityRow.regnumber,
            name = securityRow.name,
            emitentTitle = securityRow.emitentTitle,
            tradedate = historyRow.tradedate,
            numtrades = historyRow.numtrades,
            open = historyRow.open,
            close = historyRow.close
          )
      }.toList
    )
  }
}
