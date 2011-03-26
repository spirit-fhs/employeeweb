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
import spiritrecord.SpiritRecord
import xml.{Elem, NodeSeq, Text}

trait blockUI extends GlobalRequests {

  //TODO Always redirect to the page the User came from!
  object reloadAfterDelete extends RequestVar[String]("/index")

  /**
   * Creating a delete link for any Record which inherits SpiritRecord
   * with a Modal Dialog acting as a delete confirmation.
   * @param in T <: SpiritRecord.
   * @return The generated Link.
   */
  def deleteLink[T <: SpiritRecord[T]](in: T): Elem = {
    SHtml.a(() => { CurrentSpiritRecord(Full(in));
             S.runTemplate(List("_delete_template")).
             map(ns => ModalDialog(ns)) openOr
             Alert("Couldn't find _delete_template")}, Text("Löschen"))
  }

  /**
   * Creating the confirm Buttons for the _delete_template.html which is
   * called when triggering the deletion link created with deleteLink.
   * @todo Reload last page when confim the deletion with Yes.
   */
  def confirmdelete(in: NodeSeq) = {
    ("name=yes" #> ((b: NodeSeq) => ajaxButton(b, () => {
         CurrentSpiritRecord.open_!.delete_!
         Unblock & RedirectTo(reloadAfterDelete)})) &
    "name=no" #> ((b: NodeSeq) => <button onclick={Unblock.toJsCmd}>{b}</button>)
    )(in)
  }

}
