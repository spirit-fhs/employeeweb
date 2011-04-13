package de.codecarving.employeeweb
package snippet
package news
package snippet

import net.liftweb.http._
import S._
import js._
import JsCmds._
import JE._
import net.liftweb.common._
import net.liftweb.util._
import Helpers._
import scala.xml._
import net.liftweb.textile._

/**
 * Creating a preview of the Entry which is currently typed by a User.
 */
trait EntryPreview {

  object jsonPreview extends JsonHandler {
    def apply(in: Any): JsCmd =
      SetHtml("news_preview", in match {
        case JsonCmd("preview", _, p: String, _) =>
          TextileParser.toHtml(p, Empty)
        case x => <b>Oops.... {x}</b>
      })
  }

  /**
   * Creating a Preview of the text in news_id_field.
   * This will help in order to inject the Preview into a
   * JQuery Modal Dialog.
   */
  def mkPreview(in: NodeSeq): NodeSeq = {
    bind("json", in,
      "script" -> Script(jsonPreview.jsCmd),
      AttrBindParam("onclick", Text(jsonPreview.call("preview", ElemById("news_id")~>Value).toJsCmd), "onclick"))
  }

  /**
   * Creates the Preview Button.
   */
  def createPreviewButton = {
    <span class="lift:WriteNews.mkPreview">
      <json:script></json:script>
      <div id="dialog" title="Vorschau">
      <div id="news_preview"></div></div>
    <button json:onclick="onclick" id="onclick">Vorschau</button></span>
  }                                                                                            }
