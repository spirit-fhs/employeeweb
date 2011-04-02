package de.codecarving
package employeeweb
package spiritrecord
package fields

import net.liftweb.common.{Box, Empty, Failure, Full}
import net.liftweb.json.JsonAST._
import net.liftweb.record.{Field, FieldHelpers, MandatoryTypedField, Record}
import net.liftweb.http.js.JE.{Str, JsNull, JsRaw}

/**
* List field.
*/
class SpiritListField[OwnerType <: SpiritRecord[OwnerType], ListType](rec: OwnerType)
  extends Field[List[ListType], OwnerType]
  with MandatoryTypedField[List[ListType]]
{

  def owner = rec

  def defaultValue = List[ListType]()

  /**
   * A Dirty List is considered a List with Empty Strings ( "" ).
   * Although we could exclude them before setting this field,
   * it is a more comfortable way to have a Method like this.
   * @param in The List to be saved.
   */
  def setFromDirtyList(in: List[ListType]) {

    this.set(in.filterNot(_ == ""))
  }

  @deprecated("Not implemented!")
  def setFromAny(in: Any): Box[List[ListType]] = Empty

  @deprecated("Not implemented!")
  def setFromJValue(jvalue: JValue) = Empty

  @deprecated("Not implemented!")
  def setFromString(in: String): Box[List[ListType]] = Empty

  @deprecated("Not implemented!")
  def toForm = Empty // FIXME

  @deprecated("Not implemented!")
  def asJs = JsNull

  @deprecated("Not implemented!")
  def asJValue = JNothing

}
