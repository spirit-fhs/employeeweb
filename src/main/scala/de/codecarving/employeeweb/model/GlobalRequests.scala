package de.codecarving.employeeweb
package model

import net.liftweb.common.{Box, Empty}
import records._
import net.liftweb.http.{LiftResponse, SessionVar, RequestVar}
import spiritrecord.SpiritRecord

trait GlobalRequests {

  /**
   * ChartChooser is necessary for generating different Chart types.
   */
  object ChartChooser extends RequestVar[String]("")

  /**
   * In order to create delete links, it is necessary to have a SpiritRecord as a RequestVar.
   */
  object CurrentSpiritRecord extends RequestVar[Box[SpiritRecord[_]]](Empty)

  /**
   * Needed for creating downloadable files for Users.
   */
  object CurrentDownload extends RequestVar[Box[LiftResponse]](Empty)

  /**
   * A List of TalkAllocatorTalks which can be pushed from snippet <-> snippet.
   */
  object CurrentTalkAllocatorTalks extends RequestVar[Box[List[SpiritTalkAllocatorTalks]]](Empty)

  /**
   * A TalkAllocatorTalk which can be pushed from snippet <-> snippet.
   */
  object CurrentTalkAllocatorTalk extends RequestVar[Box[SpiritTalkAllocatorTalks]](Empty)

  /**
   * The current TalkAllocator which is used for editing.
   */
  object CurrentTalkAllocator extends SessionVar[Box[SpiritTalkAllocator]](Empty)

  /**
   * Creating a new TalkAllocator and having it in a SessionVar in order to
   * allocate Talks to that TalkAllocator.
   */
  object NewTalkAllocator extends SessionVar[Box[SpiritTalkAllocator]](Empty)

  /**
   * Getting the PollAnswers for a current Poll in order to display or evaluate them.
   */
  object CurrentPollAnswers extends RequestVar[Box[SpiritPollAnswers]](Empty)

  /**
   * A Poll either for editing or evaluation.
   */
  object CurrentPoll extends RequestVar[Box[SpiritPoll]](Empty)

  /**
   * An Entry which can be edited and passed from snippet <-> snippet.
   */
  object CurrentEntry extends RequestVar[Box[SpiritEntry]](Empty)
}
