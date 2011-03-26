package de.codecarving.employeeweb
package snippet
package admin
package snippet

import de.codecarving.fhsldap.model.User
import net.liftweb.actor.LiftActor
import net.liftweb.http.{SessionInfo, SessionMaster, SessionWatcherInfo}
import net.liftweb.common.{Empty, Loggable}
import model.SessionMonitor

/**
 * Creating Real Time Data about Users and Sessions.
 */
class RealTimeData extends Loggable {

  SessionMaster.sessionWatchers = SessionMonitor :: SessionMaster.sessionWatchers

  def render = {

    <div>
    <div>Aktive Sessions: { SessionMonitor.sessionSize }</div>
    <div>Session Infos:</div>
      { for(sess <- SessionMonitor.allSessions)
            yield <div>{ sess._2 }</div> }
    </div>
  }
}
