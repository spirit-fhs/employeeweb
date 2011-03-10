package de.codecarving.employeeweb
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
import model.{GlobalRequests, SpiritEntry}


class WriteNews extends Loggable with GlobalRequests with EntryPreview {

  private var semesterList = List[String]()

  lazy val openEntry =
    CurrentEntry.get match {
      case Full(entry) =>
        logger info "Entry was found: " + entry.nr.value + "!"
        entry.newEntry.set(false)
        entry
      case Empty =>
        logger info "Creating new Entry to save later!"
        SpiritEntry.createRecord
      case _ =>
        logger info "This should not have happend, but why did it?!"
        SpiritEntry.createRecord
    }

  /**
   * Processing the input page.
   */
  def process() {

    openEntry.semester.setFromList(semesterList)
    openEntry.save(openEntry.newEntry.value)
    S.redirectTo("/index")
  }

  /**
   * Rendering the input form for an entry.
   */
  def render = {
    "#myform [action] " #> "/" &
    "name=twitterBool"  #> openEntry.twitterBool.toForm.open_! &
    "name=emailBool"    #> openEntry.emailBool.toForm.open_! &
    "name=displayName"  #> openEntry.displayName.toForm.open_! &
    "name=subject"      #> openEntry.subject.toForm.open_! &
    "name=expires"      #> openEntry.expires.toForm.open_! &
    "name=news"         #> openEntry.news.toForm.open_! &
    "type=submit"       #> SHtml.onSubmitUnit(process) &
    "type=preview"      #> <span class="lift:WriteNews.mkPreview">
                              <json:script></json:script>
                              <div id="dialog" title="Vorschau">
                              <div id="news_preview"></div></div>
                           <button json:onclick="onclick" id="onclick">Vorschau</button></span>
  }

  /**
   * Building the Checkboxes for the Mailinglists.
   * If Crudentry contains any semester, the checkbox is set to true.
   */
  def makecheckboxlist(in: NodeSeq): NodeSeq = {
      (".checkbox_row" #> Props.get(Props.get("Semester","") + "_" + S.attr("semester").open_!, "").split(";").toList
        .map ( sem =>
      ".title" #> sem &
      ".checkbox" #> SHtml.checkbox(openEntry.semester.valueAsList contains sem,
                                    if (_) semesterList = sem :: semesterList))
      ).apply(in)
  }

}
