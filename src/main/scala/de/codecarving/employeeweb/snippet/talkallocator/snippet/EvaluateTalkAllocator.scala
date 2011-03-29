package de.codecarving.employeeweb.snippet.talkallocator.snippet

import de.codecarving.fhsldap.model.LDAPUtils._
import net.liftweb.textile._
import de.codecarving.employeeweb.model.records.{SpiritTalkAllocator, SpiritTalkAllocatorTalks}
import xml.Text
import net.liftweb.common.{Box, Full, Loggable}
import net.liftweb.http.{LiftResponse, StreamingResponse, SHtml, S}
import scala.collection.mutable.ArrayBuffer
import de.codecarving.employeeweb.model.{SpiritHelpers, GlobalRequests}

/**
 * Creating a List of a TalkAllocator with its Talks and Speakers.
 * With the option for CSV Export.
 */
class EvaluateTalkAllocator extends Loggable with GlobalRequests with SpiritHelpers {
  //TODO generate a nice filename for download.
  // Taking a look if CurrentTalkAllocator is full or not.
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

  lazy val data = buildCSVString(talkAllocator, talkAllocatorTalks)

  private def buildCSVString(alloc: SpiritTalkAllocator,
                             talks: List[SpiritTalkAllocatorTalks]): Array[Byte] = {
    lazy val csvHead = alloc.title.value.replaceAll(";", " ") + ";" +
                       alloc.description.value.replaceAll(";", " ") + "\n"
    lazy val csvBodyHead = "Thema;Beschreibung;Studenten;Vergeben\n"
    lazy val csvBody = for(talk <- talks)
                    yield talk.talkTitle.value.replaceAll(";"," ") + ";" +
                          talk.description.value.replaceAll(";"," ") + ";" +
                          talk.speakers.value.split(";").map(s =>
                            getAttribute("displayName", s).openOr("")).mkString(" - ") + ";" +
                          (if(talk.assigned.value) "Ja" else "") + "\n"

    (csvHead + csvBodyHead + csvBody.mkString)./:(ArrayBuffer[Byte]()) { _ += _.toByte }.toArray
  }

  def render = {
    //TODO Render corrent data for students fhs-id.
    <h2>{ talkAllocator.title.value }</h2>
    <div>{ TextileParser.toHtml(talkAllocator.title.value) }</div>
    <div>{ SHtml.link("/download", () => CurrentDownload(return4Download(data, talkAllocator.title.value)),
                                   Text("Als CSV exportieren")) }</div>
    <hr />
    <table>
      <tr>
        <th>{ "Thema:" }</th>
        <th>{ "Beschreibung:" }</th>
        <th>{ "Student/en:" }</th>
        <th>{ "Vergeben:" }</th>
      </tr>
    { talkAllocatorTalks.flatMap { talk =>
      lazy val speakers = talk.speakers.valueAsSet
      <tr>
        <td>{ talk.talkTitle.value }</td>
        <td>{ talk.description.value }</td>
        <td>{ if(speakers.head == "") { }
              else for(speaker <- speakers.toList)
                     yield getAttribute("displayName", speaker)
                           .openOr("Oops!") + ", " }
        </td>
        <td>{ if(talk.assigned.value) "Ja" } </td>
      </tr>
    }}
    </table>
  }
}
