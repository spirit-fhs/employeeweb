package de.codecarving.employeeweb.persistence
package h2

import net.liftweb.mapper._

object BackendPoll extends BackendPoll with LongKeyedMetaMapper[BackendPoll] {
  override def dbTableName = "backendpoll"

}

class BackendPoll extends LongKeyedMapper[BackendPoll] with IdPK {
  def getSingleton = BackendPoll

  object title extends MappedString(this,100)
  object answerCount extends MappedInt(this)
  object expires extends MappedString(this, 100)
  object user extends MappedString(this, 100)
  object displayName extends MappedString(this, 100)
}
