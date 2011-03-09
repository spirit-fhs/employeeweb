package de.codecarving.employeeweb

import org.specs._
import model._
import persistence.EntryCounter
import snippet._
import net.liftweb.mapper.BaseMetaMapper
import net.liftweb.util._
import net.liftweb.common._
import net.liftweb.mongodb._
import specification.Contexts
import de.codecarving.fhsldap.model.User
import net.liftweb.http.{LiftRules, S, LiftSession}

import persistence.mongo.{ BackendEntry, BackendEntryCounter }
import persistence.h2.{BackendEntry => h2BE, BackendEntryCounter => h2BEC }

class EntrySpecs extends Specification with Contexts {
  //TODO clean this mess up!

  /* This is from the Lift Wiki! https://www.assembla.com/wiki/show/liftweb/Unit_Testing_Snippets_With_A_Logged_In_User! THANKS!*/
  val session = new LiftSession("", StringHelpers.randomString(20), Empty)

  def inSession(a: => Any) = {
    S.initIfUninitted(session) { a }
  }

  def loginUser = inSession {
    User.logUserIdIn("SpecUser")
  }

  /* Defining what we'll do before and after an example.*/
  new SpecContext {
    beforeExample {
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

        DB.defineConnectionManager(
              DefaultConnectionIdentifier, vendor)

        Schemifier.schemify(true, Schemifier.infoF _, h2BE, h2BEC)

      case _ =>
    }
      EntryCounter.setCounter(0)
      SpiritEntry.findAll.foreach(_.delete_!)
      loginUser
    }
    afterExample {
      EntryCounter.setCounter(0)
      SpiritEntry.findAll.foreach(_.delete_!)
    }
    aroundExpectations(inSession(_))
  }

  "WriteNews" should {
    val newNews = new WriteNews
    newNews.openEntry.subject.set("NewsSpec")
    newNews.openEntry.newEntry.set(true)

    val updateNews = new WriteNews
    "create and store one Entry." in {
      newNews.openEntry.save(true)
      SpiritEntry.findAll.filter(e =>
        e.nr.value == (EntryCounter.getCounter - 1)
      ).size mustEqual 1
    }

    "create and store ten Entrys" in {
      for(i <- 1 to 10) {
        new WriteNews().openEntry.save(true)
      }
      SpiritEntry.findAll.size mustEqual 10
    }


    "read all entries for the SpecsUser." in {

      SpiritEntry.findAll.filter(e =>
        e.user.value == User.currentUserId.open_!
      ).forall(_.user.value mustEqual User.currentUserId.openOr("")) mustBe true
    }

    "delete all entries from the SpecUser." in {
      SpiritEntry.findAll.filter(e =>
        e.user.value == User.currentUserId.openOr("")
      ).foreach(e => e.delete_!)

      SpiritEntry.findAll.filter(e =>
        e.user.value == User.currentUserId.openOr("")
      ).size mustEqual 0
    }

    "update an existing Entry with inc its number (Needed for Twitter)." in {
      newNews.openEntry.save(true)

      val oldEntry = SpiritEntry.findAll.head
      val oldNr = oldEntry.nr.value + 1

      updateNews.CurrentEntry(Full(oldEntry))
      updateNews.openEntry.save(false)

      SpiritEntry.findAll.head.nr.value mustEqual oldNr
    }

    "update an existing Entry without inc its number (If not Tweeting Update)." in {
      newNews.openEntry.save(true)

      val oldEntry = SpiritEntry.findAll.head
      val oldNr = oldEntry.nr.value

      updateNews.CurrentEntry(Full(oldEntry))
      updateNews.openEntry.twitterBool.set(false)
      updateNews.openEntry.save(false)

      SpiritEntry.findAll.head.nr.value mustEqual oldNr
    }
  }
}
