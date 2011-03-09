package de.codecarving.employeeweb.persistence
package mongo

import net.liftweb.mongodb._
import record.{MongoRecord, MongoId, MongoMetaRecord}
import net.liftweb.record.field._

object BackendEntryCounter extends BackendEntryCounter with MongoMetaRecord[BackendEntryCounter] {

}

class BackendEntryCounter extends MongoRecord[BackendEntryCounter] with MongoId[BackendEntryCounter] {
  def meta = BackendEntryCounter

  object counter extends IntField(this, 0)
}
