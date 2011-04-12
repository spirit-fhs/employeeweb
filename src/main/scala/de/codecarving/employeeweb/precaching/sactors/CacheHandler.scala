package de.codecarving.employeeweb
package precaching
package sactors

import model.records._
import collection.mutable.HashMap
import net.liftweb.common.{Full, Box, Loggable}
import collection.mutable.SynchronizedMap

/**
 * Prototype of handling the caching of LDAP attributes.
 */
object CacheHandler extends Loggable {

  /**
   * PreCached names go into this SynchronizedMap.
   * Alternatives will come, since we don't know
   * how efficient this is at the moment.
   */
  private var studentNames =
    new HashMap[String, Any]
      with SynchronizedMap[String, Any]

  /**
   * PreFetching the Data that could be read.
   */
  def preFetch() {

    lazy val sec = SpiritTalkAllocatorTalks.findAll

    lazy val fhsids = (for(i <- sec) yield i.speakers.value).flatten.toSet

    fhsids map { s =>
      if (!studentNames.contains(s)) {
        lazy val newName = LDAPCaching !? (2000, s)
        if (newName.isEmpty || newName.get != "")
          studentNames += (s -> newName)
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
