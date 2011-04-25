package de.codecarving.employeeweb
package lib

import javax.mail._
import javax.mail.internet._
import java.util.Properties
import net.liftweb.http.S
import net.liftweb.common.Loggable
import de.codecarving.fhsldap.model.User
import net.liftweb.util.Props

/**
 * Using SpiritMailer instead of the LiftMailer,
 * since this solution is easier to adopt and is
 * perfectly for the needs of the EmployeeWeb.
 */
object SpiritMailer extends Loggable {

  //TODO Fix footer for studweb if it goes online!
  private val footer =
    """<br><br><br>
      |----------------------------------------------------------<br>
      |Sent over FhS_Spirit!<br>
      |<a href="http://spirit.fh-schmalkalden.de/">
      |  http://spirit.fh-schmalkalden.de/</a><br>
      |Follow @<a href="http://www.twitter.com/fhs_spirit">
      |  FhS_Spirit</a> on Twitter<br>
      |Visit <a href=http://www.facebook.com/fhs.spirit>
      |  FhS Spirit</a> on Facebook<br>""".stripMargin

  private val mailerProps = new Properties
  mailerProps.setProperty("mail.transport.protocol", Props.get("mail.host.type", ""))
  mailerProps.setProperty("mail.host", Props.get("mail.smtp.host", ""))

  /**
   * Sending emails.
   * @param context eMail Body.
   * @param subject eMail subject.
   * @param adresses Destination email adresses.
   */
  def send(context: String, subject: String, adresses: Array[String]) {

    logger warn User.currentUserId.open_! + " is using the email function!"

    val mailSession = Session.getDefaultInstance(mailerProps, null)
    val transport = mailSession.getTransport

    val msg = new MimeMessage(mailSession)
    msg.setSubject(subject, "UTF-8")
    msg.setContent(context + footer, "text/html; charset=UTF-8")

    msg.addRecipients(Message.RecipientType.TO,
       adresses map { email =>
         logger warn "Sending to " + email
         new InternetAddress(email).asInstanceOf[javax.mail.Address]
       })

    msg.addRecipient(Message.RecipientType.CC,
      new InternetAddress(User.ldapAttributes.email.openOr(Props.get("spirit.default.email", ""))))

    msg.setFrom(new InternetAddress(User.ldapAttributes.email.openOr(Props.get("spirit.default.email", "")),
                 User.ldapAttributes.displayName.openOr("")))

    try {
      transport.connect
      transport.sendMessage(msg, msg.getRecipients(Message.RecipientType.TO))
      transport.sendMessage(msg, msg.getRecipients(Message.RecipientType.CC))
      transport.close
      logger warn "Mail was sent successfully!"
      S notice "eMail wurde gesendet!"
    } catch {
      case e =>
        logger warn "Mail was not sent correctly!"
        logger warn e.printStackTrace.toString
        S error "eMail konnte nicht versendet werden! "
    }
  }

}
