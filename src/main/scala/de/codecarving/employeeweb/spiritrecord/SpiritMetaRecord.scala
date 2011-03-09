package de.codecarving.employeeweb
package spiritrecord

import java.util.{Calendar, UUID}

import net.liftweb.common.{Box, Empty, Full}
import net.liftweb.json.{Formats, JsonParser}
import net.liftweb.record.{MetaRecord, Record}

trait SpiritMetaRecord[BaseRecord <: SpiritRecord[BaseRecord]] extends MetaRecord[BaseRecord] {
  self: BaseRecord =>

  // Override in implementation!
  def delete_!(inst: BaseRecord): Boolean

  // Override in implementation!
  def findAll: List[Any]

  // Override in implementation!
  def save(inst: BaseRecord): Boolean
}
