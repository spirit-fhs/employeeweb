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

      logger info "Created Dummy Data...."
    }
  }
}
