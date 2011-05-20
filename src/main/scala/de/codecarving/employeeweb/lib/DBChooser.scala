package de.codecarving.employeeweb
package lib

import net.liftweb.util.Props
import net.liftweb.common.Empty
import net.liftweb.http.LiftRules
import bootstrap.liftweb.Boot

trait DBChooser[T <: Boot] {

  lazy val db = Props.get("spirit.admin.record.backentry").openOr((""))
  lazy val MONGODB = "mongodb"
  lazy val H2DB = "h2db"
  lazy val REST = "rest"

  db match {
    case MONGODB =>
      import net.liftweb.mongodb._
      MongoDB.defineDbAuth(DefaultMongoIdentifier,
        MongoAddress(MongoHost("127.0.0.1", 27017), "spirit_admin_employeeweb"),
        "spirit_admin_employeeweb",
        "spirit_admin_employeeweb")

    case H2DB =>
      import net.liftweb.mapper._
      import de.codecarving.employeeweb.persistence.h2._
      val vendor = new StandardDBVendor("org.h2.Driver",
                                        "jdbc:h2:spirit_admin_employeeweb.db;AUTO_SERVER=TRUE",
                                        Empty, Empty)

      LiftRules.unloadHooks.append(vendor.closeAllConnections_! _)

      DB.defineConnectionManager(DefaultConnectionIdentifier, vendor)

      Schemifier.schemify(true, Schemifier.infoF _,
        BackendEntry, BackendEntryCounter,
        BackendPoll, BackendPollAnswers,
        BackendTalkAllocator, BackendTalkAllocatorTalks,
        BackendEntryComments)

    case _ =>
  }
}
