package de.codecarving.employeeweb
package snippet
package pollpal
package snippet

import net.liftweb._
import http._
import js.jquery.JqJsCmds

import js._
import JsCmds._
import net.liftweb.http.js.JsCmd
import net.liftweb.http.{SHtml, S}
import net.liftweb.util.BindHelpers._

import net.liftweb.common.{ Loggable, Empty, Full }
import xml.NodeSeq
import de.codecarving.employeeweb.model.records.{ SpiritPoll, SpiritPollAnswers }
import de.codecarving.employeeweb.model.{ SpiritHelpers, GlobalRequests }

class PollPal extends Loggable with GlobalRequests with SpiritHelpers {

  var answerSet = Set[SpiritPollAnswers]()

  lazy val openPoll =
    CurrentPoll.get match {
      case Full(poll) =>
        logger info "Poll was found: " + poll.title.value + "!"
        poll
      case Empty =>
        logger info "Creating new Poll to save later!"
        SpiritPoll.createRecord
      case _ =>
        logger info "This should not have happend, but why did it?!"
        SpiritPoll.createRecord
    }

  /**
   * Creating a Button which enables the User to add multiple Input Fields.
   */
  def addTextfield(ns: NodeSeq): NodeSeq = {
    val div = S.attr("div") openOr "where"
    SHtml.ajaxButton(ns, () => {
      JqJsCmds.AppendHtml(div, renderTextfield(SpiritPollAnswers.createRecord))
    })
  }

  /**
   * Creates an ajax input field, in order to have n Answers on a Poll.
   */
  private def renderTextfield(in: SpiritPollAnswers): NodeSeq = {
    if(answerSet.size == 9) return <div></div>
    answerSet = answerSet + in
    <div id={in.answer.value}>{SHtml.ajaxText("", { s => in.answer.set(s); Noop })}</div>
  }

  /**
   * Viewing the created input ajax fields.
   */
  def showLines = "* *" #> (answerSet.toList.flatMap(renderTextfield): NodeSeq)

  //TODO Reset button.
  def render = {

    def process(): JsCmd = {
      //TODO Create nicer way to tell the user that the poll with this name allready exists.
      for(cur <- SpiritPoll.findAll){
        if(cur.title.value == openPoll.title.value) {
          S.error("Diese Umfrage existiert bereits. Bitte einen anderen Titel wählen!")
          S.redirectTo("/pollpal/newpoll")
        }
      }
      answerSet = removeDuplicatesfromanswerSet(answerSet)
      openPoll.answerCount.set(answerSet.size)
      for(cur <- answerSet) {
        if(cur.answer.value == "") {
          logger warn "Throwing away an answer since its empty!"
        }
        else {
          cur.title.set(openPoll.title.value)
          cur.save
        }
      }
      openPoll.save
      S.redirectTo("/")
    }

    "name=expires" #> openPoll.expires.toForm.open_! &
    "name=title" #> (openPoll.title.toForm.open_! ++ SHtml.hidden(process))
  }

}
