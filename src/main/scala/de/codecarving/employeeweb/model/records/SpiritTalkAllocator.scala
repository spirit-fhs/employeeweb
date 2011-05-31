package de.codecarving.employeeweb
package model
package records

import net.liftweb.record.field._
import spiritrecord.{SpiritMetaRecord, SpiritRecord}

import net.liftweb.record.LifecycleCallbacks
import de.codecarving.fhsldap.model.User
import java.text.SimpleDateFormat
import net.liftweb.common.{Loggable, Box, Full}

object SpiritTalkAllocator extends SpiritTalkAllocator with SpiritMetaRecord[SpiritTalkAllocator] {

  override def save(inst: SpiritTalkAllocator): Boolean = {
    this.foreachCallback(inst, _.beforeSave)
    methods.save(inst)
  }
  override def update(inst: SpiritTalkAllocator): Boolean = methods.update(inst)
  override def delete_!(inst: SpiritTalkAllocator): Boolean = methods.delete_!(inst)
  override def findAll(): List[SpiritTalkAllocator] = methods.findAll()
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
