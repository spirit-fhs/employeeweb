package de.codecarving.employeeweb
package persistence
package rest

import dispatch._
import net.liftweb.json._
import net.liftweb.util.Props
import de.codecarving.fhsldap.model.User
import spiritrecord.SpiritMethods

/**
 * Got this from
 * https://github.com/n8han/Databinder-Dispatch/blob/master/http/src/main/scala/Http.scala
 * This is only a temp solution, when working with SSL we need to do something REAL here.
 */
class Https extends BlockingHttp with HttpsLeniency {
  /** Unadorned handler return type */
  type HttpPackage[T] = T
  /** Synchronously access and return plain result value */
  def pack[T](req: { def abort() }, result: => T) = result
}

class SpiritEntryMethods[T] extends SpiritMethods[T] {

  import model.records.SpiritEntry

  implicit val formats = DefaultFormats

  val restURL = Props.get("spirit.admin.record.rest.service", "")
  val h = new Https

  def delete_!(inst: T): Boolean = true

  def save(inst: T): Boolean = true

  def update(inst: T): Boolean = true

  def findAll(): List[T] = {

    val req = new Request(restURL + "news?owner=" + User.currentUserId.open_!)
    val rawJson = h(req.as_str)

    val newsList = for {
      i <- (parse(rawJson) \ "news" ).children
    } yield i.extract[news]

    newsList map { nl =>
      val newSE = SpiritEntry.createRecord
      newSE.id.set(nl.news_id)
      newSE.subject.set(nl.title)
      newSE.news.set(nl.content)
      newSE.displayName.set(nl.displayedName)
      newSE.crdate.set(nl.creationDate)
      newSE.semester.set(nl.classes map { _.title })
      newSE.asInstanceOf[T]
    }
  }
}

class SpiritEntryCommentsMethods[T] extends SpiritMethods[T] {

  import model.records.SpiritEntryComments

  implicit val formats = DefaultFormats

  val restURL = Props.get("spirit.admin.record.rest.service", "")
  val h = new Https

  def delete_!(inst: T): Boolean = true

  def save(inst: T): Boolean = true

  def update(inst: T): Boolean = true

  def findAll(): List[T] = {

    val req = new Request(restURL + "news/comments")
    val rawJson = h(req.as_str)

    val commentList = for {
      i <- (parse(rawJson) \ "newsComments" ).children
    } yield i.extract[newsComments]

    commentList map { ncl =>
      val newSEC = SpiritEntryComments.createRecord
      newSEC.id.set(ncl.comment_id)
      newSEC.comment.set(ncl.content)
      newSEC.crdate.set(ncl.creationDate)
      newSEC.entryId.set(ncl.news_id)
      newSEC.user.set(ncl.owner)
      newSEC.asInstanceOf[T]
    }
  }
}

class SpiritPollMethods[T] extends SpiritMethods[T] {

  def delete_!(inst: T): Boolean = true

  def save(inst: T): Boolean = true

  def update(inst: T): Boolean = true

  def findAll(): List[T] = {
    Nil
  }
}

class SpiritPollAnswerMethods[T] extends SpiritMethods[T] {

  def delete_!(inst: T): Boolean = true

  def save(inst: T): Boolean = true

  def update(inst: T): Boolean = true

  def findAll(): List[T] = {
    Nil
  }
}

class SpiritTalkAllocatorMethods[T] extends SpiritMethods[T] {

  def delete_!(inst: T): Boolean = true

  def save(inst: T): Boolean = true

  def update(inst: T): Boolean = true

  def findAll(): List[T] = {
    Nil
  }
}

class SpiritTalkAllocatorTalkMethods[T] extends SpiritMethods[T] {

  def delete_!(inst: T): Boolean = true

  def save(inst: T): Boolean = true

  def update(inst: T): Boolean = true

  def findAll(): List[T] = {
    Nil
  }
}

/**
 * Case classes which represent the Database Model from the REST-DB Service.
 */
case class news(news_id: Int, title: String,
                  content: String, displayedName: String,
                  creationDate: String, classes: List[semester])

case class semester(title: String)

case class newsComments(comment_id: Int, news_id: Int, content: String, owner: String,
                         displayedName: String, creationDate: String)
