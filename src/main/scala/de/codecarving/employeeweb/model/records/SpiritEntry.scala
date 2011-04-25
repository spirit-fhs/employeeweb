package de.codecarving.employeeweb
package model
package records

import net.liftweb.record.field._
import spiritrecord._
import fields.SpiritListField

import java.util.{ Calendar, TimeZone, Date }
import java.text.SimpleDateFormat
import net.liftweb.record.LifecycleCallbacks
import de.codecarving.fhsldap.model.User
import net.liftweb.util.Props
import persistence.mongo.{BackendEntryCounter => BEC, BackendEntry => BE, BackendEntryComments => BECO}
import persistence.EntryCounter
import net.liftweb.mapper.By._
import persistence.h2.{BackendEntryComments, BackendEntry => h2BE, BackendEntryCounter => h2BEC}
import net.liftweb.common.Logger._
import net.liftweb.common.{Loggable, Box, Full}
import model.Spitter
import model.tweetCases.TweetNews
import net.liftweb.util.Mailer._
import net.liftweb.textile.TextileParser

/**
 * The Record which will be used for the backend implementation of the persistence layer.
 */
object SpiritEntry extends SpiritEntry with SpiritMetaRecord[SpiritEntry] {

  /**
   * Deleting the Entry by it's Number.
   */
  override def delete_!(inst: SpiritEntry): Boolean = db match {
    case this.mongodb =>
      BE.findAll("_id_", inst.id.value).map(_.delete_!)
      BECO.findAll("_id_", inst.id.value).map(_.delete_!)
      true
    case this.h2db =>
      import net.liftweb.mapper._
      h2BE.findAll(By(h2BE._id_,inst.id.value)).map(_.delete_!)
      //TODO Comments should actually not be deleted here! Need an idea?!
      BackendEntryComments.findAll.filter(_._id_ == inst.id.value).map(_.delete_!)
      true
    case _=>
      println("not implemented yet")
      false
  }

  /**
   * Returning a List of all Entries.
   */
  override def findAll: List[SpiritEntry] = db match {
    case this.mongodb =>
      lazy val be = BE.findAll
      be map { b =>
        lazy val se = SpiritEntry.createRecord
        se.id.set(b._id_.value)
        se.user.set(b.user.value)
        se.displayName.set(b.displayName.value)
        se.crdate.set(b.crdate.value)
        se.expires.set(b.expires.value)
        se.news.set(b.news.value)
        se.semester.set(b.semester.value)
        se.subject.set(b.subject.value)
        se
      }

    case this.h2db =>
      lazy val be = h2BE.findAll
      be map { b =>
        lazy val se = SpiritEntry.createRecord
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

    case _ =>
      println("not implemented yet")
      Nil
  }

  /**
   * Saving the Entry.
   */
  override def save(inst: SpiritEntry): Boolean = db match {
    case this.mongodb =>
      foreachCallback(inst, _.beforeSave)
      val in = inst.asInstanceOf[SpiritEntry]
      lazy val be = BE.createRecord
      be.user.set(in.user.value)
      be._id_.set(in.id.value)
      be.displayName.set(in.displayName.value)
      be.semester.set(in.semester.value)
      be.crdate.set(in.crdate.value)
      be.expires.set(in.expires.value)
      be.news.set(in.news.value)
      be.subject.set(in.subject.value)
      be.save
      true

    case this.h2db =>
      foreachCallback(inst, _.beforeSave)
      val in = inst.asInstanceOf[SpiritEntry]
      lazy val be = h2BE.create
      be.user.set(in.user.value)
      be._id_.set(in.id.value)
      be.displayName.set(in.displayName.value)
      be.semester.set(in.semester.value.mkString(";"))
      be.crdate.set(in.crdate.value)
      be.expires.set(in.expires.value)
      be.news.set(in.news.value)
      be.subject.set(in.subject.value)
      be.save
      true

    case _ =>
      println("not implemented")
      false
    }

  override def update(inst: SpiritEntry): Boolean = db match {
    case this.mongodb =>
      logger warn "Not Implemented yet..."
      false
    case this.h2db =>
      logger warn "Not Implemented yet..."
      false
    case _ =>
      logger warn "Not Implemented yet..."
      false
  }
}

class SpiritEntry extends SpiritRecord[SpiritEntry] with SpiritHelpers with Loggable {
  def meta = SpiritEntry


  object newEntry extends BooleanField(this, true)

  object twitterBool extends BooleanField(this, true)
    with LifecycleCallbacks {
      override def beforeSave() = {
        if (this.value && newEntry.value) {
          Spitter ! TweetNews(subject.value, semester.value.map(" #"+_).mkString, id.value.toString)
        } else if(this.value && !newEntry.value) {
          Spitter ! TweetNews("[Update] " + subject.value, semester.value.map(" #"+_).mkString, id.value.toString)
        } else { }
      }
  }

  object emailBool extends BooleanField(this, false)
    with LifecycleCallbacks {
      import lib.SpiritMailer
      import scala.collection.mutable.Set

      //TODO Maybe move this to afterSave?!
      override def beforeSave() = {

        if (this.value && mailerActive) {
          SpiritMailer.send(TextileParser.toHtml(news.value).toString,
                      subject.value,
                      semester.value./:(Set[String]()) {
                        (o, u) => o += (u + Props.get("SemesterMailTail", ""))
                      }.toArray )
        } else if (this.value && !mailerActive) {
          logger warn "Mailing ist not Active! Sending Dummy email defined at: " +
            "spirit.employeeweb.mailer.testEmail"
          SpiritMailer.send(TextileParser.toHtml(news.value).toString,
                      subject.value,
                      Array(Props.get("spirit.employeeweb.mailer.testEmail","")))
        } else { }
    }
  }

  object id extends IntField(this) with LifecycleCallbacks {

    override def beforeSave() = {

      super.beforeSave
      if (newEntry.value) {
        this.setFromAny(EntryCounter.newNumber)
      } else {
        this.setFromAny(EntryCounter.updateNumber(twitterBool.value,this.value))
      }
    }
  }

  object user extends StringField(this, 100, User.currentUserId.openOr("default"))

  object displayName extends StringField(this, 100, User.ldapAttributes.displayName.openOr("Mr. Default"))

  //TODO Set Subject if User leaves it blank.
  object subject extends StringField(this, 100)

  object crdate extends StringField(this, ((new SimpleDateFormat("dd.MM.yyyy")).format(new Date)))

  object expires extends StringField(this, ((new SimpleDateFormat("dd.MM.yyyy")).format(new Date)))
    with LifecycleCallbacks {

      override def beforeSave() {
        super.beforeSave
        this.setFromAny(dateValidator(this.value))
      }
    }

  object news extends TextareaField(this, 100000) {

    override def textareaRows  = 12
    override def textareaCols = 80
  }

  object semester extends SpiritListField[SpiritEntry, String](this)

}
