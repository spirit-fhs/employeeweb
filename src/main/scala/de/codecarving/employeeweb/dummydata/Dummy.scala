package de.codecarving.employeeweb.model
package dummydata

import net.liftweb.common.Loggable
import records.{SpiritTalkAllocator, SpiritTalkAllocatorTalks, SpiritPollAnswers, SpiritPoll}

object Dummy extends Loggable {

  /**
   * Creating some Dummy Poll Data to see nice Chart!
   * Only Created if no Polls exist!
   * Log in as "default" to see the Dummy Poll!
   */
  def createDummyPolls() {

    if(SpiritPoll.findAll.isEmpty) {

      logger info "Creating Dummy Polls..."

      val firstPoll = SpiritPoll.createRecord
      firstPoll.title.set("Dummy Poll Nr. 1")
      firstPoll.answerCount.set(5)

      for(i <- 2 to 7){
        val spa = SpiritPollAnswers.createRecord
        spa.title.set(firstPoll.title.value)
        spa.answer.set("Answer Nr." + i)
        spa.votes(i * 9)
        spa.save
      }

      firstPoll.save

      val secondPoll = SpiritPoll.createRecord
      secondPoll.title.set("Dummy Poll Nr. 2")

      for(i <- 1 to 9){
        val spa = SpiritPollAnswers.createRecord
        spa.title.set(secondPoll.title.value)
        spa.answer.set("Answer Nr." + i)
        spa.votes(i * 11)
        spa.save
      }

      secondPoll.answerCount.set(10)
      secondPoll.save

      logger info "Created Dummy Polls...."
    }
  }

  def createDummyTalkAllocator() {

    if(SpiritTalkAllocator.findAll.isEmpty) {

      logger info "Creating Dummy TalkAllocator..."

      val sta = SpiritTalkAllocator.createRecord
      sta.title.set("TalkAllocator Nr. 1")
      sta.description("TalkAllocator Nr. 1 - Description")

      for(i <- 1 to 5){
        val stat = SpiritTalkAllocatorTalks.createRecord
        stat.allocatorTitle.set(sta.title.value)
        stat.talkTitle.set("Talk Nr." + i)
        stat.description.set("Description Nr. " + i)
        stat.save
      }

      for(i <- 6 to 10){
        val stat = SpiritTalkAllocatorTalks.createRecord
        stat.allocatorTitle.set(sta.title.value)
        stat.talkTitle.set("Talk Nr." + i)
        stat.description.set("Description Nr. " + i)
        stat.speaker.set("Speaker Nr. " + i)
        stat.assigned.set(true)
        stat.save
      }

      sta.save

      val sta2 = SpiritTalkAllocator.createRecord
      sta2.title.set("TalkAllocator Nr. 2")
      sta2.description("TalkAllocator Nr. 2 - Description")
      sta2.released.set(true)

      for(i <- 1 to 5){
        val stat = SpiritTalkAllocatorTalks.createRecord
        stat.allocatorTitle.set(sta2.title.value)
        stat.talkTitle.set("Talk Nr." + i)
        stat.description.set("Description Nr. " + i)
        stat.save
      }

      for(i <- 6 to 10){
        val stat = SpiritTalkAllocatorTalks.createRecord
        stat.allocatorTitle.set(sta2.title.value)
        stat.talkTitle.set("Talk Nr." + i)
        stat.description.set("Description Nr. " + i)
        stat.speaker.set("Speaker Nr. " + i)
        stat.assigned.set(true)
        stat.save
      }

      sta2.save

      logger info "Created Dummy TalkAllocator...."
    }
  }
}
