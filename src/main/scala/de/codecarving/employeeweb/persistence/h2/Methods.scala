package de.codecarving.employeeweb
package persistence
package h2

import spiritrecord.SpiritRecord
import model.records.{ SpiritEntry, SpiritEntryComments }

object Methods extends Methods

class Methods {

  /**
   * Building a List[SpiritRecord[_]] out of the H2 database.
   */
  def findAll(inst: SpiritEntry): List[SpiritRecord[_]] = {

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
      se
    }
  }

  def findAll(inst: SpiritEntryComments): List[SpiritRecord[_]] = {

    val bec = BackendEntryComments.findAll

    bec map { b =>
      lazy val sec = SpiritEntryComments.createRecord
      sec.user.set(b.user)
      sec.id.set(b._id_)
      sec.comment.set(b.comment)
      sec
    }
  }
}
