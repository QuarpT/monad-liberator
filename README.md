# Monad Liberator

Liberate your monads

## Overview

Monad Liberator is a library for mixing multiple monad and nested monads types in Scala `for` comprehensions using customisable implicit rules of precedence.

The library also supports deep flattening / deep traversing / deep mapping nested monads

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

To speed up compile times and avoid precedence collisions, only `import monadLiberator._` in the scope where it is used.

### Deep Flatten Traverse example

```scala
val monadLiberator = new MonadLiberator[String]
import monadLiberator._

val result = DeepFlattenTraverse(Some(Future(Future(Right(Some(Some(List(5))))))))
// Returns Future(List(Right(Some(5)))
```

### Custom precedence example

```scala
import monad.liberator._
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Try

class CustomMonadLiberator[EitherLeftType] extends MonadLiberatorMixin {
  implicit def seqMonadTypeWitness[F, A](implicit ev: F <:< Seq[A]) = new MonadTypeWitness[F, Seq[A]] {
    override def apply(a: F): Seq[A] = a
  }

  type EitherM[R] = Either[EitherLeftType, R]
  implicit def eitherMonadTypeWitness[F, A](implicit ev: F <:< EitherM[A]) = new MonadTypeWitness[F, EitherM[A]] {
    override def apply(a: F): EitherM[A] = a
  }

  implicit val monadPrecedence = new Precedence[Future[_] :>>: Seq[_] :>>: Option[_] :>>: EitherM[_] :>>: Try[_] :>>: PNil]
}


val customMonadLiberator = new CustomMonadLiberator[String]
import customMonadLiberator._

val result: Future[Seq[Option[Either[String, Int]]]] = for {
  a <- List(Right(1), Left("hi")).M
  b <- List(Some(2)).M
  c <- Future(3).M
} yield a + b + c

// Returns Future(Seq(Some(Right(6)), Some(Left(hi)))) since we have given Options a higher precedence than Eithers in our custom precedence.
```
- Create a class extending MonadLiberatorMixin
- Define implicit monad type witnesses for your monad types.
- Define an implicit monad precedence type using the `:>>:` operator.
- Instantiate and import all from the instance.

See [examples](src/main/scala/monad/liberator/examples/Examples.scala) for more detailed information and context.

## Implementation details

The implicit precedence provided by the `MonadLiberator` class is:
```
Future[_] > Seq[_] > EitherM[_] > Try[_] > Option[_]
```
This precedence means for the types output, Future will always be outside Sequences, which will be outside Eithers etc...
All types must have a `Cats` Monad implementation, and all types except for the leftmost, must have a `Cats` Traverse implementation on the implicit scope.
The required `cats` implicits are provided by the library when importing all (`import monadLiberator._`) from the instantiated `MonadLiberator` object.

The library builds type class instances to 'deep map', 'deep traverse' and 'deep flatten' nested monads using the precedence rule defined implicitly.

### Monad Type Witnesses explained

MonadTypeWitnesses are used to find Monad types from Monad subclasses for implicits.

Default MonadTypeWitnesses are supplied by `MonadLiberatorMixin` for Future, Option and Try.

In the custom precedence example we use them for the EitherM type, an Either type alias with a fixed Left parameter, which we use in our custom monad precedence.
It would also be possible to convert Monad types using a MonadTypeWitness rule.

## Contributors

- Peter Colley (zvvvvt@gmail.com)

Comments and contributions are welcome.
