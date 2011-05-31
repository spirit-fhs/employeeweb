package de.codecarving.employeeweb
package model
package records

import net.liftweb.record.field._
import spiritrecord.{SpiritMetaRecord, SpiritRecord}

import net.liftweb.common.{Empty, Loggable, Box, Full}
import spiritrecord.fields.SpiritListField

object SpiritTalkAllocatorTalks extends SpiritTalkAllocatorTalks with SpiritMetaRecord[SpiritTalkAllocatorTalks] {

  override def save(inst: SpiritTalkAllocatorTalks): Boolean = methods.save(inst)
  override def update(inst: SpiritTalkAllocatorTalks): Boolean = methods.update(inst)
  override def delete_!(inst: SpiritTalkAllocatorTalks): Boolean = methods.delete_!(inst)
  override def findAll(): List[SpiritTalkAllocatorTalks] = methods.findAll()

}

class SpiritTalkAllocatorTalks extends SpiritRecord[SpiritTalkAllocatorTalks] with SpiritHelpers with Loggable {
  def meta = SpiritTalkAllocatorTalks

  object allocatorTitle extends StringField(this,100)
  object talkTitle extends StringField(this,100)
  object speakers extends SpiritListField[SpiritTalkAllocatorTalks, String](this)
  object description extends TextareaField(this, 100000) {

    override def textareaRows  = 12
    override def textareaCols = 80
  }
  object assigned extends BooleanField(this, false)
}
