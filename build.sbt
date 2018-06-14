// build.sbt

ThisBuild / scalaVersion := "2.12.6"
ThisBuild / name := "sbt-jmh-example"
ThisBuild / organization := "io.ino"

lazy val core = (project in file("core"))
  .settings(
    fork in run := true,
    // javaOptions in run += "-XX:+PrintCompilation",
    // send output to the build's standard output and error, prevent buffering in order to immediately see print statements
    outputStrategy := Some(StdoutOutput)
  )

lazy val bench = (project in file("bench"))
  .enablePlugins(JmhPlugin)

addCommandAlias("gnext", ";groll next")
