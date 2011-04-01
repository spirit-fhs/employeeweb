package de.codecarving.employeeweb.persistence
package mongo

import net.liftweb.mongodb._
import record.field.MongoListField
import record.{MongoRecord, MongoId, MongoMetaRecord}
import net.liftweb.record.field._

object BackendEntry extends BackendEntry with MongoMetaRecord[BackendEntry] {

}

class BackendEntry extends MongoRecord[BackendEntry] with MongoId[BackendEntry] {
  def meta = BackendEntry

  object _id_ extends IntField(this, 4)
  object user extends StringField(this, 100)
  object displayName extends StringField(this, 100)
  object subject extends StringField(this, 100)
  object crdate extends StringField(this, 100)
  object expires extends StringField(this, 100)
  object news extends TextareaField(this, 100000)
  object semester extends MongoListField[BackendEntry, String](this)

}
