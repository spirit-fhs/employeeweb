package de.codecarving.employeeweb
package snippet

import de.codecarving.employeeweb.model.GlobalRequests
import net.liftweb.common.{Full, Loggable}
import net.liftweb.textile._
import model.records.{SpiritTalkAllocator, SpiritTalkAllocatorTalks}
import net.liftweb.http.{SHtml, S}
import xml.Text._
import xml.Text

class EvaluateTalkAllocator extends Loggable with GlobalRequests {

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

    <h2>{ talkAllocator.title.value }</h2>
    <div>{ TextileParser.toHtml(talkAllocator.title.value) }</div>
    <hr />
    <table>
      <tr>
        <th>{ "Thema:" }</th>
        <th>{ "Beschreibung:" }</th>
        <th>{ "Student/en:" }</th>
        <th>{ "Vergeben:" }</th>
      </tr>
    { talkAllocatorTalks.flatMap { talk =>
      <tr>
        <td>{ talk.talkTitle.value }</td>
        <td>{ talk.description.value }</td>
        <td>{ talk.speaker.value }</td>
        <td>{ if(talk.assigned.value) "Ja" } </td>
      </tr>
    }}
    </table>
  }
}
