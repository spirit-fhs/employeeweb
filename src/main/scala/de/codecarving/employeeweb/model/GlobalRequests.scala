package de.codecarving.employeeweb
package model

import net.liftweb.common.{Box, Empty}
import records._
import net.liftweb.http.{LiftResponse, SessionVar, RequestVar}

trait GlobalRequests {

  /**
   * ChartChooser is necessary for generating different Chart types.
   */
  object ChartChooser extends RequestVar[String]("")

  object CurrentDownload extends RequestVar[Box[LiftResponse]](Empty)
  object CurrentTalkAllocatorTalks extends RequestVar[Box[List[SpiritTalkAllocatorTalks]]](Empty)
  object CurrentTalkAllocatorTalk extends RequestVar[Box[SpiritTalkAllocatorTalks]](Empty)
  object CurrentTalkAllocator extends SessionVar[Box[SpiritTalkAllocator]](Empty)
  object NewTalkAllocator extends SessionVar[Box[SpiritTalkAllocator]](Empty)
  object CurrentPollAnswers extends RequestVar[Box[SpiritPollAnswers]](Empty)
  object CurrentPoll extends RequestVar[Box[SpiritPoll]](Empty)
  object CurrentEntry extends RequestVar[Box[SpiritEntry]](Empty)
}
