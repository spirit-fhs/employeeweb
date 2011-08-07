package de.codecarving.employeeweb.persistence.rest

import java.util.concurrent.ConcurrentHashMap
import net.liftweb.common.{Empty, Box, Full}
import dispatch._

/**
 * Object for configuration of the RESTful DB-Service.
 */
object RESTfulDB {

  /*
   * Defining the HOST of RESTful DB-Service with credentials.
   * @param host
   * @param port
   * @param username
   * @param password
   */
  def defineDbAuth(host: String, port: String, username: String, password: String) {
    config.put(CREDS, (username, password))
    config.put(HOST, (host, port))
  }

  /**
   * Returns complete URL with Port for RESTful DB-Service.
   * @return Box[String]
   * @todo Remove hardcoded /fhs-spirit/ and use proper solution.
   */
  def getURL(): Box[String] = config.get(HOST) match {
    case null => Empty
    case host => Full(host._1 + ":" + host._2 + "/fhs-spirit/")
  }

  /*
   * Return DB-Service HOST.
   * @return Box[(Host, Port)]
   */
  def getHost(): Box[(String, String)] = config.get(HOST) match {
    case null => Empty
    case host => Full(host._1, host._2)
  }

  /**
   * Return credentials in order to get AUTH at the RESTful DB-Service.
   * @return Box[(Username, Password)]
   */
  def getCredentials(): Box[(String, String)] = config.get(CREDS) match {
    case null => Empty
    case host => Full(host._1, host._2)
  }

  /**
   * Testing credentials.
   * @return Boolean if authenticated or not.
   * @todo Provide better case then "yeah!", return code by DB-Service should be a little better!
   */
  def isAuthenticated(): Boolean = {
    val creds = config.get(CREDS)
    val auth = new Request(getURL().openTheBox + "protected")
    val h = new Https
    val answer = h(auth as(creds._1,creds._2) as_str)

    answer match {
      case answer if (answer.contains("yeah!")) => true
      case _ => false
    }
  }

  /**
   * HashMap for configuration of the RESTful DB-Serivce.
   */
  private val config = new ConcurrentHashMap[String, (String, String)]

  private val CREDS = "credentials"
  private val HOST = "hostname"

}
