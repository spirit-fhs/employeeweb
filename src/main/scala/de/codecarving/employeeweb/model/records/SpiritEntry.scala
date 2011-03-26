package de.codecarving.employeeweb
package model
package records

import net.liftweb.record.field._
import spiritrecord.{SpiritMetaRecord, SpiritRecord}

import java.util.{ Calendar, TimeZone, Date }
import java.text.SimpleDateFormat
import net.liftweb.record.LifecycleCallbacks
import de.codecarving.fhsldap.model.User
import net.liftweb.util.Props
import persistence.mongo.{BackendEntryCounter => BEC, BackendEntry => BE}
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
      BE.findAll("nr", inst.nr.value).map(_.delete_!)
      true
    case this.h2db =>
      import net.liftweb.mapper._
      h2BE.findAll(By(h2BE.nr,inst.nr.value)).map(_.delete_!)
      //TODO Comments should actually not be deleted here! Need an idea?!
      SpiritEntryComments.findAll.filter(_.nr.value == inst.nr.value).map(_.delete_!)
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
        se.nr.set(b.nr.value)
        se.user.set(b.user.value)
        se.displayName.set(b.displayName.value)
        se.from.set(b.from.value)
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
        se.nr.set(b.nr)
        se.user.set(b.user)
        se.displayName.set(b.displayName)
        se.from.set(b.from)
        se.expires.set(b.expires)
        se.news.set(b.news)
        se.semester.set(b.semester)
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
      be.nr.set(in.nr.value)
      be.displayName.set(in.displayName.value)
      be.semester.set(in.semester.value)
      be.from.set(in.from.value)
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
      be.nr.set(in.nr.value)
      be.displayName.set(in.displayName.value)
      be.semester.set(in.semester.value)
      be.from.set(in.from.value)
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
        if(this.value && newEntry.value) {
          Spitter ! TweetNews(subject.value, semester.valueAsList.map(" #"+_).mkString, nr.value.toString)
        } else if(this.value && !newEntry.value) {
          Spitter ! TweetNews("[Update] " + subject.value, semester.valueAsList.map(" #"+_).mkString, nr.value.toString)
        } else { }
      }
  }

  object emailBool extends BooleanField(this, false)
    with LifecycleCallbacks {
      //TODO If mailing fails send a notice to User.
      //TODO Maybe replace this with a own java mail wrapper?!
      override def beforeSave() = {
        if(this.value && mailerActive) {
           sendMail(From(User.ldapAttributes.displayName.openOr("") +
                    "<" + User.ldapAttributes.email.openOr("") + ">"),
                    Subject(subject.value),
                    (CC(User.ldapAttributes.email.openOr("")) ::
                     xmlToMailBodyType(scala.xml.Unparsed(TextileParser.toHtml(news.value) + footer)) ::
                     semester.valueAsList.map(x =>
                       To(x + Props.get("SemesterMailTail", "")))) :_* )
        } else if(this.value && !mailerActive) {
          logger warn "Mailing ist not Active! Sending Dummy email to " +
            Props.get("spirit.employeeweb.mailer.testEmail","")

            sendMail(From(User.ldapAttributes.displayName.openOr("") +
                     "<" + Props.get("spirit.employeeweb.mailer.testEmail","") + ">"),
                     Subject(subject.value),
                     (To(Props.get("spirit.employeeweb.mailer.testEmail","")) ::
                      xmlToMailBodyType(scala.xml.Unparsed(semester.valueAsList.toString +
                       TextileParser.toHtml(news.value) +
                       footer)) ::
                      Nil) :_* )
        } else { }
    }
  }

  object nr extends IntField(this) with LifecycleCallbacks {

    override def beforeSave() = {
      super.beforeSave
      if(newEntry.value) {
        this.setFromAny(EntryCounter.newNumber)
      } else {
        this.setFromAny(EntryCounter.updateNumber(twitterBool.value,this.value))
      }
    }
  }

  object user extends StringField(this, 100, User.currentUserId.openOr("default"))

  object displayName extends StringField(this, 100, User.ldapAttributes.displayName.openOr("Mr. Default"))

  object subject extends StringField(this, 100)

  object from extends StringField(this, ((new SimpleDateFormat("dd.MM.yyyy")).format(new Date)))

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

  object semester extends StringField(this, 100) {

    def setFromList(in: List[String]): Box[String] = in match {
      case dirty if dirty.contains("") => setFromList(dirty.filterNot(_ == ""))
      case clean if clean.nonEmpty => setFromAny(clean.mkString(";"))
      case _ => genericSetFromAny("")
    }

    def valueAsList(): List[String] = {
      if(!this.valueBox.isEmpty)
        this.value.split(";").toList
      else
        Nil
    }
  }

}
