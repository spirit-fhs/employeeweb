package de.codecarving.employeeweb
package specs

import net.liftweb.util.Props
import net.liftweb.common.Empty

import persistence.h2.{BackendTalkAllocator => BTA, BackendTalkAllocatorTalks => BTAT }
import persistence.h2.{BackendEntry => h2BE, BackendEntryCounter => h2BEC }
import persistence.h2.{BackendPollAnswers => BPA, BackendPoll => BP, BackendEntryComments }

trait SpecDBChooser {

  def dbInit() = {

    // Getting the Persistence layer up an running
    lazy val db = Props.get("spirit.admin.record.backentry").openOr((""))
    lazy val MONGODB = "mongodb"
    lazy val H2DB = "h2db"

    db match {
      case MONGODB =>
        import net.liftweb.mongodb._

        MongoDB.defineDbAuth(DefaultMongoIdentifier,
          MongoAddress(MongoHost("127.0.0.1", 27017), "spirit_admin_test"),
          "spirit_admin_test",
          "spirit_admin_test")

      case H2DB =>
        import net.liftweb.mapper._

        val vendor =
              new StandardDBVendor(
                "org.h2.Driver",
                "jdbc:h2:spirit_admin_test.db;AUTO_SERVER=TRUE",
                Empty, Empty)

        DB.defineConnectionManager(DefaultConnectionIdentifier, vendor)

        Schemifier.schemify(true, Schemifier.infoF _, BTA, BTAT, h2BE, h2BEC, BPA, BP, BackendEntryComments)

      case _ =>
    }
  }
}
