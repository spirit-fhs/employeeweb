import sbt._
import eu.henkelmann.sbt.JUnitXmlTestsListener

class LiftProject(info: ProjectInfo) extends DefaultWebProject(info) with AkkaProject {
  val liftVersion = "2.3"

  override def compileOptions = super.compileOptions ++ Seq(Unchecked)

  val scalaToolsSnapshots = "Scala Tools Snapshots" at "http://scala-tools.org/repo-snapshots/"

  //create a listener that writes to the normal output directory
  def junitXmlListener: TestReportListener = new JUnitXmlTestsListener(outputPath.toString)

  //add the new listener to the already configured ones
  override def testListeners: Seq[TestReportListener] = super.testListeners ++ Seq(junitXmlListener)

  override def libraryDependencies = Set(
    "net.databinder" %% "dispatch" % "0.8.1",
    "com.googlecode.charts4j" % "charts4j" % "1.3" % "compile->default" withSources(),
    "net.liftweb" %% "lift-json" % liftVersion % "compile->default" withSources(),
    "net.liftweb" %% "lift-textile" % liftVersion % "compile->default" withSources(),
    "net.liftweb" %% "lift-util" % liftVersion % "compile->default" withSources(),
    "net.liftweb" %% "lift-webkit" % liftVersion % "compile->default" withSources(),
    "net.liftweb" %% "lift-mapper" % liftVersion % "compile->default" withSources(),
    "net.liftweb" %% "lift-mongodb" % liftVersion % "compile->default" withSources,
    "net.liftweb" %% "lift-mongodb-record" % liftVersion % "compile->default" withSources,
    "net.liftweb" %% "lift-record" % liftVersion % "compile->default" withSources(),
    "com.h2database" % "h2" % "1.2.138",
    "org.mortbay.jetty" % "jetty" % "6.1.22" % "test->default",
    "junit" % "junit" % "4.5" % "test->default",
    "ch.qos.logback" % "logback-classic" % "0.9.26",
    "org.scala-tools.testing" %% "specs" % "1.6.7" % "test->default"
  ) ++ super.libraryDependencies

}
