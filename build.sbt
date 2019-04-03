name := "monad-precedence"

version := "0.1"

scalaVersion := "2.12.8"
resolvers ++= Seq(
  Resolver.sonatypeRepo("releases"),
  Resolver.sonatypeRepo("snapshots")
)
libraryDependencies += "org.typelevel" %% "cats-core" % "1.6.0"
libraryDependencies ++= Seq(
  "com.chuusai" %% "shapeless" % "2.3.3"
)
resolvers += Resolver.sonatypeRepo("releases")
addCompilerPlugin("org.spire-math" %% "kind-projector" % "0.9.9")