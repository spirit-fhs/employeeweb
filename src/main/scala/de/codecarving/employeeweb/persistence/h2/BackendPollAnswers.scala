package de.codecarving.employeeweb.persistence
package h2

import net.liftweb.mapper._

object BackendPollAnswers extends BackendPollAnswers with LongKeyedMetaMapper[BackendPollAnswers] {
  override def dbTableName = "backendpollanswers"

}

class BackendPollAnswers extends LongKeyedMapper[BackendPollAnswers] with IdPK {
  def getSingleton = BackendPollAnswers

  object title extends MappedString(this,100)
  object answer extends MappedString(this, 100)
  object votes extends MappedInt(this)
}
