package de.codecarving.employeeweb
package snippet
package talkallocator
package snippet

import net.liftweb.common.Full._
import net.liftweb.common.{Full, Loggable}
import net.liftweb.http.{SHtml, S}
import net.liftweb.util.Helpers._
import de.codecarving.employeeweb.model.GlobalRequests

/**
 * Editing a specific Talk.
 */
class EditTalkAllocatorTalk extends Loggable with GlobalRequests {

  CurrentTalkAllocatorTalk.get match {
    case Full(talkallocatortalk) =>
      logger info "Found TalkAllocatorTalk for editing!"
    case _ =>
      S error "You should not do this!"
      S redirectTo "/"
  }

  lazy val talkallocatortalk = CurrentTalkAllocatorTalk.open_!

  def render = {

    def process() = {

      talkallocatortalk.update
      S redirectTo "/talkallocator/edit"
    }

    "name=talkTitle" #> talkallocatortalk.talkTitle.value &
    "name=description" #> (talkallocatortalk.description.toForm.open_! ++ SHtml.hidden(process))
  }
}
