package de.codecarving.employeeweb
package spiritrecord

import java.util.{Calendar, UUID}

import net.liftweb.common.{Box, Empty, Full}
import net.liftweb.json.{Formats, JsonParser}
import net.liftweb.record.{MetaRecord, Record}
import net.liftweb.util.Props

/**
 * Mixin providing Record capabilities.
 */
trait SpiritMetaRecord[BaseRecord <: SpiritRecord[BaseRecord]] extends MetaRecord[BaseRecord] {
  self: BaseRecord =>

  // Override in implementation!
  def delete_!(inst: BaseRecord): Boolean

  // Override in implementation!
  def findAll: List[Any]

  // Override in implementation!
  def save(inst: BaseRecord): Boolean

  // Override in implementation!
  def update(inst: BaseRecord): Boolean

  /**
   * Use these vals to check which Database shall be used.
   * Match them in the implementation for a Record.
   */
  lazy val db = Props.get("spirit.admin.record.backentry").openOr((""))
  lazy val mongodb = "mongodb"
  lazy val h2db = "h2db"
  lazy val rest = "rest"

}
