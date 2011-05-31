package de.codecarving.employeeweb
package persistence
package rest

import dispatch.Http
import dispatch.Http._
import net.liftweb.json._
import net.liftweb.util.Props
import de.codecarving.fhsldap.model.User
import spiritrecord.SpiritMethods
import model.records.{SpiritEntryComments, SpiritEntry}

class SpiritEntryMethods[T] extends SpiritMethods[T] {

  def delete_!(inst: T): Boolean = true

  def save(inst: T): Boolean = true

  def update(inst: T): Boolean = true

  implicit val formats = DefaultFormats

  val restURL = Props.get("spirit.admin.record.rest.service", "")
  val http = new Http

  /**
   * Using lift-json to extract the rawJson data into case classes and into SpiritRecords.
   */
  def findAll(): List[T] = {

    val rawJson = http((restURL + "news?owner=" + User.currentUserId.open_!).as_str)

    val newsList = for {
      i <- (parse(rawJson) \ "news" ).children
    } yield i.extract[news]

    newsList map { nl =>
      val newSE = SpiritEntry.createRecord
      newSE.id.set(nl.id)
      newSE.subject.set(nl.title)
      newSE.news.set(nl.content)
      newSE.displayName.set(nl.displayedName)
      newSE.crdate.set(nl.creationDate)
      newSE.semester.set(nl.classes map { _.title })
      newSE.asInstanceOf[T]
    }
  }

  /**
   * Case classes which represent the Database Model from the REST-DB Service.
   */
  case class news(id: Int, title: String,
                  content: String, displayedName: String,
                  creationDate: String, classes: List[semester],
                  newsComments: List[newsComment])
  case class semester(title: String)
  case class newsComment(id: Int, content: String,
                         displayedName: String, creationDate: String)

}

class SpiritEntryCommentsMethods[T] extends SpiritMethods[T] {

  def delete_!(inst: T): Boolean = true

  def save(inst: T): Boolean = true

  def update(inst: T): Boolean = true

  def findAll(): List[T] = {
    Nil
  }
}
