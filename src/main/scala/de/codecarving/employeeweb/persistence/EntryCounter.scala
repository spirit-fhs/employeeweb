package de.codecarving.employeeweb
package persistence

import persistence.mongo.{BackendEntryCounter => BEC, BackendEntry => BE}
import persistence.h2.{BackendEntry => h2BE, BackendEntryCounter => h2BEC}
import net.liftweb.util.Props

object EntryCounter extends EntryCounter

class EntryCounter {

  lazy val db = Props.get("spirit.admin.record.backentry").openOr((""))
  lazy val MONGODB = "mongodb"
  lazy val H2DB = "h2db"

  /**
   * Setting the Counter to an exact Value.
   */
  def setCounter(in: Int): Int = db match {
    case MONGODB =>
      val count = if(BEC.findAll.isEmpty) BEC.createRecord else BEC.findAll.head
        count.counter.set( in )
        count.save
      in
    case H2DB =>
      val count = if(h2BEC.findAll.isEmpty) h2BEC.create else h2BEC.findAll.head
        count.counter.set( in )
        count.save
      in
    case _ => 0
  }

  /**
   * Getting the current Counter.
   */
  def getCounter(): Int = db match {
    case MONGODB =>
      if(BEC.findAll.isEmpty) 0 else BEC.findAll.head.counter.value
      //BEC.findAll.head.counter.value
    case H2DB =>
      if(h2BEC.findAll.isEmpty) 0 else h2BEC.findAll.head.counter
      //h2BEC.findAll.head.counter
    case _ =>
      0
  }

  /**
   * Getting a newNumber and Incrementing the counter.
   */
  def newNumber(): Int = {
    val nr = getCounter
    setCounter(nr + 1)
    nr
  }

  /**
   * Getting a new Number but only Incrementing if twitter is True.
   */
  def updateNumber(twitter: Boolean, oldNr: Int): Int = {
    val newNr: Int = {
      if(twitter) {
        newNumber
      } else {
        oldNr
      }
    }

    if (newNr != oldNr) {
      setCounter(newNr + 1)
    }
      newNr
  }
}
