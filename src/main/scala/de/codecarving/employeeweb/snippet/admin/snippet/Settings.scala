package de.codecarving.employeeweb
package snippet
package admin
package snippet


import net.liftweb.util.Helpers._
import net.liftweb.common.Loggable
import net.liftweb.http._
import js.jquery.JqJsCmds
import js._
import js.JsCmds._
import net.liftweb.{http, common}
import http.{js, SHtml}
import js.{JE, JsCmds}

import xml.NodeSeq
import model.records.SpiritPollAnswers
import precaching.{sactors, akkaactors, szactors}

class Settings extends Loggable {

  def benchmark = {
    logger warn "Running Benchmarks...."

    logger warn "Running Akka Actors...."
    val akkaStartTime = System.currentTimeMillis()
    akkaactors.CacheHandler.preFetch()
    val akkaStopTime = System.currentTimeMillis()

    logger warn "Running Scala Actors...."
    val sActorsStartTime = System.currentTimeMillis()
    sactors.CacheHandler.preFetch()
    val sActorsStopTime = System.currentTimeMillis()

    logger warn "Running Scalaz Actors...."
    val szActorsStartTime = System.currentTimeMillis()
    szactors.CacheHandler.preFetch()
    val szActorsStopTime = System.currentTimeMillis()

    println("akka: " + (akkaStopTime - akkaStartTime) + " " + akkaactors.CacheHandler.studentNames)
    println("scala a: " + (sActorsStopTime - sActorsStartTime) + " " + sactors.CacheHandler.studentNames)
    println("scalaZ: " + (szActorsStopTime - szActorsStartTime) + " "  + szactors.CacheHandler.studentNames)
  }

  def render = {
    
    "name=benchmark" #> SHtml.submitButton(() => benchmark)
  }
}
