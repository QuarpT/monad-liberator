# Monad Liberator

Liberate your monads

## Overview

Monad Liberator is a library for mixing multiple monad and nested monads types in Scala `for` comprehensions using customisable implicit rules of precedence.

The library also supports deep flattening / deep traversing / deep mapping nested monads

## Install

Requires Scala 2.12 or 2.11

```sbtshell
resolvers += Resolver.bintrayRepo("quarpt", "maven")
libraryDependencies += "monad-liberator" %% "monad-liberator" % "0.1.0"
```

## Examples

### For comprehension example

```scala
val result: Future[List[Option[Int]]] = for {
  a <- List(1, 2).!?
  b <- List(None, Some(2)).!?
  c <- Future(1).!?
} yield a * b + c

// Returns Future(List(None, Some(3), None, Some(5)))
```

### Deep Flatten Traverse example

```scala
val result = DeepFlattenTraverse(Option(Future(Future(Right(Option(Option(List(5))))))))
// Returns Future(List(Right(Some(5)))
```

See [examples](src/main/scala/monad/liberator/examples/Examples.scala) for more information and context.

## Implementation details

The default implicit precedence is 
```
Future[_] > List[_] > Either[_] > Try[_] > Option[_]
```
This precedence means for the types output, Future will always be outside Lists, which will be outside Eithers etc...
All types must have a `Cats` Monad implementation, and all types except for the leftmost, must have a `Cats` Traverse implementation on the implicit scope.
Mixing in the `MonadLiberator` trait provides the default `Cats` implicits.

The library builds type class instances to 'deep map', 'deep traverse' and 'deep flatten' nested monads using the precedence rule defined implicitly.

It is possible to provide your own precedence, see the [examples](src/main/scala/monad/liberator/examples/Examples.scala).

The library is experimental and still being developed.

## Contributors

- Peter Colley (zvvvvt@gmail.com)

Comments and contributions are welcome. 
