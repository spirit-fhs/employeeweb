package de.codecarving.employeeweb.spiritrecord

trait SpiritMethods {

  def delete_![T <: SpiritRecord[T]](in: SpiritRecord[T]): Boolean
  def save[T <: SpiritRecord[T]](inst: SpiritRecord[T]): Boolean
  def update[T <: SpiritRecord[T]](inst: SpiritRecord[T]): Boolean
  def findAll(): List[_]
}
