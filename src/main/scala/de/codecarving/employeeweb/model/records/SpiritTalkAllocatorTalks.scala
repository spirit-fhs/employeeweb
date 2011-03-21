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
import net.liftweb.mapper.By
import persistence.h2.{ BackendTalkAllocator => BTA, BackendTalkAllocatorTalks => BTAT}

object SpiritTalkAllocatorTalks extends SpiritTalkAllocatorTalks with SpiritMetaRecord[SpiritTalkAllocatorTalks] {

  lazy val db = Props.get("spirit.admin.record.backentry").openOr((""))
  lazy val mongodb = "mongodb"
  lazy val h2db = "h2db"

  //TODO Implement mongodb features for Concept of Proof ?!

  override def delete_!(inst: SpiritTalkAllocatorTalks): Boolean = db match {
    case this.mongodb =>
      logger warn "Not Implemented yet..."
      true
    case this.h2db =>
      BTAT.findAll(By(BTAT.talkTitle,inst.talkTitle.value)).map(_.delete_!)
      true
    case _=>
      logger warn "Not Implemented yet..."
      false
  }

  override def findAll: List[SpiritTalkAllocatorTalks] = db match {
    case this.mongodb =>
      logger warn "Not Implemented yet..."
      Nil
    case this.h2db =>
      lazy val btat = BTAT.findAll
      btat map { b =>
        lazy val stat = SpiritTalkAllocatorTalks.createRecord
        stat.talkTitle.set(b.talkTitle)
        stat.allocatorTitle.set(b.allocatorTitle)
        stat.description.set(b.description)
        stat.assigned.set(b.assigned)
        stat.speakers.set(b.speakers)
        stat
      }
    case _ =>
      println("not implemented yet")
      Nil
  }

  /**
   * Saving the TalkAllocatorTalks.
   */
  override def save(inst: SpiritTalkAllocatorTalks): Boolean = db match {
    case this.mongodb =>
      logger warn "Not Implemented yet..."
      true
    case this.h2db =>
      foreachCallback(inst, _.beforeSave)
      val in = inst.asInstanceOf[SpiritTalkAllocatorTalks]
      lazy val btat = BTAT.create
      btat.talkTitle.set(in.talkTitle.value)
      btat.allocatorTitle.set(in.allocatorTitle.value)
      btat.description.set(in.description.value)
      btat.assigned.set(in.assigned.value)
      btat.speakers.set(in.speakers.value)
      btat.save
      true
    case _ =>
      logger warn "Not Implemented yet..."
      false
   }

  override def update(inst: SpiritTalkAllocatorTalks): Boolean = db match {
    case this.mongodb =>
      logger warn "Not Implemented yet..."
      false
    case this.h2db =>
      foreachCallback(inst, _.beforeUpdate)
      val in = inst.asInstanceOf[SpiritTalkAllocatorTalks]
      val btat = BTAT.find(By(BTAT.talkTitle,inst.talkTitle.value)).openOr(BTAT.create)
      btat.talkTitle.set(in.talkTitle.value)
      btat.allocatorTitle.set(in.allocatorTitle.value)
      btat.description.set(in.description.value)
      btat.assigned.set(in.assigned.value)
      btat.speakers.set(in.speakers.value)
      btat.save
    case _ =>
      logger warn "Not Implemented yet..."
      false
  }
}

class SpiritTalkAllocatorTalks extends SpiritRecord[SpiritTalkAllocatorTalks] with SpiritHelpers with Loggable {
  def meta = SpiritTalkAllocatorTalks

  object allocatorTitle extends StringField(this,100)
  object talkTitle extends StringField(this,100)
  object speakers extends StringField(this,100){

    def setFromSet(in: Set[String]): Box[String] = in match {
      case set if set.nonEmpty => setFromAny(set.mkString(";"))
      case _ => genericSetFromAny("")
    }

    def valueAsSet(): Set[String] = {
      if(!this.valueBox.isEmpty)
        this.value.split(";").toSet
      else
        Set("")
    }
  }
  object description extends TextareaField(this, 100000) {

    override def textareaRows  = 12
    override def textareaCols = 80
  }
  object assigned extends BooleanField(this, false)
}
