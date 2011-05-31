package de.codecarving.employeeweb
package lib

import net.liftweb.util.Props
import de.codecarving.employeeweb.persistence
import spiritrecord.{SpiritMethods, SpiritRecord}
import model.records._

object MethodFactory {

  def apply[T <: SpiritRecord[T]](in: T): SpiritMethods[T] = (db, in) match {

    case (this.rest, in: SpiritEntry) =>
      new persistence.rest.SpiritEntryMethods[T]

    case (this.rest, in: SpiritEntryComments) =>
      new persistence.rest.SpiritEntryCommentsMethods[T]

    case (this.rest, in: SpiritPoll) =>
      new persistence.rest.SpiritPollMethods[T]

    case (this.rest, in: SpiritPollAnswers) =>
      new persistence.rest.SpiritPollAnswerMethods[T]

    case (this.rest, in: SpiritTalkAllocator) =>
      new persistence.rest.SpiritTalkAllocatorMethods[T]

    case (this.rest, in: SpiritTalkAllocatorTalks) =>
      new persistence.rest.SpiritTalkAllocatorTalkMethods[T]

    case (this.h2db, in: SpiritEntry) =>
      new persistence.h2.SpiritEntryMethods[T]

    case (this.h2db, in: SpiritEntryComments) =>
      new persistence.h2.SpiritEntryCommentsMethods[T]

    case (this.h2db, in: SpiritPoll) =>
      new persistence.h2.SpiritPollMethods[T]

    case (this.h2db, in: SpiritPollAnswers) =>
      new persistence.h2.SpiritPollAnswerMethods[T]

    case (this.h2db, in: SpiritTalkAllocator) =>
      new persistence.h2.SpiritTalkAllocatorMethods[T]

    case (this.h2db, in: SpiritTalkAllocatorTalks) =>
      new persistence.h2.SpiritTalkAllocatorTalkMethods[T]

  }

  /**
   * Use these vals to check which Database shall be used.
   */
  lazy val db = Props.get("spirit.admin.record.backentry").openOr((""))
  lazy val mongodb = "mongodb"
  lazy val h2db = "h2db"
  lazy val rest = "rest"
}
