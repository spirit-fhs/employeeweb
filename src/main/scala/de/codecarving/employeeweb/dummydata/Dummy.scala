package de.codecarving.employeeweb.model
package dummydata

import de.codecarving.employeeweb.model.{ SpiritPollAnswers, SpiritPoll }
import net.liftweb.common.Loggable

object Dummy extends Loggable {

  /**
   * Creating some Dummy Poll Data to see nice Chart!
   * Only Created if no Polls exist!
   * Log in as "default" to see the Dummy Poll!
   */
  def createDummyData() {

    if(SpiritPoll.findAll.isEmpty) {

      logger info "Creating Dummy Data..."

      val firstPoll = SpiritPoll.createRecord
      firstPoll.title.set("Dummy Poll Nr. 1")

      val firstPollAnswer1 = SpiritPollAnswers.createRecord
      val firstPollAnswer2 = SpiritPollAnswers.createRecord
      val firstPollAnswer3 = SpiritPollAnswers.createRecord
      val firstPollAnswer4 = SpiritPollAnswers.createRecord
      val firstPollAnswer5 = SpiritPollAnswers.createRecord

      firstPollAnswer1.title.set(firstPoll.title.value)
      firstPollAnswer2.title.set(firstPoll.title.value)
      firstPollAnswer3.title.set(firstPoll.title.value)
      firstPollAnswer4.title.set(firstPoll.title.value)
      firstPollAnswer5.title.set(firstPoll.title.value)

      firstPollAnswer1.answer.set("Answer Nr. 1")
      firstPollAnswer2.answer.set("Answer Nr. 2")
      firstPollAnswer3.answer.set("Answer Nr. 3")
      firstPollAnswer4.answer.set("Answer Nr. 4")
      firstPollAnswer5.answer.set("Answer Nr. 5")

      firstPollAnswer1.votes.set(10)
      firstPollAnswer2.votes.set(20)
      firstPollAnswer3.votes.set(30)
      firstPollAnswer4.votes.set(20)
      firstPollAnswer5.votes.set(110)

      firstPoll.save
      firstPollAnswer1.save
      firstPollAnswer2.save
      firstPollAnswer3.save
      firstPollAnswer4.save
      firstPollAnswer5.save

      logger info "Created Dummy Data...."
    }
  }
}
