package de.codecarving.employeeweb
package snippet
package admin
package snippet


import net.liftweb.util.Helpers._
import net.liftweb.common.Loggable
import net.liftweb.http._
import js.jquery.JqJsCmds
import js._
import js.JsCmds._
import net.liftweb.{http, common}
import http.{js, SHtml}
import js.{JE, JsCmds}

class Settings extends Loggable {

  def render = {
    <div>{ "Settings" }</div>
  }
}
