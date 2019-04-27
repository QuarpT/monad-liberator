package monad.liberator.examples

import monad.liberator._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.Try

// The default precedence is Future[_] :>>: Seq[_] :>>: EitherM[_] :>>: Try[_] :>>: Option[_]
// All monads must be defined as part of the implicit monad precedence order.
// It is possible to provide your own monad precedence, see ExampleChangingPrecedence

object BasicExample {
  val monadLiberator = new MonadLiberator[String]
  import monadLiberator._

  val result: Future[Seq[Option[Int]]] = for {
    a <- List(1, 2).M
    b <- List(None, Some(2)).M
    c <- Future(1).M
  } yield a * b + c

  // Returns Future(Seq(None, Some(3), None, Some(5)))
}

object DeepFlattenExample {
  val monadLiberator = new MonadLiberator[String]
  import monadLiberator._

  val result: Future[Seq[Either[String, Option[Int]]]] =
    DeepFlattenTraverse(Some(Future(Future(Right(Some(Some(List(5))))))))
  // Returns Future(Seq(Right(Some(5)))
}

object DeepMapExample {
  val monadLiberator = new MonadLiberator[String]
  import monadLiberator._

  val result: Future[Seq[Either[String, Option[Int]]]] = Future(List(Right(Some(5)))).deepMap(_ * 2)
  // Returns Future(Seq(Right(Some(10)))
}

// A more complex example
object ComplexExample {
  val monadLiberator = new MonadLiberator[String]
  import monadLiberator._

  val result: Future[Seq[Either[String, Option[Int]]]] = for {
    a <- Some(Future(Right(List(1, 2)))).M
    b <- Some(Some(2)).M
    c <- (a + b).M
    d <- (if (c % 2 == 0) Right(c) else Left(s"$c is not even")).M
  } yield d

  // Returns Future(Seq(Left("3 is not even"), Right(Some(4))))
  // Currently the library does not support _ = _ in for comprehension. Instead use .M and flatMap
}

// Using a custom precedence (the order nesting monads) is possible:
// In this example I put List after Option
// Ensure that there is only a single precedence type defined in the implicit scope
object ExampleChangingPrecedence {
  // With MonadLiberatorCustomPrecedence[CustomPrecedenceType]
  // if you want to add Eithers to the precedence, currently you'll need to define a type alias with a fixed left type.
  // MonadPreprocessorWithEitherImplicits[EitherLeftType] already provides this for you.
  type Either[A] = scala.Either[String, A]

  // All types must require an implemented Cats Traverse and Monad type class
  // The left most type, (Future) only needs a Monad, not Traversable
  // It would not be possible to put Future to the right of another type, since it is not Traversable
  //    (you would need to provide your own blocking implementation of cats Traverse for Future if you require this).
  type CustomPrecedenceType = Future[_] :>>: Try[_] :>>: Either[String] :>>: Option[_] :>>: Seq[_] :>>: PNil
  val monadLiberator = new MonadLiberatorCustomPrecedence[CustomPrecedenceType] {}

  import monadLiberator._

  val result: Future[Option[Seq[Int]]] = for {
    a <- List(1, 2).M
    b <- List(None, Some(2)).M
    c <- Future(1).M
  } yield a * b + c

  // Returns Future(None) since we have overridden the nesting order so that Options come before lists
}
