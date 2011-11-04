name := "EmployeeWeb"

version := "1.0"

organization := "de.codecarving"

scalaVersion := "2.9.1"

resolvers ++= Seq(
  "Scala Tools Snapshot" at "http://scala-tools.org/repo-releases/",
  "Java.net Maven2 Repository" at "http://download.java.net/maven/2/"
)

seq(webSettings :_*)

// if you have issues pulling dependencies from the scala-tools repositories (checksums don't match), you can disable checksums
//checksums := Nil

libraryDependencies ++= {
  val liftVersion = "2.4-M4" // Put the current/latest lift version here
  Seq(
    "net.liftweb" %% "lift-json" % liftVersion % "compile->default" withSources(),
    "net.liftweb" %% "lift-textile" % liftVersion % "compile->default" withSources(),
    "net.liftweb" %% "lift-util" % liftVersion % "compile->default" withSources(),
    "net.liftweb" %% "lift-webkit" % liftVersion % "compile->default" withSources(),
    "net.liftweb" %% "lift-mapper" % liftVersion % "compile->default" withSources(),
    "net.liftweb" %% "lift-mongodb" % liftVersion % "compile->default" withSources,
    "net.liftweb" %% "lift-mongodb-record" % liftVersion % "compile->default" withSources,
    "net.liftweb" %% "lift-record" % liftVersion % "compile->default" withSources()
  )  
}

// when using the sbt web app plugin 0.2.4+, use "container" instead of "jetty" for the context
// Customize any further dependencies as desired
libraryDependencies ++= Seq(
  "net.databinder" %% "dispatch-http" % "0.8.6" % "compile->default",
  "com.googlecode.charts4j" % "charts4j" % "1.3" % "compile->default" withSources(),
  //"org.eclipse.jetty" % "jetty-webapp" % "8.0.4.v20111024" % "container", // For Jetty 8
  "org.mortbay.jetty" % "jetty" % "6.1.22" % "container,test", // For Jetty 6, add scope test to make jetty avl. for tests
  "org.scala-tools.testing" % "specs_2.9.0" % "1.6.8" % "test", // For specs.org tests
  "junit" % "junit" % "4.8" % "test->default", // For JUnit 4 testing
  "javax.servlet" % "servlet-api" % "2.5" % "provided->default",
  "com.h2database" % "h2" % "1.2.138", // In-process database, useful for development systems
  "ch.qos.logback" % "logback-classic" % "0.9.26" % "compile->default" // Logging
)
