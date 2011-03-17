package de.codecarving.employeeweb
package model

import net.liftweb.record.field._
import spiritrecord.{SpiritMetaRecord, SpiritRecord}

import net.liftweb.record.LifecycleCallbacks
import de.codecarving.fhsldap.model.User
import net.liftweb.util.Props
import persistence.h2.{BackendPoll => BP, BackendPollAnswers => BPA}
import java.text.SimpleDateFormat
import net.liftweb.common.{Loggable, Box, Full}

/**
 * The Record which will be used for the backend implementation of the persistence layer.
 */
object SpiritPoll extends SpiritPoll with SpiritMetaRecord[SpiritPoll] {

  lazy val db = Props.get("spirit.admin.record.backentry").openOr((""))
  lazy val mongodb = "mongodb"
  lazy val h2db = "h2db"

  //TODO Implement mongodb features for Concept of Proof ?!

  /**
   * Deleting the Poll by it's title.
   */
  override def delete_!(inst: SpiritPoll): Boolean = db match {
    case this.mongodb =>
      logger warn "Not Implemented yet..."
      true
    case this.h2db =>
      import net.liftweb.mapper._
      val tmp = inst.title.value
      BP.findAll(By(BP.title,tmp)).map(_.delete_!)
      BPA.findAll(By(BPA.title,tmp)) map {_.delete_!}
      true
    case _=>
      println("not implemented yet")
      false
  }

  /**
   * Returning a List of all Polls.
   */
  override def findAll: List[SpiritPoll] = db match {
    case this.mongodb =>
      logger warn "Not Implemented yet..."
      Nil
    case this.h2db =>
      lazy val bp = BP.findAll
      bp map { b =>
        lazy val sp = SpiritPoll.createRecord
        sp.title.set(b.title)
        sp.answerCount.set(b.answerCount)
        sp.expires.set(b.expires)
        sp.user.set(b.user)
        sp.displayName(b.displayName)
        sp
      }
    case _ =>
      println("not implemented yet")
      Nil
  }

  /**
   * Saving the Poll.
   */
  override def save(inst: SpiritPoll): Boolean = db match {
    case this.mongodb =>
      logger warn "Not Implemented yet..."
      true
    case this.h2db =>
      foreachCallback(inst, _.beforeSave)
      val in = inst.asInstanceOf[SpiritPoll]
      lazy val bp = BP.create
      bp.title.set(in.title.value)
      bp.answerCount.set(in.answerCount.value)
      bp.expires.set(in.expires.value)
      bp.user.set(in.user.value)
      bp.displayName.set(in.displayName.value)
      bp.save
      true
    case _ =>
      println("not implemented")
      false
   }
}

class SpiritPoll extends SpiritRecord[SpiritPoll] with SpiritHelpers with Loggable {
  def meta = SpiritPoll

  object user extends StringField(this, User.currentUserId.openOr("default"))
  object displayName extends StringField(this, User.ldapAttributes.displayName.openOr("mr. default"))
  object title extends StringField(this,100)
  object answerCount extends IntField(this, 0)
  object expires extends StringField(this, ((new SimpleDateFormat("dd.MM.yyyy")).format(new java.util.Date)))
    with LifecycleCallbacks {

      override def beforeSave() {
        super.beforeSave
        this.setFromAny(dateValidator(this.value))
      }
    }

}
