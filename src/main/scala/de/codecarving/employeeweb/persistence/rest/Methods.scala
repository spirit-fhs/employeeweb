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

//TODO Refactorings needed!!!!
class SpiritEntryMethods[T] extends SpiritMethods[T] {

  import model.records.SpiritEntry

  implicit val formats = DefaultFormats

  val restURL = Props.get("spirit.admin.record.rest.service", "")
  val h = new Https

  /**
   * Not implemented yet.
   * After implementing please change comment.
   */
  def delete_!(inst: T): Boolean = true

  /**
   * Saving a SpiritEntry to the RESTful DB-Service.
   * @param inst The SpiritEntry to be saved.
   * @return Boolean Saving was successful or not.
   */
  def save(inst: T): Boolean = {

    val in = inst.asInstanceOf[SpiritEntry]

    val asJson = Map("Accept" -> "application/json",
                     "Content-Type" -> "application/json").toMap

    val req = new Request(restURL + "news") <<< in.:-*

    val answer = h(req <:< asJson as_str)

    true
  }

  /**
   * Not implemented yet.
   * After implementing please change comment.
   */
  def update(inst: T): Boolean = true

  /**
   * Fetching all News from RESTful DB-Service for the current user.
   * @return List[SpiritEntry]
   */
  def findAll(): List[T] = {

    val asJson = Map("Accept" -> "application/json").toMap

    val req = new Request(restURL + "news?owner=" + User.currentUserId.open_!)

    val rawJson = h(req <:< asJson as_str)

    val newsList = for {
      i <- (parse(rawJson) \ "news" ).children
    } yield i.extract[news]

    newsList map { nl =>
      val newSE = SpiritEntry.createRecord
      newSE.id.set(nl.news_id)
      newSE.subject.set(nl.title)
      newSE.news.set(nl.content)
      newSE.displayName.set(nl.owner.displayedName)
      newSE.crdate.set(nl.creationDate)
      newSE.semester.set(nl.degreeClass.map(_.title))
      newSE.asInstanceOf[T]
    }
  }
}

class SpiritEntryCommentsMethods[T] extends SpiritMethods[T] {

  import model.records.SpiritEntryComments

  implicit val formats = DefaultFormats

  val restURL = Props.get("spirit.admin.record.rest.service", "")
  val h = new Https

  /**
   * Not implemented yet.
   * After implementing please change comment.
   */
  def delete_!(inst: T): Boolean = true

  /**
   * Not implemented yet.
   * After implementing please change comment.
   */
  def save(inst: T): Boolean = true

  /**
   * Not implemented yet.
   * After implementing please change comment.
   */
  def update(inst: T): Boolean = true

  /**
   * Not implemented yet.
   * After implementing please change comment.
   */
  def findAll(): List[T] = {

    val asJson = Map("Accept" -> "application/json").toMap

    val req = new Request(restURL + "news/comment")

    val rawJson = h(req <:< asJson as_str)

    val commentList = for {
      i <- (parse(rawJson) \ "newsComment" ).children
    } yield i.extract[newsComments]

    commentList map { ncl =>
      val newSEC = SpiritEntryComments.createRecord
      newSEC.id.set(ncl.comment_id)
      newSEC.comment.set(ncl.content)
      newSEC.crdate.set(ncl.creationDate)
      newSEC.entryId.set(ncl.news.news_id)
      newSEC.user.set(ncl.owner.fhs_id)
      newSEC.asInstanceOf[T]
    }
  }
}

class SpiritPollMethods[T] extends SpiritMethods[T] {
  /**
   * Not implemented yet.
   * After implementing please change comment.
   */
  def delete_!(inst: T): Boolean = true
  /**
   * Not implemented yet.
   * After implementing please change comment.
   */
  def save(inst: T): Boolean = true
  /**
   * Not implemented yet.
   * After implementing please change comment.
   */
  def update(inst: T): Boolean = true
  /**
   * Not implemented yet.
   * After implementing please change comment.
   */
  def findAll(): List[T] = {
    Nil
  }
}

class SpiritPollAnswerMethods[T] extends SpiritMethods[T] {
  /**
   * Not implemented yet.
   * After implementing please change comment.
   */
  def delete_!(inst: T): Boolean = true

  /**
   * Not implemented yet.
   * After implementing please change comment.
   */
  def save(inst: T): Boolean = true

  /**
   * Not implemented yet.
   * After implementing please change comment.
   */
  def update(inst: T): Boolean = true

  /**
   * Not implemented yet.
   * After implementing please change comment.
   */
  def findAll(): List[T] = {
    Nil
  }
}

class SpiritTalkAllocatorMethods[T] extends SpiritMethods[T] {

  /**
   * Not implemented yet.
   * After implementing please change comment.
   */
  def delete_!(inst: T): Boolean = true

  /**
   * Not implemented yet.
   * After implementing please change comment.
   */
  def save(inst: T): Boolean = true

  /**
   * Not implemented yet.
   * After implementing please change comment.
   */
  def update(inst: T): Boolean = true

  /**
   * Not implemented yet.
   * After implementing please change comment.
   */
  def findAll(): List[T] = {
    Nil
  }
}

class SpiritTalkAllocatorTalkMethods[T] extends SpiritMethods[T] {

  /**
   * Not implemented yet.
   * After implementing please change comment.
   */
  def delete_!(inst: T): Boolean = true

  /**
   * Not implemented yet.
   * After implementing please change comment.
   */
  def save(inst: T): Boolean = true

  /**
   * Not implemented yet.
   * After implementing please change comment.
   */
  def update(inst: T): Boolean = true

  /**
   * Not implemented yet.
   * After implementing please change comment.
   */
  def findAll(): List[T] = {
    Nil
  }
}

/**
 * Case classes which represent the Database Model from the RESTful DB-Service.
 */
case class owner(fhs_id: String, displayedName: String)

case class news(news_id: Int, title: String,
                  content: String, owner: owner,
                  creationDate: String, degreeClass: List[degreeClass])

case class semester(title: String)

case class newsComments(comment_id: Int, news: newsid, content: String,
                        owner: owner, creationDate: String)

case class degreeClass(title: String, class_id: Int, mail: String)

case class newsid(news_id: Int)
