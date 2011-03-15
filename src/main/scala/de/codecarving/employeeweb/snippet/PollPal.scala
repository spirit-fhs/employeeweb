package de.codecarving.employeeweb
package snippet

import net.liftweb._
import util.Helpers._
import common.Loggable
import http._
import js.jquery.JqJsCmds

import js._
import JsCmds._
import JE._

import net.liftweb.http.js.JsCmd
import net.liftweb.http.{SHtml, S}
import net.liftweb.util.BindHelpers._

import net.liftweb.common.{Loggable, Empty, Full}
import model.{GlobalRequests, SpiritPollAnswers, SpiritPoll}
import xml.NodeSeq
import net.liftweb.http.js.JsCommands

class PollPal extends Loggable with GlobalRequests {

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

  def showLines = "* *" #> (answerSet.toList.flatMap(renderTextfield): NodeSeq)

  def addTextfield(ns: NodeSeq): NodeSeq = {
    val div = S.attr("div") openOr "where"
    SHtml.ajaxButton(ns, () => {
      JqJsCmds.AppendHtml(div, renderTextfield(SpiritPollAnswers.createRecord))
    })
  }

  private def renderTextfield(in: SpiritPollAnswers): NodeSeq = {
    answerSet = answerSet + in
    <div id={in.answer.value}>{SHtml.ajaxText("", { s => in.answer.set(s); Noop })}</div>
  }

  def render = {

    def process(): JsCmd = {
      openPoll.answerCount.set(answerSet.size)
      for(i <- answerSet) {
        i.title.set(openPoll.title.value)
        i.save
      }
      openPoll.save
      S.redirectTo("/")
    }

    "name=expires" #> openPoll.expires.toForm.open_! &
    "name=title" #> (openPoll.title.toForm.open_! ++ SHtml.hidden(process))

  }

}
