package de.codecarving.employeeweb

import org.specs._
import model._
import snippet._
import net.liftweb.util._
import net.liftweb.common._
import specification.Contexts
import de.codecarving.fhsldap.model.User
import net.liftweb.http.{LiftRules, S, LiftSession}

import persistence.h2.{BackendPollAnswers => BPA, BackendPoll => BP }

class PollPalSpecs extends Specification with Contexts {
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

        DB.defineConnectionManager(DefaultConnectionIdentifier, vendor)

        Schemifier.schemify(true, Schemifier.infoF _, BPA, BP)

      case _ =>
    }
      SpiritPoll.findAll.foreach(_.delete_!)
      loginUser
    }
    afterExample {

      SpiritPoll.findAll.foreach(_.delete_!)
    }
    aroundExpectations(inSession(_))
  }

  "PollPal" should {
    val pollpal = new PollPal

    "create a Poll with 2 Answers" in {
      pollpal.openPoll.title.set("Poll1")

      val spa1 = SpiritPollAnswers.createRecord
        spa1.title(pollpal.openPoll.title.value)
        spa1.answer("Answer1")
        spa1.save
      val spa2 = SpiritPollAnswers.createRecord
        spa2.title(pollpal.openPoll.title.value)
        spa2.answer("Answer2")
        spa2.save

      pollpal.answerSet = pollpal.answerSet + spa1 + spa2

      pollpal.openPoll.answerCount.set(pollpal.answerSet.size)
      pollpal.openPoll.save

      SpiritPollAnswers.findAll.filter { x =>
        x.title.value == "Poll1"
      }.size mustBe 2
    }

    "create a Poll with answerCount size 2" in {
      pollpal.openPoll.title.set("Poll1")

      val spa1 = SpiritPollAnswers.createRecord
        spa1.title(pollpal.openPoll.title.value)
        spa1.answer("Answer1")
        spa1.save
      val spa2 = SpiritPollAnswers.createRecord
        spa2.title(pollpal.openPoll.title.value)
        spa2.answer("Answer2")
        spa2.save

      pollpal.answerSet = pollpal.answerSet + spa1 + spa2
      pollpal.openPoll.answerCount.set(pollpal.answerSet.size)
      pollpal.openPoll.save

      SpiritPoll.findAll.filter(_.title.value == "Poll1").head.answerCount.value mustEqual 2

    }

  }
}
