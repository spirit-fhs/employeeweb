package de.codecarving.employeeweb.snippet.talkallocator.snippet

import de.codecarving.employeeweb.model.GlobalRequests
import de.codecarving.fhsldap.model.LDAPUtils._
import net.liftweb.textile._
import de.codecarving.employeeweb.model.records.{SpiritTalkAllocator, SpiritTalkAllocatorTalks}
import xml.Text
import net.liftweb.common.{Box, Full, Loggable}
import net.liftweb.http.{LiftResponse, StreamingResponse, SHtml, S}
import scala.collection.mutable.ArrayBuffer

class EvaluateTalkAllocator extends Loggable with GlobalRequests {
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

  private def headers(in: Array[Byte]) = {
      ("Content-type" -> "application/csv") ::
      ("Content-length" -> in.length.toString) ::
      ("Content-disposition" -> "attachment; filename=download.csv") :: Nil
  }

  private def return4Download(in: Array[Byte]): Box[LiftResponse] = {
    Full(StreamingResponse(
      new java.io.ByteArrayInputStream(in),
      () => {},
      in.length,
      headers(in), Nil, 200)
    )
  }

  def render = {
    //TODO Render corrent data for students fhs-id.
    <h2>{ talkAllocator.title.value }</h2>
    <div>{ TextileParser.toHtml(talkAllocator.title.value) }</div>
    <div>{ SHtml.link("/download", () => CurrentDownload(return4Download(data)),
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
