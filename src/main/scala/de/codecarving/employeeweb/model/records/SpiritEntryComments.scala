package de.codecarving.employeeweb
package model
package records

import net.liftweb.record.field._
import spiritrecord.{SpiritMetaRecord, SpiritRecord}
import net.liftweb.util.Props
import net.liftweb.common.{Loggable, Box, Full}
import persistence.mongo.{BackendEntry, BackendEntryComments => BEC}
import persistence.h2.{BackendEntry, BackendEntryComments => h2BEC}
import de.codecarving.fhsldap.model.User
import java.text.SimpleDateFormat
import java.util.Date

/**
 * The Record which will be used for the backend implementation of the persistence layer.
 */
object SpiritEntryComments extends SpiritEntryComments with SpiritMetaRecord[SpiritEntryComments] {

  //TODO Implement mongodb features for Concept of Proof ?!

  /**
   * SpiritEntryComments shall not be deleted from here.
   */
  override def delete_!(inst: SpiritEntryComments): Boolean = db match {
    case this.mongodb =>
      BEC.findAll("_id_", inst.id.value).map(_.delete_!)
      true
    case this.h2db =>
      import net.liftweb.mapper._
      h2BEC.findAll(By(h2BEC.id,inst.id.value)).map(_.delete_!)
      true
    case _=>
      println("not implemented yet")
      false
  }

  /**
   * Returning a List of all Comments.
   */
  override def findAll: List[SpiritEntryComments] = db match {
    case this.mongodb =>
      lazy val bec = BEC.findAll
      bec map { b =>
        lazy val sec = SpiritEntryComments.createRecord
        sec.user.set(b.user.value)
        sec.id.set(b._id_.value)
        sec.comment.set(b.comment.value)
        sec
      }
    case this.h2db =>
      lazy val bec = h2BEC.findAll
      bec map { b =>
        lazy val sec = SpiritEntryComments.createRecord
        sec.user.set(b.user)
        sec.id.set(b._id_)
        sec.comment.set(b.comment)
        sec
      }
    case _ =>
      println("not implemented yet")
      Nil
  }

  /**
   * Saving SpiritEntryComments.
   */
  override def save(inst: SpiritEntryComments): Boolean = db match {
    case this.mongodb =>
      foreachCallback(inst, _.beforeSave)
      val in = inst.asInstanceOf[SpiritEntryComments]
      lazy val be = BEC.createRecord
      be.user.set(in.user.value)
      be._id_.set(in.id.value)
      be.crdate.set(in.crdate.value)
      be.comment.set(in.comment.value)
      be.save
      true
    case this.h2db =>
      foreachCallback(inst, _.beforeSave)
      val in = inst.asInstanceOf[SpiritEntryComments]
      lazy val be = h2BEC.create
      be.user.set(in.user.value)
      be._id_.set(in.id.value)
      be.crdate.set(in.crdate.value)
      be.comment.set(in.comment.value)
      be.save
      true
    case _ =>
      println("not implemented")
      false
   }

  /**
   * Updating shall not happen.
   */
  override def update(inst: SpiritEntryComments): Boolean = db match {
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

class SpiritEntryComments extends SpiritRecord[SpiritEntryComments] with Loggable {
  def meta = SpiritEntryComments

  object id extends IntField(this)
  object entryId extends IntField(this)
  object user extends StringField(this, User.currentUserId.openOr(""))
  object comment extends TextareaField(this, 1000){

    override def textareaRows  = 6
    override def textareaCols = 40
  }
  object crdate extends StringField(this, ((new SimpleDateFormat("dd.MM.yyyy")).format(new Date)))

}
