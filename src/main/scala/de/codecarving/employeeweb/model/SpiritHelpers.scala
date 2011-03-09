package de.codecarving.employeeweb
package model

import java.text.SimpleDateFormat
import java.util.Date

import net.liftweb.http.S

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
}
