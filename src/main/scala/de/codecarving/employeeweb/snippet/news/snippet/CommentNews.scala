package de.codecarving.employeeweb
package snippet
package news
package snippet

import model.GlobalRequests
import net.liftweb.common.{Full, Loggable}
import net.liftweb.http.{ S, SHtml }
import model.records.SpiritEntryComments
import net.liftweb.textile.TextileParser
import xml.Text

/**
 * Creating the View to add comments to a Users own News.
 */
class CommentNews extends Loggable with GlobalRequests {

  CurrentEntry.get match {
    case Full(id) =>

    case _ =>
      S error "You should not do this!"
      S redirectTo ("/")
  }

  val entry = CurrentEntry.open_!
  val newComment = SpiritEntryComments.createRecord
  newComment.id.set(entry.id.value)

  def render = {

    def process() = {

      newComment.save
      S redirectTo "/news/news"
    }

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
        { SpiritEntryComments.findAll.filter(
          _.id.value == entry.id.value
          ).flatMap( sec =>
        <tr>
          <td colspan="4">Kommentar von {sec.user.value} :
          <br />{ sec.comment.value }</td>
        </tr>
        )}
        <tr>
          <td colspan="4">
            Neuer Kommentar:<br />
            <form>
              {newComment.comment.toForm.open_!}<br />
              {SHtml.submit("Speichern", () => process)}
            </form>
          </td>
        </tr>
			</tbody>
		  </table>
  }
}
