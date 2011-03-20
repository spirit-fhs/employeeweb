package de.codecarving.employeeweb
package persistence
package h2

import net.liftweb.mapper._

object BackendTalkAllocator extends BackendTalkAllocator with LongKeyedMetaMapper[BackendTalkAllocator] {
  override def dbTableName = "backendtalkallocator"

}

class BackendTalkAllocator extends LongKeyedMapper[BackendTalkAllocator] with IdPK {
  def getSingleton = BackendTalkAllocator

  object user extends MappedString(this,100)
  object displayName extends MappedString(this, 100)
  object title extends MappedString(this , 100)
  object description extends MappedTextarea(this, 10000)
  object released extends MappedBoolean(this)
  object expires extends MappedString(this, 100)
}
