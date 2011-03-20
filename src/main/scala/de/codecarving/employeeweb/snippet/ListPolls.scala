package de.codecarving.employeeweb
package snippet

import net.liftweb.http.SHtml
import net.liftweb.common.{Full, Loggable}
import scala.xml.Text
import model.GlobalRequests
import model.records.{ SpiritPollAnswers, SpiritPoll }

class ListPolls extends Loggable with GlobalRequests {
  import de.codecarving.fhsldap.model.User

  //TODO Delete different! This is too dirty! -> Line 21
  def render = {

    SpiritPoll.findAll.filter(
      _.user.value == User.currentUserId.openOr("default")
    ) flatMap { sp =>
      <table style="border:1">
        <thead>
				  <tr>
					  <th width="100%" colspan="4">Umfrage: {sp.title.value}</th>
				  </tr>
			  </thead>
        <tbody>
        <tr>
          <td colspan="4">Optionen: {SHtml.link("/pollpal/pollgraph", () => { CurrentPoll(Full(sp)); ChartChooser("BarChart") }, Text("Als BarChart darstellen"))}
                                    {SHtml.link("/pollpal/pollgraph", () => { CurrentPoll(Full(sp)); ChartChooser("PieChart") }, Text("Als PieChart darstellen"))}
                                    {SHtml.link("/pollpal/index", () => sp.delete_!, Text("Löschen"))}
          </td>
        </tr>
        {SpiritPollAnswers.findAll.filter(_.title.value == sp.title.value) flatMap { spa =>
        <tr>
          <td>{spa.answer.value}</td>
          <td>{spa.votes.value}</td>
        </tr>
        }}
        </tbody>
      </table>
    }
  }
}
