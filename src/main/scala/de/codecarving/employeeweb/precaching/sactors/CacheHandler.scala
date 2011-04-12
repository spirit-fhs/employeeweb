package de.codecarving.employeeweb
package precaching
package sactors

import model.records._
import collection.mutable.{ HashMap, SynchronizedMap }
import net.liftweb.common.{Full, Box, Loggable}

/**
 * CacheHandler with scala Actors!
 */
object CacheHandler extends Loggable {

  LDAPCaching.start

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
      if (!studentNames.contains(s)) {

        val newName = LDAPCaching !? (2000, s)

        newName match {
          case Some(name) =>
            println(name)
            if (name != s) studentNames += (s -> newName)

          case _ =>
        }
      }
    }
  }

  /**
   * Returning PreFetched Data or PreFetching it.
   * @param fhsid The fhsid which shall be transformed into a real name.
   * @return The RealName in a Box.
   * @todo Add efficient way, if LDAP Server is not available!
   */
  def getDisplayName(fhsid: String): Box[Any] =
    studentNames match {
      case a if (studentNames contains fhsid) =>
        logger warn "Allready have the Name!"
        Full(studentNames(fhsid))

      case b =>
        logger warn "Fetching from LDAP!"
        LDAPCaching !? (1000, fhsid) match {
          case Some(newName) =>
            if (newName != "" && newName != fhsid) {
              studentNames += (fhsid -> newName)
              Full(newName)
            } else {
              Full(fhsid)
            }

          case None =>
            Full(fhsid)
        }
  }
}
