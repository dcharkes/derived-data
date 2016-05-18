name := "Weblab-SubmissionTree-i3QL"

version := "1.0"

libraryDependencies in ThisBuild ++= Seq(
  "de.tud.cs.st" %% "idb-syntax-iql" % "latest.integration",
  "de.tud.cs.st" %% "idb-runtime" % "latest.integration"
)

scalaVersion in ThisBuild := "2.10.2"
scalaOrganization in ThisBuild := "org.scala-lang.virtualized"
