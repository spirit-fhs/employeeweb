package de.codecarving.employeeweb.persistence
package mongo

import net.liftweb.mongodb._
import record.{MongoRecord, MongoId, MongoMetaRecord}
import net.liftweb.record.field._

object BackendPollAnswers extends BackendPollAnswers with MongoMetaRecord[BackendPollAnswers] {

}

class BackendPollAnswers extends MongoRecord[BackendPollAnswers] with MongoId[BackendPollAnswers] {
  def meta = BackendPollAnswers

  object votes extends IntField(this)
  object title extends StringField(this, 100)
  object answer extends StringField(this, 100)

}
