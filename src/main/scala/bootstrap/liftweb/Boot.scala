package bootstrap.liftweb

import net.liftweb._
import util._
import Helpers._

import common._
import http._
import sitemap._
import Loc._

import de.codecarving.fhsldap.fhsldap
import de.codecarving.employeeweb.model.{MenuBuilder, DBChooser}

class Boot extends Loggable with DBChooser[Boot] with MenuBuilder[Boot] {
  def boot {

    LiftRules.addToPackages("de.codecarving.employeeweb")

    LiftRules.setSiteMap(SiteMap(sitemap:_*))

    LiftRules.ajaxStart =
      Full(() => LiftRules.jsArtifacts.show("ajax-loader").cmd)

    LiftRules.ajaxEnd =
      Full(() => LiftRules.jsArtifacts.hide("ajax-loader").cmd)

    LiftRules.early.append(_.setCharacterEncoding("UTF-8"))

    LiftRules.htmlProperties.default.set((r: Req) =>
      new Html5Properties(r.userAgent))

    ResourceServer.allow {
      case "images" :: _ => true
      case "css" :: _ => true
      case "blueprint" :: _ => true
      case "jquery" :: _ => true
      case "js" :: _ => true
    }

    // Starting the FhS LDAP Module
    fhsldap.init
  }
}