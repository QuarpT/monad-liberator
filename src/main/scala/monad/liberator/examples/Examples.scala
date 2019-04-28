package monad.liberator.examples

import monad.liberator._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.Try

// The default monad precedence is Future[_] :>>: Seq[_] :>>: EitherM[_] :>>: Try[_] :>>: Option[_]
// All monads must be defined as part of the implicit monad precedence order.
// It is possible to provide your own monad precedence, see ExampleChangingPrecedence

// MonadTypeWitnesses are used to convert subclasses of Seq to Seq for precedence ordering
// See ExampleChangingPrecedence for creating your own MonadTypeWitnesses

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
    DeepFlattenTraverse(Vector(Some(Future(Future(Right(Some(List(5))))))))
  // Returns Future(Seq(Right(Some(5)))
}

object DeepMapExample {
  val monadLiberator = new MonadLiberator[String]
  import monadLiberator._

  val result: Future[Seq[Either[String, Option[Int]]]] = Future(List(Right(Some(5)))).deepMap(_ * 2)
  // Returns Future(Seq(Right(Some(10)))
}

// A more complex example
// MonadTypeWitnesses are used to convert subclasses of Either to EitherM[LeftType]
// (EitherM is an either type alias with a fixed left type)

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


object ExampleChangingPrecedence {

  // Copy and paste MonadLiberator and add your own MonadTypeWitnesses and implicit precedence object
  // MonadTypeWitnesses are used to find Monad types from subclasses for implicits.
  // It would be possible to convert Monad types using a MonadTypeWitness, e.g. convert Try monads to Future monads.
  class CustomMonadLiberator[EitherLeftType] extends MonadLiberatorMixin {
    implicit def seqMonadTypeWitness[F, A](implicit ev: F <:< Seq[A]) = new MonadTypeWitness[F, Seq[A]] {
      override def apply(a: F): Seq[A] = a
    }

    type EitherM[R] = Either[EitherLeftType, R]
    implicit def eitherMonadTypeWitness[F, A](implicit ev: F <:< EitherM[A]) = new MonadTypeWitness[F, EitherM[A]] {
      override def apply(a: F): EitherM[A] = a
    }

    // All types require an implemented Cats Traverse and Monad type class
    // except the left most type, (Future in this case) does not need to be Traversable.
    // It would not be possible to put Future to the right of another type, since it is not Traversable
    //    (you would need to provide your own blocking implementation of cats Traverse for Future if you require this).

    implicit val monadPrecedence = new Precedence[Future[_] :>>: Seq[_] :>>: Option[_] :>>: EitherM[_] :>>: Try[_] :>>: PNil]
  }

  val monadLiberator = new CustomMonadLiberator[String]
  // Ensure that there is only a single precedence type defined in the implicit scope
  import monadLiberator._

  val result: Future[Seq[Option[Either[String, Int]]]] = for {
    a <- List(Right(1), Left("hi")).M
    b <- List(Some(2)).M
    c <- Future(3).M
  } yield a + b + c

  // Returns Future(Seq(Some(Right(6)), Some(Left(hi)))) since we have given Options a higher precedence than Eithers

}
