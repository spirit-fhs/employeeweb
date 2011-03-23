package de.codecarving.employeeweb
package snippet
package talkallocator
package snippet

import net.liftweb.http.SHtml
import de.codecarving.employeeweb.model.GlobalRequests
import net.liftweb.common.{Full, Loggable}
import de.codecarving.fhsldap.model.User
import scala.xml.{ Text, NodeSeq }

import de.codecarving.employeeweb.model.records.{ SpiritTalkAllocator, SpiritTalkAllocatorTalks}

class ListTalkAllocators extends Loggable with GlobalRequests {

  def render = {
    //TODO Implement editing TalkAllocators.
    //TODO Implement evaluating TalkAllocators.

    SpiritTalkAllocator.findAll.filter(
      _.user.value == User.currentUserId.openOr("default")
    ) flatMap { sta =>
      <table style="border:1">
        <thead>
				  <tr>
					  <th width="100%" colspan="4">TalkAllocator: {sta.title.value}</th>
				  </tr>
			  </thead>
        <tbody>
        <tr>
          <td colspan="4">Optionen: {SHtml.link("/talkallocator/edit", () => CurrentTalkAllocator(Full(sta)), Text("Editieren"))}
                                    {SHtml.link("/talkallocator/evaluate", () => CurrentTalkAllocator(Full(sta)), Text("Auswerten"))}
                                    {SHtml.link("/talkallocator/index", () => sta.delete_!, Text("Löschen"))}
          </td>
        </tr>
        </tbody>
      </table>
    }
  }
}
