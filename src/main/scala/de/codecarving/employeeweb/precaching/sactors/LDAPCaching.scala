package de.codecarving.employeeweb
package precaching
package sactors

import actors.Actor
import de.codecarving.fhsldap.model.LDAPUtils

import de.codecarving.employeeweb.model.records._
import net.liftweb.common.Loggable

/**
 * Sends a Request to the LDAP Servers.
 */
object LDAPCaching extends Actor with Loggable {

  def act {
    loop {
      react{
        case fhsid: String =>
          reply(LDAPUtils.getAttribute("displayName", fhsid).openOr(fhsid))
        case _ =>

      }
    }
  }
}
