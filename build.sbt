name := "monad-liberator"

scalaVersion := "2.12.8"
resolvers ++= Seq(
  Resolver.sonatypeRepo("releases"),
  Resolver.sonatypeRepo("snapshots")
)
libraryDependencies += "org.typelevel" %% "cats-core" % "1.6.0"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.5" % "test"

addCompilerPlugin("org.spire-math" %% "kind-projector" % "0.9.9")

crossScalaVersions := Seq("2.11.11", "2.12.5")

// Use normal Scala 2.11/2.12 versioning after sonatype maven repo is setup
version := {
  val versionPrefix = CrossVersion.partialVersion(scalaVersion.value) match {
    case Some((2, 11)) => "1"
    case _ => "2"
  }
  s"0.$versionPrefix.0"
}

ghreleaseRepoOrg := "QuarpT"
ghreleaseRepoName := "monad-liberator"
