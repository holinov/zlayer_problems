import Versions._
import sbt.Keys._

lazy val zio_playground = (project in file("."))
  .settings(
    addCompilerPlugin("com.olegpy" %% "better-monadic-for" % "0.3.1"),
    scalaVersion := "2.12.10",
    organization := "org.fruttech",
    name := "zio_playground",
    version := "0.0.1",
    licenses += ("MIT", url("http://opensource.org/licenses/MIT")),
    maxErrors := 3,
    testFrameworks := Seq(new TestFramework("zio.test.sbt.ZTestFramework")),
    scalacOptions ++= Seq("-Ywarn-unused", "-Yrangepos"),
    scalacOptions in console --= Seq("-Xfatal-warnings"),
    PB.targets in Compile := Seq(scalapb.gen() -> (sourceManaged in Compile).value),
    resolvers ++= Seq(
        Resolver.bintrayRepo("holinov", "maven"),
        Resolver.mavenLocal,
        Resolver.sonatypeRepo("releases"),
        Resolver.sonatypeRepo("snapshots")
      ),
    libraryDependencies ++= Seq(
        // ZIO
        "dev.zio" %% "zio"          % ZioVersion,
        "dev.zio" %% "zio-streams"  % ZioVersion,
        "dev.zio" %% "zio-test"     % ZioVersion % "test",
        "dev.zio" %% "zio-test-sbt" % ZioVersion % "test"
      )
  )
