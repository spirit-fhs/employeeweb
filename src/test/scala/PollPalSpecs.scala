package de.codecarving.employeeweb
package specs

import org.specs._
import model.records.{ SpiritPollAnswers, SpiritPoll }
import snippet._
import net.liftweb.util._
import net.liftweb.common._
import pollpal.snippet.PollPal
import specification.Contexts
import de.codecarving.fhsldap.model.User
import net.liftweb.http.{LiftRules, S, LiftSession}

class PollPalSpecs extends Specification with Contexts with SpecDBChooser {

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
      dbInit()
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
    val pollpal2 = new PollPal

    "create a Poll with two Answers" in {
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

    "create one Poll with answerCount size 2" in {
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

    "create two Polls and delete one of them with its Answers" in {
      pollpal.openPoll.title.set("Poll 1")

      for(i <- 0 to 4) {
        val spa = SpiritPollAnswers.createRecord
          spa.title(pollpal.openPoll.title.value)
          spa.answer("Answer" + i)
          spa.save
      }

      pollpal.openPoll.save

      pollpal2.openPoll.title.set("Poll 2")

      for(i <- 0 to 4) {
        val spa = SpiritPollAnswers.createRecord
          spa.title(pollpal2.openPoll.title.value)
          spa.answer("Answer" + i)
          spa.save
      }

      pollpal2.openPoll.save

      SpiritPoll.findAll.filter(_.title.value == "Poll 1").map(_.delete_!)

      val pollSize = SpiritPoll.findAll.size == 1
      val answerSize = SpiritPollAnswers.findAll.filter(_.title.value == "Poll 1").size == 0

      pollSize mustEqual answerSize
    }

    "create two Polls with a count of 14 Answers" in {
      pollpal.openPoll.title.set("Poll 1")
      for(i <- 0 to 6) {
        val spa = SpiritPollAnswers.createRecord
          spa.title(pollpal.openPoll.title.value)
          spa.answer("Answer" + i)
          spa.save
      }
      pollpal.openPoll.save

      pollpal.openPoll.title.set("Poll 2")
      for(i <- 0 to 6) {
        val spa = SpiritPollAnswers.createRecord
          spa.title(pollpal.openPoll.title.value)
          spa.answer("Answer" + i)
          spa.save
      }
      pollpal.openPoll.save


      val poll1AnswerSize = SpiritPollAnswers.findAll.filter(_.title.value == "Poll 1").size
      val poll2AnswerSize = SpiritPollAnswers.findAll.filter(_.title.value == "Poll 2").size
      poll1AnswerSize mustEqual poll2AnswerSize
    }

  }
}
