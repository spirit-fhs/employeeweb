package de.codecarving.employeeweb
package model
package records

import net.liftweb.record.field._
import spiritrecord.{SpiritMetaRecord, SpiritRecord}
import net.liftweb.common.{Loggable, Box, Full}
import de.codecarving.fhsldap.model.User
import java.text.SimpleDateFormat
import java.util.Date

/**
 * The Record which will be used for the backend implementation of the persistence layer.
 */
object SpiritEntryComments extends SpiritEntryComments with SpiritMetaRecord[SpiritEntryComments] {

  override def save(inst: SpiritEntryComments): Boolean = methods.save(inst)
  override def update(inst: SpiritEntryComments): Boolean = methods.update(inst)
  override def delete_!(inst: SpiritEntryComments): Boolean = methods.delete_!(inst)
  override def findAll(): List[SpiritEntryComments] = methods.findAll()

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
