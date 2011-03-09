package de.codecarving.employeeweb
package spiritrecord

import net.liftweb.common.{Box, Full}
import net.liftweb.record.{MetaRecord, Record}

trait SpiritRecord[MyType <: SpiritRecord[MyType]] extends Record[MyType] {
  self: MyType =>

  //The meta record (the object that contains the meta result for this type)
  def meta: SpiritMetaRecord[MyType]

  // Save the instance and return the instance
  def save: MyType = {
    meta.save(this)
    this
  }

  def save(in: Boolean): MyType = in match {
    case true =>
      meta.save(this)
      this
    case false =>
      runSafe {
        meta.delete_!(this)
      }
      meta.save(this)
      this
  }

  // Delete the instance from backing store
  def delete_! : Boolean = {
    runSafe {
      meta.delete_!(this)
    }
  }

}
