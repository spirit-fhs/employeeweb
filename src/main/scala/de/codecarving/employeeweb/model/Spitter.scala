package de.codecarving.employeeweb
package model

import dispatch._
import twitter._
import oauth._
import scala.actors._
import Http._
import net.liftweb.common.Loggable
import net.liftweb.util.Props
import tweetCases._

object Spitter extends Actor with Loggable {
  private val propsPrefix = "spirit.employeeweb.spitter."
  private val active = Props.get(propsPrefix + "Active", "") == "true"
  private val consumer = new Consumer(Props.get(propsPrefix + "Consumer", ""), Props.get(propsPrefix + "ConsumerSecret", ""))
  private val token = new Token(Props.get(propsPrefix + "Token", ""), Props.get(propsPrefix + "TokenSecret", ""))

  private def mkTweet(subject: String, tinyurl: String, semester: String) = {
    val tailWithoutSemester = " " + tinyurl
    val tailSemester = tailWithoutSemester + " " + semester
    val tail =
      if (tailSemester.length > 130) tailWithoutSemester
      else tailSemester
    val maxlen = 140 - tail.length
    val shortSubject =
      if (subject.length <= maxlen) subject
      else subject.slice(0, maxlen-3)+"..."
    shortSubject + tail
  }

  def act {
    loop {
      react {
          case TweetNews(subject,semester,nr) =>
            try {
              val http = new Http
              val longUrl = "http://is.gd/api.php?longurl=http://spirit.fh-schmalkalden.de/entry/" + nr
              val tinyurl = http(longUrl.as_str)
              if(active) {
                http(Status.update(mkTweet(subject, tinyurl, semester), consumer, token).>|)
              } else {
                logger warn "Spitter is not active, Tweet will be logged."
                logger warn mkTweet(subject, tinyurl, semester)
              }
            } catch {
              case e =>
                logger error e.toString
            }

          case _ =>
            logger error "This has not been implemented yet, see tweetCases for possible Tweets!"

      }
    }
  }
}
