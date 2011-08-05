package de.codecarving.employeeweb.spiritrecord

/**
 * SpiritMethods provide functionality in order to create classes for the MethodFactory
 * which are allocated to a implementation of SpiritRecord.
 */
trait SpiritMethods[T] {

  def delete_!(inst: T): Boolean
  def save(inst: T): Boolean
  def update(inst: T): Boolean
  def findAll(): List[T]
}
