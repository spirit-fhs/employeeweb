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
import net.liftweb.common.Logger._
import net.liftweb.common.{Loggable, Box, Full}
import model.Spitter
import model.tweetCases.TweetNews
import net.liftweb.textile.TextileParser
import persistence.h2.{BackendEntryComments, BackendEntry => h2BE, BackendEntryCounter => h2BEC}

/**
 * The Record which will be used for the backend implementation of the persistence layer.
 */
object SpiritEntry extends SpiritEntry with SpiritMetaRecord[SpiritEntry] {

  override def save(inst: SpiritEntry): Boolean = methods.save(this)
  override def update(inst: SpiritEntry): Boolean = methods.update(this)
  override def delete_!(inst: SpiritEntry): Boolean = methods.delete_!(this)
  override def findAll(): List[SpiritEntry] = methods.findAll()
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
