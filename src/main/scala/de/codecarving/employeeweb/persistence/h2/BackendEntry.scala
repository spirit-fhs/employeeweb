package de.codecarving.employeeweb.persistence
package h2

import net.liftweb.mapper._

object BackendEntry extends BackendEntry with LongKeyedMetaMapper[BackendEntry] {
  override def dbTableName = "backendentrys"

}

class BackendEntry extends LongKeyedMapper[BackendEntry] with IdPK {
  def getSingleton = BackendEntry

  object nr extends MappedInt(this)
  object user extends MappedString(this, 100)
  object displayName extends MappedString(this, 100)
  object subject extends MappedString(this, 100)
  object from extends MappedString(this, 100)
  object expires extends MappedString(this, 100)
  object news extends MappedTextarea(this, 100000)
  object semester extends MappedString(this, 100)
}
