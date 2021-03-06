package de.codecarving.employeeweb
package model

import java.text.SimpleDateFormat
import java.util.Date

import annotation.tailrec
import records._
import net.liftweb.util.Props
import net.liftweb.common.Full._
import net.liftweb.http.StreamingResponse._
import net.liftweb.http.{StreamingResponse, LiftResponse, S}
import net.liftweb.common.{Full, Box}

trait SpiritHelpers {

  lazy val mailerActive = Props.get("spirit.employeeweb.mailer.active", "") == "true"

  /**
   * Checks the input if it is a Valid date or not.
   * If it is not valid or in the past, the entry will be valid for 14 Days.
   * @param date the input date from a User
   * @return String a correct date
   */
  def dateValidator(date: String): String = {
    val checkDate = new SimpleDateFormat("dd.MM.yyyy")
    checkDate setLenient false
    val exceptionDate = new Date

    try {
      checkDate parse date
      if(checkDate.parse(date).after(exceptionDate)){
        date
      } else {
        val returnDate = checkDate.format(exceptionDate.getTime + 1209600000)
        S warning "Das gewählte Datum war heute oder in der Vergangenheit, es wurden automatisch 14 Tage gesetzt!"
        returnDate
      }

      } catch {
          case pe: java.text.ParseException => pe
            val returnDate = checkDate.format(exceptionDate.getTime + 1209600000)
            S warning "Das gewählte Datum war heute oder in der Vergangenheit, es wurden automatisch 14 Tage gesetzt!"
            returnDate
      }
  }

  /**
   * Extracts random fields from a List of any Type.
   * If return size is bigger than in.length, Nil is returned.
   * @param count How many fields should be returned.
   * @param in From this List the fields will be extracted.
   * @return List[T]
   */
  def randomFromList[T](count: Int, in: List[T]): List[T] = {
    if(count > in.length) return Nil
    @tailrec
    def randomFromList[T](count: Int, in: List[T], result: List[T]): List[T] = {
      count match {
        case 0 => result
        case _ =>
          val c = in((new util.Random).nextInt(in.length))
          randomFromList(count - 1, in.filterNot(_ == c), c :: result)
      }
    }
    randomFromList(count, in, Nil)
  }

  /**
   * Removes all duplicated Answers. If a User has put duplicated Answers in the
   * Input fields, this method will help out!
   * @param in Set[SpiritPollAnswers].
   * @return A clean Set without duplicated Answers.
   */
  def removeDuplicatesfromanswerSet(in: Set[SpiritPollAnswers]): Set[SpiritPollAnswers] = {
    // A Tuple of our Result Set and a Akku Set with Strings
    in./:((Set[SpiritPollAnswers](), Set[String]())) {(tuple, input) =>
      if (tuple._2(input.answer.value)) tuple
      else (tuple._1 + input, tuple._2 + input.answer.value)
    }._1
  }

  /**
   * Generating the headers for the return4Download method.
   */
  private def headers(in: Array[Byte], filename: String) = {
      ("Content-type" -> "application/csv") ::
      ("Content-length" -> in.length.toString) ::
      ("Content-disposition" -> ("attachment; filename=" + filename.replaceAll(" ","_") + ".csv")) :: Nil
  }

  /**
   * For the creation of download links of generated files this method sets
   * a Box[LiftResponse] which will return a Array[Byte] to the User which
   * requests a file.
   * @param in The file as an Array[Byte].
   * @param filename The filename which will be viewed to the User.
   * @return The LiftResponse which will be returned to the User.
   */
  def return4Download(in: Array[Byte], filename: String): Box[LiftResponse] = {
    Full(StreamingResponse(
      new java.io.ByteArrayInputStream(in),
      () => {},
      in.length,
      headers(in, filename), Nil, 200)
    )
  }

  /**
   * Creating an Elem which will be displayed next to Input Areas.
   */
  def createTextileTooltip = {
    <div>
      <table>
        <tr>
          <td style="border:0">
          <strong>Formatierung:</strong>
          <br />**bold text**
          <br />__italic text__
          <br />*_ bold italic text _*
          <br />{"%{color:red}Text in red %"}
          <br /><strong>Aufz&auml;hlung:</strong>
          <br />* bulleted list
          <br />
          <br />* bulleted list
          <br />** 2-level
          <br /><strong>Links:</strong>
          <br />"Link to FhS": http://www.fh-schmalkalden.de
          </td>
        </tr>
      </table>
    </div>
  }

}
