package de.codecarving.employeeweb
package persistence
package h2

import net.liftweb.mapper._

object BackendTalkAllocatorTalks extends BackendTalkAllocatorTalks with LongKeyedMetaMapper[BackendTalkAllocatorTalks] {
  override def dbTableName = "backendtalkallocatortalks"

}

class BackendTalkAllocatorTalks extends LongKeyedMapper[BackendTalkAllocatorTalks] with IdPK {
  def getSingleton = BackendTalkAllocatorTalks

  object allocatorTitle extends MappedString(this,100)
  object talkTitle extends MappedString(this, 100)
  object speakers extends MappedString(this, 100)
  object description extends MappedTextarea(this, 10000)
  object assigned extends MappedBoolean(this)
}
