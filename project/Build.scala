import sbt._
import Keys._

import scala.language.implicitConversions

object RulesBuild extends Build {

  implicit def dependsSeq(projects : Seq[Project])
                         (implicit c : Project => ClasspathDep[ProjectReference]) :
                         Seq[ClasspathDep[ProjectReference]] = projects.map(a => c(a))

  lazy val sharedSettings = Seq(
    organization                   := "com.typesafe",
    version                        := "0.1-SNAPSHOT",
    scalaVersion                   := "2.11.1",
    scalacOptions                 ++= Seq("-deprecation", "-feature"),
    scalaSource in Test           <<= (baseDirectory in Test)(_ / "test"),
    scalaSource in Compile        <<= (baseDirectory in Compile)(_ / "src"),
    resourceDirectory in Compile  <<= (baseDirectory in Compile)(_ / "resources"),
    publishArtifact in Test        := false,
    libraryDependencies          <++= (scalaVersion)(scalaVersion => Seq(
      "com.typesafe" %% "abide" % "0.1-SNAPSHOT",
      "org.scala-lang" % "scala-compiler" % scalaVersion % "provided",
      "org.scala-lang" % "scala-reflect" % scalaVersion % "provided"
    ))
  )

  lazy val samples = Project("abide-samples", file("samples"))
    .settings(sharedSettings : _*)

  lazy val rules = Seq(samples)

  lazy val tests = Project("tests", file("tests"))
    .settings(sharedSettings : _*)
    .settings(
      parallelExecution in Test  := false,
      scalaSource in Test       <<= (baseDirectory in Test)(_ / "test"),
      resourceDirectory in Test <<= (baseDirectory in Test)(_ / "resources"),
      libraryDependencies        += "org.scalatest" %% "scalatest" % "2.1.7" % "test",
      testOptions in Test        += Tests.Argument("-oF"),
      packagedArtifacts          := Map.empty
    ).dependsOn(rules : _*)

  lazy val root = rules.foldLeft {
    Project("root", file("."))
      .settings(
        test in Test      := (test in tests in Test).value,
        packagedArtifacts := Map.empty
      )
    } ((project, rule) => project.aggregate(rule))
}
