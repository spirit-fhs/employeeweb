package de.codecarving.employeeweb
package model

import _root_.net.liftweb._
import http._
import SHtml._
import js._
import JsCmds._
import js.jquery._
import JqJsCmds._
import common._
import util._
import Helpers._
import xml.{ NodeSeq, Text }
import spiritrecord.SpiritRecord

trait blockUI extends GlobalRequests {

  //TODO Always redirect to the page the User came from!
  object reloadAfterDelete extends RequestVar[String]("/index")

  def deleteLink[T <: SpiritRecord[T]](in: T) = {
    SHtml.a(() => { CurrentSpiritRecord(Full(in));
             S.runTemplate(List("_delete_template")).
             map(ns => ModalDialog(ns)) openOr
             Alert("Couldn't find _delete_template")}, Text("Löschen"))
  }

  def confirmdelete(in: NodeSeq) = {
    ("name=yes" #> ((b: NodeSeq) => ajaxButton(b, () => {
         CurrentSpiritRecord.open_!.delete_!
         Unblock & RedirectTo(reloadAfterDelete)})) &
    "name=no" #> ((b: NodeSeq) => <button onclick={Unblock.toJsCmd}>{b}</button>)
    ).apply(in)
  }

}
