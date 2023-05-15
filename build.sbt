ThisBuild / scalaVersion := "2.13.10"

ThisBuild / version := "1.0-SNAPSHOT"

lazy val root = (project in file("."))
  .enablePlugins(PlayScala)
  .settings(
    name := """TOTsystemsTestTask""",
    libraryDependencies ++= Seq(
      guice,
      "org.scalatestplus.play" %% "scalatestplus-play" % "5.1.0" % Test
    )
  )

libraryDependencies += "org.xerial" % "sqlite-jdbc" % "3.41.2.1"
libraryDependencies += "com.typesafe.slick" %% "slick" % "3.4.1"
libraryDependencies ++= Seq("org.flywaydb" %% "flyway-play" % "7.25.0")
libraryDependencies += "com.lihaoyi" %% "requests" % "0.8.0"