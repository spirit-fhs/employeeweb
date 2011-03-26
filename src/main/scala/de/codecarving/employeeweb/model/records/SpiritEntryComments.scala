package de.codecarving.employeeweb
package model
package records

import net.liftweb.record.field._
import spiritrecord.{SpiritMetaRecord, SpiritRecord}
import net.liftweb.util.Props
import net.liftweb.common.{Loggable, Box, Full}
import persistence.h2.{BackendEntry, BackendEntryComments => BEC}

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
      logger warn "Not Implemented yet..."
      true
    case this.h2db =>
      import net.liftweb.mapper._
      BEC.findAll(By(BEC.nr,inst.nr.value)).map(_.delete_!)
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
      logger warn "Not Implemented yet..."
      Nil
    case this.h2db =>
      lazy val bec = BEC.findAll
      bec map { b =>
        lazy val sec = SpiritEntryComments.createRecord
        sec.user.set(b.user)
        sec.nr.set(b.nr)
        sec.comment.set(b.comment)
        sec
      }
    case _ =>
      println("not implemented yet")
      Nil
  }

  /**
   * SpiritEntryComments shall not be saved from here.
   */
  override def save(inst: SpiritEntryComments): Boolean = db match {
    case this.mongodb =>
      logger warn "Not Implemented yet..."
      true
    case this.h2db =>
      logger warn "Not Implemented yet..."
      true
    case _ =>
      println("not implemented")
      false
   }

  /**
   * SpiritEntryComments shall not be updated from here.
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

  object nr extends IntField(this)
  object user extends StringField(this, 100)
  object comment extends StringField(this, 1000)

}
