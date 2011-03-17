package de.codecarving.employeeweb
package snippet

import de.codecarving.employeeweb.model.{SpiritPollAnswers, GraphCreators, GlobalRequests}
import net.liftweb.common.{Full, Loggable}
import net.liftweb.http.S

class PollGraph extends Loggable with GlobalRequests with GraphCreators {

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

  lazy val url = createBarChart(currentPoll.open_!.title.value, currentAnswers)

  def render = {
   //TODO Add handling if Google Api ist not reachable
   <img src={url}></img>
  }

}
