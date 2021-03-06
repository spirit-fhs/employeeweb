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
import net.liftweb.common.{Loggable, Box, Full}

/**
 * The Record which will be used for the backend implementation of the persistence layer.
 */
object SpiritEntry extends SpiritEntry with SpiritMetaRecord[SpiritEntry] {

  override def save(inst: SpiritEntry): Boolean = {
    this.foreachCallback(inst, _.beforeSave)
    methods.save(inst)
  }
  override def update(inst: SpiritEntry): Boolean = methods.update(inst)
  override def delete_!(inst: SpiritEntry): Boolean = methods.delete_!(inst)
  override def findAll(): List[SpiritEntry] = methods.findAll()
}

class SpiritEntry extends SpiritRecord[SpiritEntry] with SpiritHelpers with Loggable {
  def meta = SpiritEntry


  object newEntry extends BooleanField(this, true)

  object twitterBool extends BooleanField(this, true)
    with LifecycleCallbacks {

  }

  object emailBool extends BooleanField(this, false)
    with LifecycleCallbacks {

   }


  object id extends IntField(this) with LifecycleCallbacks {

  }

  object user extends StringField(this, 100, User.currentUserId.openOr("default"))

  object displayName extends StringField(this, 100, User.ldapAttributes.displayName.openOr("Mr. Default"))

  object subject extends StringField(this, 100) with LifecycleCallbacks {

    override def beforeSave() {
      if (this.value.isEmpty) {
        this.set(news.value./:(("", 20)) { (output, input) =>
          if (output._2 == 0) output
          else (output._1 + input, output._2 - 1)
        }._1 + "...")
      }
    }
  }

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


   /**
    * Creating a JSON in order to send it to the RESTful DB Service.
    * See Specs @
    * https://pads.fh-schmalkalden.de/wiki/spirit/database/specification/createNews
    * @todo Implement this properly, maybe a different function name? ;)!
    */
   def :-* = """{ "news": {
           "title" : """" + this.subject.value.toString + """",
           "content" : """" + this.news.value.toString + """",
           "expireDate" : "2011-12-24 12:00:00",
           "degreeClass" : [{
             "class_id" : 5
           }],
           "owner" : {
             "fhs_id" : """" + this.user.value.toString + """"
           }}}"""
}
