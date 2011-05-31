package de.codecarving.employeeweb
package model
package records

import net.liftweb.record.field._
import spiritrecord.{SpiritMetaRecord, SpiritRecord}

import net.liftweb.common.{Loggable, Box, Full}

/**
 * The Record which will be used for the backend implementation of the persistence layer.
 */
object SpiritPollAnswers extends SpiritPollAnswers with SpiritMetaRecord[SpiritPollAnswers] {

  override def save(inst: SpiritPollAnswers): Boolean = methods.save(inst)
  override def update(inst: SpiritPollAnswers): Boolean = methods.update(inst)
  override def delete_!(inst: SpiritPollAnswers): Boolean = methods.delete_!(inst)
  override def findAll(): List[SpiritPollAnswers] = methods.findAll()
}

class SpiritPollAnswers extends SpiritRecord[SpiritPollAnswers] with Loggable {
  def meta = SpiritPollAnswers

  object title extends StringField(this, 100)
  object answer extends StringField(this, 100)
  object votes extends IntField(this, 0)

}
