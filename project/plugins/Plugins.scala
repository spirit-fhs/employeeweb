import sbt._
class Plugins(info: ProjectInfo) extends PluginDefinition(info)
{
  //tell sbt where to find the repo
  val repo = "Christoph's Maven Repo" at "http://maven.henkelmann.eu/"
  //include the junit xml listener 
  val junitXml = "eu.henkelmann" % "junit_xml_listener" % "0.2"
}
