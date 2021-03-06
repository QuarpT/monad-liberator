name := "monad-liberator"

version := "1.0.0"

scalaVersion := "2.12.8"
resolvers ++= Seq(
  Resolver.sonatypeRepo("releases"),
  Resolver.sonatypeRepo("snapshots")
)
libraryDependencies += "org.typelevel" %% "cats-core" % "1.6.0"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.5" % "test"

addCompilerPlugin("org.spire-math" %% "kind-projector" % "0.9.9")

crossScalaVersions := Seq("2.11.11", "2.12.8")

licenses += ("Apache-2.0", url("https://opensource.org/licenses/Apache-2.0"))
