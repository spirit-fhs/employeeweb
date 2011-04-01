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
