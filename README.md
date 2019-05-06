# Monad Liberator

This was written to experiment and play with some type level programming in Scala. Not for use in prod.

## Overview

Monad Liberator is a library for mixing multiple monad and nested monads types in Scala `for` comprehensions.
Nested monad types are reordered and flattened automatically based on customisable implicit ordering rules.

It could be seen as an alternative to monad transformers.

## Install

Requires Scala 2.12 or 2.11

```sbtshell
resolvers += Resolver.bintrayRepo("quarpt", "maven")
libraryDependencies += "monad-liberator" %% "monad-liberator" % "1.0.0"
```

## Basic Examples

### For comprehension example

```scala
import monad.liberator._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

val monadLiberator = new MonadLiberator[String]
import monadLiberator._

val result: Future[Seq[Option[Int]]] = for {
  a <- List(1, 2).M
  b <- List(None, Some(2)).M
  c <- Future(1).M
} yield a * b + c

// Returns Future(Seq(None, Some(3), None, Some(5)))
```

### Deep Flatten Traverse example

```scala
val monadLiberator = new MonadLiberator[String]
import monadLiberator._

val result = DeepFlattenTraverse(Some(Future(Future(Right(Some(Some(List(5))))))))
// Returns Future(List(Right(Some(5)))
```

