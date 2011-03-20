package de.codecarving.employeeweb
package snippet

import net.liftweb.util.Helpers._
import net.liftweb.http.{S, SHtml}
import net.liftweb.common.{Full, Loggable}
import net.liftweb.textile.TextileParser
import xml.NodeSeq
import net.liftweb.http.js.jquery.JqJsCmds

import net.liftweb.util.BindHelpers
import model.GlobalRequests
import model.records.SpiritTalkAllocatorTalks
import net.liftweb.http.js._
import JsCmds._

class NewTalkAllocatorTalks extends Loggable with GlobalRequests {

  //TODO Wrap tryo around this!
  lazy val TalkAlloc = NewTalkAllocator.open_!
  var talkSet = Set[SpiritTalkAllocatorTalks]()

  def render = {

    def process() = {
      //TODO Make it pretty
      //TODO Duplicates -> TalkAllocs.
      //TODO Multispeakers

      for(cur <- talkSet) {
        if(cur.talkTitle.value == "") {
          logger warn "Throwing away a Talk since its empty!"
        }
        else {
          cur.allocatorTitle.set(TalkAlloc.title.value)
          cur.save
        }
      }
      TalkAlloc.save
      S redirectTo "/talkallocator/index"
    }

    "name=title" #> TalkAlloc.title.value &
    "name=released" #> TalkAlloc.released.toForm &
    "name=expires" #> TalkAlloc.expires.toForm &
    "name=description" #> (TextileParser.toHtml(TalkAlloc.description.value) ++ SHtml.hidden(process))
  }

  def createTalkFields(in: NodeSeq): NodeSeq = {
    var talkTitle = ""
    var description = ""
    <div>{"Thema: "}<br />{ SHtml.ajaxText("", {
            s => talkTitle = s;
            Noop },
            "id" -> "createTalkFields_talkTitle", "size" -> "50")}
    <br />{"Beschreibung: "}<br />{ SHtml.ajaxTextarea("", {
            s => description = s;
            Noop },
            "id" -> "createTalkFields_description")}
    <br />{ SHtml.ajaxButton("Thema Hinzufügen", () => {
            val newTalk = SpiritTalkAllocatorTalks.createRecord
            newTalk.talkTitle.set(talkTitle)
            newTalk.description.set(description)
            talkSet = talkSet + newTalk
            JqJsCmds.AppendHtml("talk_fields", renderCreated(newTalk)) &
            JsCmds.SetValueAndFocus("createTalkFields_description", "") &
            JsCmds.SetValueAndFocus("createTalkFields_talkTitle", "")})}
    </div>
  }

  /**
   * Renders the added Talks.
   */
  private def renderCreated(in: SpiritTalkAllocatorTalks): NodeSeq = {
    //TODO Option to delete them instantly before saving.
    <table style="border:1">
      <tr>
        <th style="border:1"><div id={in.talkTitle.value}>{ in.talkTitle.value }</div></th>
      </tr>
      <tr>
        <td colspan="4"><div id={in.description.value}>{ in.description.value }</div></td>
      </tr>
    </table>
  }


  /**
   * Injecting the added Talks.
   */
  def showFields =
    "* *" #> (talkSet.toList.flatMap(renderCreated): NodeSeq)
}
