package de.codecarving.employeeweb
package model

import java.text.SimpleDateFormat
import java.util.Date

import net.liftweb.http.S
import annotation.tailrec

trait SpiritHelpers {

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
        S warning "Ihr Beitrag verfaellt aumatisch in 14 Tagen, da das Datum in der Vergangenheit war!"
        returnDate
      }

      } catch {
          case pe: java.text.ParseException => pe
            val returnDate = checkDate.format(exceptionDate.getTime + 1209600000)
            S warning "Ihr Beitrag verfaellt aumatisch in 14 Tagen, da das Datum nicht gültig war!"
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
   * @param in Set[SpiritPollanswers]
   * @return A clean Set without duplicated Answers.
   */
  def removeDuplicatesfromanswerSet(in: Set[SpiritPollAnswers]): Set[SpiritPollAnswers] = {
    // A Tuple of our Result Set and a Akku Set with Strings
    in.foldLeft((Set[SpiritPollAnswers](), Set[String]())) {(tuple, input) =>
      if (tuple._2(input.answer.value)) tuple
      else (tuple._1 + input, tuple._2 + input.answer.value)
    }._1
  }

}
