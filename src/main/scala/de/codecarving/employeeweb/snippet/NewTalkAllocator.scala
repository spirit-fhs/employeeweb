package de.codecarving.employeeweb
package snippet

import net.liftweb.http.{S, SHtml}
import net.liftweb.util.Helpers._
import model.GlobalRequests
import net.liftweb.common.{Full, Loggable}
import model.records.SpiritTalkAllocator

class NewTalkAllocator extends Loggable with GlobalRequests {

  lazy val newTalkAlloc = SpiritTalkAllocator.createRecord

  def render = {

    def process() = {
      NewTalkAllocator(Full(newTalkAlloc))
      S redirectTo "/talkallocator/addTalkAllocatorTalks"
    }

    "name=title" #> SHtml.text("", newTalkAlloc.title.set(_), "size" -> "50") &
    "name=description" #> (newTalkAlloc.description.toForm.open_! ++ SHtml.hidden(process))
  }
}
