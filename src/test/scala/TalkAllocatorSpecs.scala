package de.codecarving.employeeweb
package specs

import org.specs._
import model.records.{ SpiritTalkAllocator, SpiritTalkAllocatorTalks }
import net.liftweb.util._
import net.liftweb.common._
import specification.Contexts
import de.codecarving.fhsldap.model.User
import net.liftweb.http.{LiftRules, S, LiftSession}
import de.codecarving.employeeweb.snippet.talkallocator.snippet.NewTalkAllocator

class TalkAllocatorSpecs extends Specification with Contexts with SpecDBChooser {

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
      SpiritTalkAllocator.findAll.foreach(_.delete_!)
      loginUser
    }
    afterExample {

      SpiritTalkAllocator.findAll.foreach(_.delete_!)
    }
    aroundExpectations(inSession(_))
  }

  "TalkAllocator" should {
    val talkalloc = new NewTalkAllocator
    val talkalloc2 = new NewTalkAllocator

    "create a TalkAllocator with 15 Talks" in {
      talkalloc.newTalkAlloc.title.set("TalkAlloc 1")

      for(i <- 1 to 15) {
        val tat = SpiritTalkAllocatorTalks.createRecord
        tat.talkTitle.set("Talk Nr. " + i)
        tat.allocatorTitle.set(talkalloc.newTalkAlloc.title.value)
        tat.save
      }

      talkalloc.newTalkAlloc.save

      SpiritTalkAllocatorTalks.findAll.filter(
        _.allocatorTitle.value == "TalkAlloc 1"
      ).size mustEqual 15
    }

    "create two TalkAllocators and delete one of them with its Talks" in {
      talkalloc.newTalkAlloc.title.set("TalkAlloc 1")

      for(i <- 1 to 4) {
        println(i)
        val tat = SpiritTalkAllocatorTalks.createRecord
        tat.talkTitle.set("Talk Nr. " + i)
        tat.allocatorTitle.set(talkalloc.newTalkAlloc.title.value)
        tat.save
      }

      talkalloc.newTalkAlloc.save

      talkalloc2.newTalkAlloc.title.set("TalkAlloc 2")

      for(i <- 1 to 4) {
        println(i)
        val tat = SpiritTalkAllocatorTalks.createRecord
        tat.talkTitle.set("Talk Nr. " + i)
        tat.allocatorTitle.set(talkalloc2.newTalkAlloc.title.value)
        tat.save
      }

      talkalloc2.newTalkAlloc.save

      SpiritTalkAllocator.findAll.filter(_.title.value == "TalkAlloc 1").map(_.delete_!)

      val sta = SpiritTalkAllocator.findAll.filter(_.title.value == "TalkAlloc 2").size == 1
      val stat = SpiritTalkAllocatorTalks.findAll.filter(
        _.allocatorTitle.value == "TalkAlloc 2").size == 4
      sta mustEqual stat
    }

    "create a TalkAllocator which is released to the Students" in {
      talkalloc.newTalkAlloc.title.set("TalkAlloc 1")
      talkalloc.newTalkAlloc.released.set(true)

      for(i <- 1 to 15) {
        val tat = SpiritTalkAllocatorTalks.createRecord
        tat.talkTitle.set("Talk Nr. " + i)
        tat.allocatorTitle.set(talkalloc.newTalkAlloc.title.value)
        tat.save
      }

      talkalloc.newTalkAlloc.save

      SpiritTalkAllocator.findAll.filter(
        _.title.value == "TalkAlloc 1"
      ).head.released.value mustEqual true

    }

    "create a TalkAllocator which is NOT released to the Students" in {
      talkalloc.newTalkAlloc.title.set("TalkAlloc 1")
      talkalloc.newTalkAlloc.released.set(false)

      for(i <- 1 to 15) {
        val tat = SpiritTalkAllocatorTalks.createRecord
        tat.talkTitle.set("Talk Nr. " + i)
        tat.allocatorTitle.set(talkalloc.newTalkAlloc.title.value)
        tat.save
      }

      talkalloc.newTalkAlloc.save

      SpiritTalkAllocator.findAll.filter(
        _.title.value == "TalkAlloc 1"
      ).head.released.value mustEqual false
    }

    "update a Talk" in {
      talkalloc.newTalkAlloc.title.set("TalkAlloc 1")

      val tat = SpiritTalkAllocatorTalks.createRecord
      tat.talkTitle.set("talk")
      tat.description.set("New Desciption")
      tat.allocatorTitle.set(talkalloc.newTalkAlloc.title.value)

      tat.save
      talkalloc.newTalkAlloc.save

      val talk2update = SpiritTalkAllocatorTalks.findAll.filter(
        _.allocatorTitle.value == "TalkAlloc 1").filter(
        _.talkTitle.value == "talk").head

      talk2update.description.set("Updated Description")
      talk2update.update

      val talk2check = SpiritTalkAllocatorTalks.findAll.filter(
        _.allocatorTitle.value == "TalkAlloc 1").filter(
        _.talkTitle.value == "talk").head

      talk2check.description.value mustEqual "Updated Description"

    }
  }
}
