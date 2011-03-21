package de.codecarving.employeeweb.persistence
package h2

import net.liftweb.mapper._

object BackendEntryComments extends BackendEntryComments with LongKeyedMetaMapper[BackendEntryComments] {
  override def dbTableName = "backendentrycomments"

}

class BackendEntryComments extends LongKeyedMapper[BackendEntryComments] with IdPK {
  def getSingleton = BackendEntryComments

  object nr extends MappedInt(this)
  object user extends MappedString(this, 100)
  object comment extends MappedString(this, 1000)
}
