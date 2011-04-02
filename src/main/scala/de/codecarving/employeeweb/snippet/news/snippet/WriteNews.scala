package de.codecarving.employeeweb
package snippet
package news
package snippet

import net.liftweb._
import util.Helpers._
import common.Loggable
import http._
import js._
import JsCmds._
import JE._

import common._
import js.JE.ElemById
import scala.xml._

import scala.collection.immutable.List
import util.{CssBind, Props}
import de.codecarving.employeeweb.model.records.SpiritEntry
import model.{SpiritHelpers, GlobalRequests}

/**
 * Creating the UI for writing Entrys.
 */
class WriteNews extends Loggable with GlobalRequests with EntryPreview with SpiritHelpers {

  lazy val openEntry =
    CurrentEntry.get match {
      case Full(entry) =>
        logger info "Entry was found: " + entry.id.value + "!"
        entry.newEntry.set(false)
        entry
      case Empty =>
        logger info "Creating new Entry to save later!"
        SpiritEntry.createRecord
      case _ =>
        logger info "This should not have happend, but why did it?!"
        SpiritEntry.createRecord
    }

  private var semesterList = openEntry.semester.value.toSet


  /**
   * Rendering the input form for an entry.
   */
  def render = {

    /**
     * Processing the input page.
     */
    def process(): JsCmd = {

      openEntry.semester.setFromDirtyList(semesterList.toList)
      openEntry.save(openEntry.newEntry.value)

      S.redirectTo("/news/news")
    }

    "name=twitterBool"  #> openEntry.twitterBool.toForm.open_! &
    "name=emailBool"    #> openEntry.emailBool.toForm.open_! &
    "name=displayName"  #> openEntry.displayName.toForm.open_! &
    "name=subject"      #> openEntry.subject.toForm.open_! &
    "name=news"         #> openEntry.news.toForm.open_! &
    "name=expires"      #> (openEntry.expires.toForm.open_! ++ SHtml.hidden(process)) &
    "type=preview"      #> createPreviewButton &
    "name=tooltip"      #> createTextileTooltip
  }

  /**
   * Building the Checkboxes for the Mailinglists.
   * If Crudentry contains any semester, the checkbox is set to true.
   */
  def makecheckboxlist(in: NodeSeq): NodeSeq = {
      (".checkbox_row" #> Props.get(Props.get("Semester","") + "_" + S.attr("semester").open_!, "").split(";").toList
        .map ( sem =>
      ".title" #> sem &
      ".checkbox" #> SHtml.ajaxCheckbox(semesterList contains sem,
                                    { v =>
                                      if(v) semesterList = semesterList + sem;
                                      if(!v) semesterList = semesterList - sem;
                                    Noop } ))
      ).apply(in)
  }

}
