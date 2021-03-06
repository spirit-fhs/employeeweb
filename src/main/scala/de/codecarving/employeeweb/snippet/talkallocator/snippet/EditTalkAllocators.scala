package de.codecarving.employeeweb
package snippet
package talkallocator
package snippet

import de.codecarving.employeeweb.model.GlobalRequests
import net.liftweb.common.Full._
import net.liftweb.textile.TextileParser
import net.liftweb.common.{Full, Loggable}
import net.liftweb.http.{SHtml, S}
import xml.Text._
import xml.Text
import de.codecarving.employeeweb.model.records.SpiritTalkAllocatorTalks
import model.blockUI

/**
 * Edit or Delete Talks from a TalkAllocator.
 */
class EditTalkAllocators extends Loggable with blockUI {

  CurrentTalkAllocator.get match {
    case Full(talkallocator) =>
      logger info "Found TalkAllocator for Evaluation!"
    case _ =>
      S error "You should not do this!"
      S redirectTo "/"
  }

  lazy val talkAllocator = CurrentTalkAllocator.open_!
  lazy val talkAllocatorTalks = SpiritTalkAllocatorTalks.findAll.filter(
    _.allocatorTitle.value == talkAllocator.title.value)

  def render = {
    reloadAfterDelete("/talkallocator/edit")
    <h2>{ talkAllocator.title.value }</h2>
    <div>{ TextileParser.toHtml(talkAllocator.title.value) }</div>
    <hr />
    <table>
      <tr>
        <th>{ "Thema:" }</th>
        <th>{ "Beschreibung:" }</th>
        <th>{ "Student/en:" }</th>
        <th>{ "Vergeben:" }</th>
        <th>{ "Optionen:" }</th>
      </tr>
    { talkAllocatorTalks.flatMap { talk =>
      <tr>
        <td>{ talk.talkTitle.value }</td>
        <td>{ talk.description.value }</td>
        <td>{ talk.speakers.value.mkString(",") }</td>
        <td>{ if (talk.assigned.value) "Ja" } </td>
        <td>{deleteLink(talk)} <br />
            { SHtml.link("/talkallocator/editTalk", () => CurrentTalkAllocatorTalk(Full(talk)), Text("Ändern")) }</td>
      </tr>
    }}
    </table>
  }
}
