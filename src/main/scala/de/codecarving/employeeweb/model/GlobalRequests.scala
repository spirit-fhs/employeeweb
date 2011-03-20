package de.codecarving.employeeweb
package model

import net.liftweb.common.{Box, Empty}
import records._
import net.liftweb.http.{SessionVar, RequestVar}

trait GlobalRequests {

  /**
   * ChartChooser is necessary for generating different Chart types.
   */
  object ChartChooser extends RequestVar[String]("")

  object CurrentTalkAllocator extends RequestVar[Box[SpiritTalkAllocator]](Empty)
  object NewTalkAllocator extends SessionVar[Box[SpiritTalkAllocator]](Empty)
  object CurrentPollAnswers extends RequestVar[Box[SpiritPollAnswers]](Empty)
  object CurrentPoll extends RequestVar[Box[SpiritPoll]](Empty)
  object CurrentEntry extends RequestVar[Box[SpiritEntry]](Empty)
}
