package de.codecarving.employeeweb
package precaching
package sactors

import actors.Actor
import de.codecarving.fhsldap.model.LDAPUtils

/**
 * Sends a Request to the LDAP Servers.
 */
object LDAPCaching extends Actor {

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
