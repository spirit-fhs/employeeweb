package de.codecarving.employeeweb
package precaching
package akkaactors

import akka.actor.Actor
import akka.actor.Actor._
import de.codecarving.fhsldap.model.LDAPUtils

object LDAPCaching {

  val cachingActor = actorOf[LDAPCaching].start
}

class LDAPCaching extends Actor {

  def receive = {
    case fhsid: String =>
      self.reply(LDAPUtils.getAttribute("displayName", fhsid).openOr(fhsid))

    case _ =>

  }
}
