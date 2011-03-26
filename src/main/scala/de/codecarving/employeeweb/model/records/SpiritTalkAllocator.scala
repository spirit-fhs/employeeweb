package de.codecarving.employeeweb
package model
package records

import net.liftweb.record.field._
import spiritrecord.{SpiritMetaRecord, SpiritRecord}

import net.liftweb.record.LifecycleCallbacks
import de.codecarving.fhsldap.model.User
import net.liftweb.util.Props
import java.text.SimpleDateFormat
import net.liftweb.common.{Loggable, Box, Full}
import xml.NodeSeq
import net.liftweb.mapper.By
import persistence.h2.{ BackendTalkAllocator => BTA, BackendTalkAllocatorTalks => BTAT }

object SpiritTalkAllocator extends SpiritTalkAllocator with SpiritMetaRecord[SpiritTalkAllocator] {

  //TODO Implement mongodb features for Concept of Proof ?!

  override def delete_!(inst: SpiritTalkAllocator): Boolean = db match {
    case this.mongodb =>
      logger warn "Not Implemented yet..."
      true
    case this.h2db =>
      BTA.findAll(By(BTA.title,inst.title.value)).map(_.delete_!)
      BTAT.findAll(By(BTAT.allocatorTitle,inst.title.value)).map(_.delete_!)
      true
    case _=>
      logger warn "Not Implemented yet..."
      false
  }

  override def findAll: List[SpiritTalkAllocator] = db match {
    case this.mongodb =>
      logger warn "Not Implemented yet..."
      Nil
    case this.h2db =>
      lazy val bta = BTA.findAll
      bta map { b =>
        lazy val sta = SpiritTalkAllocator.createRecord
        sta.user.set(b.user)
        sta.displayName.set(b.displayName)
        sta.title.set(b.title)
        sta.description.set(b.description)
        sta.released.set(b.released)
        sta.expires.set(b.expires)
        sta
      }
    case _ =>
      logger warn "Not Implemented yet..."
      Nil
  }

  /**
   * Saving the TalkAllocator.
   */
  override def save(inst: SpiritTalkAllocator): Boolean = db match {
    case this.mongodb =>
      logger warn "Not Implemented yet..."
      true
    case this.h2db =>
      foreachCallback(inst, _.beforeSave)
      val in = inst.asInstanceOf[SpiritTalkAllocator]
      lazy val bta = BTA.create
      bta.user.set(in.user.value)
      bta.displayName.set(in.displayName.value)
      bta.title.set(in.title.value)
      bta.description.set(in.description.value)
      bta.released.set(in.released.value)
      bta.expires.set(in.expires.value)
      bta.save
      true
      true
    case _ =>
      logger warn "Not Implemented yet..."
      false
   }

  override def update(inst: SpiritTalkAllocator): Boolean = db match {
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

class SpiritTalkAllocator extends SpiritRecord[SpiritTalkAllocator] with SpiritHelpers with Loggable {
  def meta = SpiritTalkAllocator

  object user extends StringField(this, User.currentUserId.openOr("default"))
  object displayName extends StringField(this, User.ldapAttributes.displayName.openOr("mr. default"))
  object title extends StringField(this,100)
  object description extends TextareaField(this, 100000) {

    override def textareaRows  = 12
    override def textareaCols = 80
  }
  object speakerCount extends IntField(this, 1)
  object released extends BooleanField(this, false)
  object expires extends StringField(this, ((new SimpleDateFormat("dd.MM.yyyy")).format(new java.util.Date)))
    with LifecycleCallbacks {

      override def beforeSave() {
        super.beforeSave
        this.setFromAny(dateValidator(this.value))
      }
    }

}
