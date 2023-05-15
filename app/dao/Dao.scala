package dao

import dao.repositories.{HistoryRepo, SecurityRepo}
import org.sqlite.SQLiteConfig.Pragma
import org.sqlite.{SQLiteConfig, SQLiteException}
import slick.dbio.{DBIOAction, Effect, NoStream}
import slick.jdbc.SQLiteProfile.api._
import slick.lifted.TableQuery

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

abstract class Dao {

  private val sqliConfig = new SQLiteConfig();
  sqliConfig.setPragma(Pragma.FOREIGN_KEYS, "true")
  sqliConfig.setJournalMode(SQLiteConfig.JournalMode.MEMORY)
  sqliConfig.setSynchronous(SQLiteConfig.SynchronousMode.OFF)
  protected val connection = Database.forURL("jdbc:sqlite:database.sqlite",
    driver = "org.sqlite.JDBC",
    prop = sqliConfig.toProperties)
  protected val historyTable = TableQuery[HistoryRepo]
  protected var securityTable = TableQuery[SecurityRepo]

  protected def executionAndChecked(query: DBIOAction[Int, NoStream, Effect.Write]): Future[Boolean] = {
    connection.run(query).map(value =>
      if (value != 0) true
      else false).recover {
      case _: SQLiteException => false
    }
  }
}
