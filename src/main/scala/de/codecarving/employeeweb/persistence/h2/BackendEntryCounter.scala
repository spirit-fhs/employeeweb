package de.codecarving.employeeweb.persistence
package h2

import net.liftweb.mapper._

object BackendEntryCounter extends BackendEntryCounter with LongKeyedMetaMapper[BackendEntryCounter] {

}

class BackendEntryCounter extends LongKeyedMapper[BackendEntryCounter] with IdPK {
  def getSingleton = BackendEntryCounter

  object counter extends MappedInt(this)
}
