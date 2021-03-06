package bootstrap.liftweb

import net.liftweb._
import util._

import common._
import http._
import sitemap._

import de.codecarving.fhsldap.fhsldap
import de.codecarving.employeeweb.lib.{ MenuBuilder, DBChooser }
import de.codecarving.employeeweb.model.GlobalRequests

class Boot extends Loggable with DBChooser[Boot] with MenuBuilder[Boot] with GlobalRequests {
  def boot {

    /**
     * We need to dispatch /download in order to provide files to download for a User.
     */
    LiftRules.dispatch.append {
      case Req("download" :: _, _, GetRequest) =>
        () => CurrentDownload
    }

    LiftRules.addToPackages("de.codecarving.employeeweb")
    // Registering the snippet packages.
    LiftRules.addToPackages("de.codecarving.employeeweb.snippet.talkallocator")
    LiftRules.addToPackages("de.codecarving.employeeweb.snippet.news")
    LiftRules.addToPackages("de.codecarving.employeeweb.snippet.pollpal")
    LiftRules.addToPackages("de.codecarving.employeeweb.snippet.admin")

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

    val useLDAP = Props.get("ldap.server.auth.use", "") == "true"
    if (!useLDAP && H2DB == db) {
      import de.codecarving.employeeweb.dummydata.Dummy
      Dummy.createDummyPolls
      Dummy.createDummyTalkAllocator
      Dummy.createDummyEntrys
    }

  }
}
