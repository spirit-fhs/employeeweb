package de.codecarving.employeeweb
package snippet
package news
package snippet

import de.codecarving.fhsldap.model.User
import net.liftweb.http.SHtml._
import net.liftweb.http.S
import net.liftweb.common.{Box, Empty, Full, Loggable}
import net.liftweb.textile._

import scala.xml.Text
import de.codecarving.employeeweb.model.records.{ SpiritEntry, SpiritEntryComments }
import model.{blockUI, GlobalRequests}

class ListEntries extends Loggable with blockUI {

  /**
   * Renders all written Entrys by the User.
   * With the option links to edit or delete the Entry.
   */
  def render = {
    reloadAfterDelete("/news/news")
    SpiritEntry.findAll.filter(
      _.user.value == User.currentUserId.openOr("default")
    ).sortWith(
      (entry1, entry2) => (entry1.id.value > entry2.id.value)
    ).flatMap(entry =>
      <table style="border:1">
			<thead>
				<tr>
					<th width="100%" colspan="4">Betreff: {entry.subject.value}</th>
				</tr>
			</thead>
			<tbody>
				<tr>
					<td>Semester: {entry.semester.value.mkString(" ")}</td>
					<td>Verfasser: {entry.displayName.value}</td>
					<td>Vom: {entry.crdate.value}</td>
					<td>Nr: {entry.id.value}</td>
				</tr>
				<tr>
					<td colspan="4">{TextileParser.toHtml(entry.news.value)}</td>
				</tr>
        <tr>
          <td colspan="4">Optionen: {link("/news/edit", () => CurrentEntry(Full(entry)), Text("Editieren"))}
                                    {deleteLink(entry)}
                                    {link("/news/comment", () => CurrentEntry(Full(entry)), Text("Kommentieren"))}
          </td>
        </tr>
        { SpiritEntryComments.findAll.filter(
          _.id.value == entry.id.value
          ).flatMap( sec =>
        <tr>
          <td colspan="4">Kommentar:
          <br />{ sec.comment.value }</td>
        </tr>
        )}
			</tbody>
		  </table>
    )
  }
}
