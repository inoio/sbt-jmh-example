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
  .settings(
    libraryDependencies ++= Seq(
      "com.typesafe.akka"           %% "akka-slf4j"     % "2.5.13",
      "com.typesafe.scala-logging"  %% "scala-logging"  % "3.9.0",
      "org.log4s"                   %% "log4s"          % "1.6.1",
      "org.slf4j"                    % "slf4j-simple"   % "1.7.25"
    )
  )

addCommandAlias("gnext", ";groll next")
