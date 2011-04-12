import sbt._

class Plugins(info: ProjectInfo) extends PluginDefinition(info) {

  val akkaRepo = "Akka Repo" at "http://akka.io/repository"
  val akkaPlugin = "se.scalablesolutions.akka" % "akka-sbt-plugin" % "1.0"

  //tell sbt where to find the repo
  val repo = "Christoph's Maven Repo" at "http://maven.henkelmann.eu/"
  //include the junit xml listener 
  val junitXml = "eu.henkelmann" % "junit_xml_listener" % "0.2"

}
