# Monad Liberator

Liberate your monads

## Overview

Monad Liberator is a library for mixing multiple monad and nested monads types in Scala `for` comprehensions using customisable implicit rules of precedence.

The library also supports deep flattening / deep traversing / deep mapping nested monads

## Install

Requires Scala 2.12 or 2.11

```sbtshell
resolvers += Resolver.bintrayRepo("quarpt", "maven")
libraryDependencies += "monad-liberator" %% "monad-liberator" % "0.2.0"
```

## Examples

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

See [examples](src/main/scala/monad/liberator/examples/Examples.scala) for more detailed information and context.

## Implementation details

The default implicit precedence is 
```
Future[_] > Seq[_] > EitherM[_] > Try[_] > Option[_]
```
This precedence means for the types output, Future will always be outside Sequences, which will be outside Eithers etc...
All types must have a `Cats` Monad implementation, and all types except for the leftmost, must have a `Cats` Traverse implementation on the implicit scope.
Mixing in the `MonadLiberator` trait provides the default `Cats` implicits.

The library builds type class instances to 'deep map', 'deep traverse' and 'deep flatten' nested monads using the precedence rule defined implicitly.

It is possible to provide your own precedence, see the [examples](src/main/scala/monad/liberator/examples/Examples.scala).

The library is experimental and still being developed.

## Contributors

- Peter Colley (zvvvvt@gmail.com)

Comments and contributions are welcome. 
