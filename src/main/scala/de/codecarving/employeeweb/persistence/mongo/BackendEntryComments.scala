package de.codecarving.employeeweb.persistence
package mongo

import net.liftweb.mongodb._
import record.{MongoRecord, MongoId, MongoMetaRecord}
import net.liftweb.record.field._

object BackendEntryComments extends BackendEntryComments with MongoMetaRecord[BackendEntryComments] {

}

class BackendEntryComments extends MongoRecord[BackendEntryComments] with MongoId[BackendEntryComments] {
  def meta = BackendEntryComments

  object _id_ extends IntField(this)
  object user extends StringField(this, 100)
  object comment extends StringField(this, 100)
  object crdate extends StringField(this, 100)

}
