package de.codecarving.employeeweb
package model
package records

import net.liftweb.record.field._
import spiritrecord.{SpiritMetaRecord, SpiritRecord}

import net.liftweb.record.LifecycleCallbacks
import net.liftweb.util.Props
import persistence.mongo.{BackendEntryCounter => BEC, BackendEntry => BE}
import persistence.mongo.{BackendEntry, BackendPoll => mongoBP, BackendPollAnswers => mongoBPA}
import persistence.h2.{BackendPollAnswers => BPA}
import net.liftweb.common.{Loggable, Box, Full}

/**
 * The Record which will be used for the backend implementation of the persistence layer.
 */
object SpiritPollAnswers extends SpiritPollAnswers with SpiritMetaRecord[SpiritPollAnswers] {

  /**
   * Deleting the Poll by it's title.
   */
  override def delete_!(inst: SpiritPollAnswers): Boolean = db match {
    case this.mongodb =>
      mongoBPA.findAll("title", inst.title.value).map(_.delete_!)
      true
    case this.h2db =>
      import net.liftweb.mapper._
      BPA.findAll(By(BPA.title,inst.title.value)).map(_.delete_!)
      true
    case _=>
      println("not implemented yet")
      false
  }

  /**
   * Returning a List of all PollAnswers.
   */
  override def findAll: List[SpiritPollAnswers] = db match {
    case this.mongodb =>
      lazy val bpa = mongoBPA.findAll
      bpa map { b =>
        lazy val spa = SpiritPollAnswers.createRecord
        spa.title.set(b.title.value)
        spa.answer.set(b.answer.value)
        spa.votes.set(b.votes.value)
        spa
      }
    case this.h2db =>
      lazy val bpa = BPA.findAll
      bpa map { b =>
        lazy val spa = SpiritPollAnswers.createRecord
        spa.title.set(b.title)
        spa.answer.set(b.answer)
        spa.votes.set(b.votes)
        spa
      }
    case _ =>
      println("not implemented yet")
      Nil
  }

  /**
   * Saving the PollAnswer.
   */
  override def save(inst: SpiritPollAnswers): Boolean = db match {
    case this.mongodb =>
      foreachCallback(inst, _.beforeSave)
      val in = inst.asInstanceOf[SpiritPollAnswers]
      lazy val bpa = mongoBPA.createRecord
      bpa.title.set(in.title.value)
      bpa.answer.set(in.answer.value)
      bpa.votes.set(in.votes.value)
      bpa.save
      true
    case this.h2db =>
      foreachCallback(inst, _.beforeSave)
      val in = inst.asInstanceOf[SpiritPollAnswers]
      lazy val bpa = BPA.create
      bpa.title.set(in.title.value)
      bpa.answer.set(in.answer.value)
      bpa.votes.set(in.votes.value)
      bpa.save
      true
    case _ =>
      println("not implemented")
      false
   }

  override def update(inst: SpiritPollAnswers): Boolean = db match {
    case this.mongodb =>
      logger warn "Not Implemented yet..."
      false
    case this.h2db =>
      logger warn "Not Implemented yet..."
      false
    case _ =>
      logger warn "Not Implemented yet..."
      false
  }
}

class SpiritPollAnswers extends SpiritRecord[SpiritPollAnswers] with Loggable {
  def meta = SpiritPollAnswers

  object title extends StringField(this, 100)
  object answer extends StringField(this, 100)
  object votes extends IntField(this, 0)

}
