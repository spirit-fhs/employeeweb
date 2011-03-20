package de.codecarving.employeeweb
package snippet

import de.codecarving.fhsldap.model.User
import net.liftweb.http.SHtml._
import net.liftweb.common.{Box, Empty, Full, Loggable}
import net.liftweb.textile._

import scala.xml.Text
import model.GlobalRequests
import model.records.SpiritEntry

class ListEntries extends Loggable with GlobalRequests {
  //TODO Delete different! This is too dirty! -> Line 41
  /**
   * Renders all written Entrys by the User.
   * With the option links to edit or delete the Entry.
   */
  def render = {

    SpiritEntry.findAll.filter(
      _.user.value == User.currentUserId.openOr("default")
    ).sortWith(
      (entry1, entry2) => (entry1.nr.value > entry2.nr.value)
    ).flatMap(v =>
      <table style="border:1">
			<thead>
				<tr>
					<th width="100%" colspan="4">Betreff: {v.subject.value}</th>
				</tr>
			</thead>
			<tbody>
				<tr>
					<td>Semester: {v.semester.value.split(";").mkString(" ")}</td>
					<td>Verfasser: {v.displayName.value}</td>
					<td>Vom: {v.from.value}</td>
					<td>Nr: {v.nr.value}</td>
				</tr>
				<tr>
					<td colspan="4">{TextileParser.toHtml(v.news.value)}</td>
				</tr>
        <tr>
          <td colspan="4">Optionen: {link("/news/edit", () => CurrentEntry(Full(v)), Text("Editieren"))}
                                    {link("/news/news", () => v.delete_!, Text("Löschen"))}
          </td>
        </tr>
			</tbody>
		  </table>
    )
  }
}
