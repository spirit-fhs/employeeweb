package de.codecarving.employeeweb.spiritrecord

trait SpiritMethods[T <: SpiritRecord[T]] {

  def delete_!(inst: T): Boolean
  def save(inst: T): Boolean
  def update(inst: T): Boolean
  def findAll(): List[T]
}
