package de.codecarving.employeeweb
package specs

import org.specs._
import model.records.SpiritEntry
import persistence.EntryCounter
import snippet._
import net.liftweb.mapper.BaseMetaMapper
import net.liftweb.util._
import net.liftweb.common._
import net.liftweb.mongodb._
import news.snippet.WriteNews
import specification.Contexts
import de.codecarving.fhsldap.model.User
import net.liftweb.http.{LiftRules, S, LiftSession}

import persistence.mongo.{ BackendEntry, BackendEntryCounter }

class EntrySpecs extends Specification with Contexts with SpecDBChooser {
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
      dbInit()
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
    lazy val newNews = new WriteNews
    newNews.openEntry.subject.set("NewsSpec")
    newNews.openEntry.newEntry.set(true)

    val updateNews = new WriteNews
    "create and store one Entry." in {
      newNews.openEntry.save(true)
      SpiritEntry.findAll.filter(e =>
        e.id.value == (EntryCounter.getCounter - 1)
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
      val oldNr = oldEntry.id.value + 1

      updateNews.CurrentEntry(Full(oldEntry))
      updateNews.openEntry.save(false)

      SpiritEntry.findAll.head.id.value mustEqual oldNr
    }

    "update an existing Entry without inc its number (If not Tweeting Update)." in {

      newNews.openEntry.save(true)

      val oldEntry = SpiritEntry.findAll.head
      val oldNr = oldEntry.id.value

      updateNews.CurrentEntry(Full(oldEntry))
      updateNews.openEntry.twitterBool.set(false)
      //TODO this should be already set during line 133
      updateNews.openEntry.newEntry.set(false)
      updateNews.openEntry.save(false)

      SpiritEntry.findAll.head.id.value mustEqual oldNr
    }

    "create an Entry for three semesters" in {
      newNews.openEntry.semester.setFromDirtyList(List("", "I1", "", "I3", "I5"))
      newNews.openEntry.save

      SpiritEntry.findAll.head.semester.value.size mustEqual 3
    }
  }
}
