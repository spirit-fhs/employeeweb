package de.codecarving.employeeweb
package lib

import net.liftweb.util.Props
import de.codecarving.employeeweb.persistence

object MethodFactory {

  def apply() = db match {
    case this.rest => new persistence.rest.Methods
    case this.h2db => new persistence.h2.Methods
  }

  /**
   * Use these vals to check which Database shall be used.
   */
  lazy val db = Props.get("spirit.admin.record.backentry").openOr((""))
  lazy val mongodb = "mongodb"
  lazy val h2db = "h2db"
  lazy val rest = "rest"
}
