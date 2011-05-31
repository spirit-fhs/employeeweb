package de.codecarving.employeeweb
package lib

import net.liftweb.util.Props
import de.codecarving.employeeweb.persistence
import model.records.{SpiritEntryComments, SpiritEntry}
import spiritrecord.{SpiritMethods, SpiritRecord}
import javax.swing.text.TabableView

object MethodFactory {

  def apply[T <: SpiritRecord[T]](in: T): SpiritMethods[T] = (db, in) match {

    case (this.rest, in: SpiritEntry) =>
      new persistence.rest.SpiritEntryMethods[T]

    case (this.rest, in: SpiritEntryComments) =>
      new persistence.rest.SpiritEntryCommentsMethods[T]

    case (this.h2db, in: SpiritEntry) =>
      new persistence.h2.SpiritEntryMethods[T]

    case (this.h2db, in: SpiritEntryComments) =>
      new persistence.h2.SpiritEntryCommentsMethods[T]
  }

  /**
   * Use these vals to check which Database shall be used.
   */
  lazy val db = Props.get("spirit.admin.record.backentry").openOr((""))
  lazy val mongodb = "mongodb"
  lazy val h2db = "h2db"
  lazy val rest = "rest"
}
