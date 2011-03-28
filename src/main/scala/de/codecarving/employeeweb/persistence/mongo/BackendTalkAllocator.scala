package de.codecarving.employeeweb.persistence
package mongo

import net.liftweb.mongodb._
import record.{MongoRecord, MongoId, MongoMetaRecord}
import net.liftweb.record.field._

object BackendTalkAllocator extends BackendTalkAllocator with MongoMetaRecord[BackendTalkAllocator] {

}

class BackendTalkAllocator extends MongoRecord[BackendTalkAllocator] with MongoId[BackendTalkAllocator] {
  def meta = BackendTalkAllocator

  object user extends StringField(this, 100)
  object displayName extends StringField(this, 100)
  object title extends StringField(this, 100)
  object expires extends StringField(this, 100)
  object description extends TextareaField(this, 10000)
  object released extends BooleanField(this)

}
