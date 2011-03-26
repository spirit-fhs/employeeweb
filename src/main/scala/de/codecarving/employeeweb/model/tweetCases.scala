package de.codecarving.employeeweb
package model

/**
 * Case classes which are created if Spitter is used for twittering.
 */
object tweetCases {

  /**
   * TweetNews is for Twittering a created Entry.
   */
  case class TweetNews(subject: String, semester: String, number: String)
  //TODO Add case classes for TalkAllocator, PollPal and more?!
}
