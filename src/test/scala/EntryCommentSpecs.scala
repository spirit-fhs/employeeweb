package de.codecarving.employeeweb
package specs

import org.specs._
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
import model.records.{SpiritEntryComments, SpiritEntry}

class EntryCommentSpecs extends Specification with Contexts with SpecDBChooser {
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
        SpiritEntry.findAll.foreach(_.delete_!)
        loginUser
      }
      afterExample {
        SpiritEntry.findAll.foreach(_.delete_!)
      }
      aroundExpectations(inSession(_))
    }

  "CommentNews" should {
    lazy val newNews = new WriteNews
    newNews.openEntry.subject.set("NewsSpec")
    newNews.openEntry.newEntry.set(true)

    "create and store three comments to a news." in {
      newNews.openEntry.save(true)

      for(sec <- 1 to 3) {
        val newSec = SpiritEntryComments.createRecord
        newSec.id.set(1)
        newSec.comment("comment :" + sec)
        newSec.save
      }

      SpiritEntryComments.findAll.filter(
        _.id.value == 1
      ).size mustEqual 3
    }

    "have no comments at all." in {
      SpiritEntryComments.findAll.size mustEqual 0
    }

  }
}
