package de.codecarving.employeeweb
package model
package records

import net.liftweb.record.field._
import spiritrecord.{SpiritMetaRecord, SpiritRecord}

import net.liftweb.record.LifecycleCallbacks
import de.codecarving.fhsldap.model.User
import java.text.SimpleDateFormat
import net.liftweb.common.{Loggable, Box, Full}

/**
 * The Record which will be used for the backend implementation of the persistence layer.
 */
object SpiritPoll extends SpiritPoll with SpiritMetaRecord[SpiritPoll] {

  override def save(inst: SpiritPoll): Boolean = {
    this.foreachCallback(inst, _.beforeSave)
    methods.save(inst)
  }
  override def update(inst: SpiritPoll): Boolean = methods.update(inst)
  override def delete_!(inst: SpiritPoll): Boolean = methods.delete_!(inst)
  override def findAll(): List[SpiritPoll] = methods.findAll()
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
