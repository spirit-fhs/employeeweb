package de.codecarving.employeeweb.persistence
package mongo

import net.liftweb.mongodb._
import record.{MongoRecord, MongoId, MongoMetaRecord}
import net.liftweb.record.field._

object BackendPoll extends BackendPoll with MongoMetaRecord[BackendPoll] {

}

class BackendPoll extends MongoRecord[BackendPoll] with MongoId[BackendPoll] {
  def meta = BackendPoll

  object title extends StringField(this, 100)
  object answerCount extends IntField(this)
  object expires extends StringField(this, 100)
  object user extends StringField(this, 100)
  object displayName extends StringField(this, 100)

}
