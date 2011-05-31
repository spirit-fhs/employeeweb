package de.codecarving.employeeweb
package spiritrecord

import java.util.{Calendar, UUID}

import net.liftweb.common.{Box, Empty, Full}
import net.liftweb.json.{Formats, JsonParser}
import net.liftweb.record.{MetaRecord, Record}
import lib.MethodFactory

/**
 * Mixin providing Record capabilities.
 */
trait SpiritMetaRecord[BaseRecord <: SpiritRecord[BaseRecord]] extends MetaRecord[BaseRecord]
  with SpiritMethods[BaseRecord] {
  self: BaseRecord =>

  lazy val methods: SpiritMethods[BaseRecord] = MethodFactory(this)

}
