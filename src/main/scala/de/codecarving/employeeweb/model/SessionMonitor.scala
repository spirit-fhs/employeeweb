package de.codecarving.employeeweb
package model

import net.liftweb.actor.LiftActor
import net.liftweb.http.SessionWatcherInfo._
import net.liftweb.http.{SessionWatcherInfo, SessionInfo}

object SessionMonitor extends LiftActor {

  var sessionSize = 0
  var allSessions: Map[String, SessionInfo] = Map[String, SessionInfo]()

  protected def messageHandler = {
    case SessionWatcherInfo(sessions) =>
      sessionSize = sessions.size
      allSessions = sessions
  }
}
