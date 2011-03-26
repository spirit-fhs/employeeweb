package de.codecarving.employeeweb
package snippet
package pollpal
package snippet

import net.liftweb.common.{Full, Loggable}
import scala.xml.Text
import de.codecarving.employeeweb.model.records.{ SpiritPollAnswers, SpiritPoll }
import model.blockUI
import net.liftweb.http.{S, SHtml}

/**
 * Creating a view of all Polls from the Current User in order for evaluation.
 */
class ListPolls extends Loggable with blockUI {
  import de.codecarving.fhsldap.model.User

  def render = {
    reloadAfterDelete("/pollpal/index")
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
                                    {deleteLink(sp)}
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
