package de.codecarving.employeeweb
package snippet
package pollpal
package snippet

import de.codecarving.employeeweb.model.{ GraphCreators, GlobalRequests }
import net.liftweb.common.{Full, Loggable}
import net.liftweb.http.S
import de.codecarving.employeeweb.model.records.SpiritPollAnswers

/**
 * Graphical evaluation of a Poll.
 */
class PollGraph extends Loggable with GlobalRequests with GraphCreators {

  //TODO clean this mess up!
  CurrentPoll.get match {
    case Full(poll) =>

    case _ =>
      S error "You should not do this!"
      S redirectTo ("/")
  }

  lazy val currentPoll = CurrentPoll.get

  lazy val currentAnswers =
    for(
      i <- SpiritPollAnswers.findAll.filter(currentPoll.open_!.title.value == _.title.value)
    ) yield (i.answer.value, i.votes.value)

  lazy val url =
    ChartChooser.get match {
      case "PieChart" => createPieChart(currentPoll.open_!.title.value, currentAnswers)
      case "BarChart" => createBarChart(currentPoll.open_!.title.value, currentAnswers)
      case _ => ""
    }

  /**
   * Rendering the current image via the Google Chart API.
   */
  def render = {
   //TODO Add handling if Google Api ist not reachable
   <img src={url}></img>
  }

}
