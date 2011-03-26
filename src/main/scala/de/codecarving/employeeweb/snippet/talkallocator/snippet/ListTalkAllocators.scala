package de.codecarving.employeeweb
package snippet
package talkallocator
package snippet

import de.codecarving.employeeweb.model.blockUI
import net.liftweb.common.{Full, Loggable}
import de.codecarving.fhsldap.model.User
import scala.xml.{ Text, NodeSeq }

import de.codecarving.employeeweb.model.records.{ SpiritTalkAllocator, SpiritTalkAllocatorTalks}
import net.liftweb.http.{S, SHtml}

/**
 * Listing all TalkAllocators from the current User in order
 * to evaluate, delete or edit them.
 */
class ListTalkAllocators extends Loggable with blockUI {

  def render = {
    reloadAfterDelete("/talkallocator/index")
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
                                    {deleteLink(sta)}
          </td>
        </tr>
        </tbody>
      </table>
    }
  }
}
