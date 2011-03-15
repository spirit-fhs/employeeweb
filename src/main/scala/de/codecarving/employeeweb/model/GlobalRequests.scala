package de.codecarving.employeeweb
package model

import net.liftweb.http.RequestVar
import net.liftweb.common.{Box, Empty}

trait GlobalRequests {

  object CurrentPollAnswers extends RequestVar[Box[SpiritPollAnswers]](Empty)
  object CurrentPoll extends RequestVar[Box[SpiritPoll]](Empty)
  object CurrentEntry extends RequestVar[Box[SpiritEntry]](Empty)
}
