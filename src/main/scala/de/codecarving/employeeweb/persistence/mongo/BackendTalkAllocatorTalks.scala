package de.codecarving.employeeweb.persistence
package mongo

import net.liftweb.mongodb._
import record.field.MongoListField
import record.{MongoRecord, MongoId, MongoMetaRecord}
import net.liftweb.record.field._

object BackendTalkAllocatorTalks extends BackendTalkAllocatorTalks with MongoMetaRecord[BackendTalkAllocatorTalks] {

}

class BackendTalkAllocatorTalks extends MongoRecord[BackendTalkAllocatorTalks] with MongoId[BackendTalkAllocatorTalks] {
  def meta = BackendTalkAllocatorTalks

  object allocatorTitle extends StringField(this, 100)
  object talkTitle extends StringField(this, 100)
  object speakers extends MongoListField[BackendTalkAllocatorTalks, String](this)
  object description extends TextareaField(this, 100000)
  object assigned extends BooleanField(this)

}
