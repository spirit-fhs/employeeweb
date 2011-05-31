package de.codecarving.employeeweb
package persistence
package h2

import spiritrecord.SpiritMethods
import model.records.{SpiritEntryComments, SpiritEntry}

class SpiritEntryMethods[T] extends SpiritMethods[T] {

  def delete_!(inst: T): Boolean = true

  def save(inst: T): Boolean = true

  def update(inst: T): Boolean = true

  /**
   * Building a List[SpiritRecord[_]] out of the H2 database.
   */
  def findAll(): List[T] = {

    lazy val be = BackendEntry.findAll

    be map { b =>
      val se = SpiritEntry.createRecord
      se.id.set(b._id_)
      se.user.set(b.user)
      se.displayName.set(b.displayName)
      se.crdate.set(b.crdate)
      se.expires.set(b.expires)
      se.news.set(b.news)
      se.semester.set(b.semester.split(";").toList)
      se.subject.set(b.subject)
      se.asInstanceOf[T]
    }
  }
}

class SpiritEntryCommentsMethods[T] extends SpiritMethods[T] {

  def delete_!(inst: T): Boolean = true

  def save(inst: T): Boolean = true

  def update(inst: T): Boolean = true

  def findAll(): List[T] = {
    Nil
  }
}
