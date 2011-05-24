package de.codecarving.employeeweb
package spiritrecord

import java.util.{Calendar, UUID}

import net.liftweb.common.{Box, Empty, Full}
import net.liftweb.json.{Formats, JsonParser}
import net.liftweb.record.{MetaRecord, Record}
import net.liftweb.util.Props
import lib.MethodFactory

/**
 * Mixin providing Record capabilities.
 */
trait SpiritMetaRecord[BaseRecord <: SpiritRecord[BaseRecord]] extends MetaRecord[BaseRecord]
  with SpiritMethods[BaseRecord] {
  self: BaseRecord =>

  val methods = MethodFactory()

  /**
   * @todo Need to remove these, but first refactor SpiritRecords implementations.
   */
  lazy val db = Props.get("spirit.admin.record.backentry").openOr((""))
  lazy val mongodb = "mongodb"
  lazy val h2db = "h2db"
  lazy val rest = "rest"

}
