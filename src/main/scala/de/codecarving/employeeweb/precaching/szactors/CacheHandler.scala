package de.codecarving.employeeweb
package precaching
package szactors

import scalaz.Scalaz._
import scalaz.concurrent.Actor
import de.codecarving.fhsldap.model.LDAPUtils
import collection.mutable.{SynchronizedMap, HashMap}
import model.records.SpiritTalkAllocatorTalks
import net.liftweb.common.{Loggable, Box, Full}

/**
 * CacheHandler with scalaz Actors!
 */
object CacheHandler extends Loggable {

  /**
   * PreCached names go into this SynchronizedMap.
   * Alternatives will come, since we don't know
   * how efficient this is at the moment.
   */
  var studentNames =
    new HashMap[String, Any]
      with SynchronizedMap[String, Any]

  /**
   * PreFetching the Data that could be read.
   */
  def preFetch() {

    val sec = SpiritTalkAllocatorTalks.findAll

    val fhsids =
      (for(i <- sec)
        yield i.speakers.value).flatten.toSet.filter(_ != "")

    fhsids map { s =>
      if (!studentNames.contains(s))
        LDAPCaching.myActor ! s
    }
  }

  @deprecated("Not implemented at the moment!")
  def getDisplayName(fhsid: String): Box[Any] = None

  object LDAPCaching {

    val myActor: Actor[String] = actor {
      case fhsid if (fhsid.nonEmpty) =>
        lazy val newName =
          LDAPUtils.getAttribute("displayName", fhsid).openOr(fhsid)
        if (newName != fhsid)
          studentNames += (fhsid -> newName)

      case _ =>

    }
  }

}
