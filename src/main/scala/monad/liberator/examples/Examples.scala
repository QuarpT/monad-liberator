package monad.liberator.examples

import monad.liberator._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Await, Future}
import scala.concurrent.duration.Duration
import scala.util.Try

// The default precedence is Future[_] :>>: List[_] :>>: Either[_] :>>: Try[_] :>>:  Option[_]

object BasicExample extends MonadLiberator[String] {

  val result: Future[List[Option[Int]]] = for {
    a <- List(1, 2).!?
    b <- List(None, Some(2)).!? // Intellij Scala plugin doesn't think this will compile, but it does. Use dm[numberOfNestedMonads] to be explicit for intellij, see below
    c <- Future(1).!?
  } yield a * b + c

  // Returns List(None, Some(3), None, Some(5))
  println(Await.result(result, Duration.Inf))

}

// Intellij prefers specifying the monad depth
object ExampleExplicitlyIndicatingMonadDepth extends MonadLiberator[String] {

  val result: Future[List[Option[Int]]] = for {
    a <- List(1, 2).dm1 // dm1 indicates this Monad is 1 Monad types deep
    b <- List(None, Some(2)).dm2 // dm2 indicates this Monad is 2 Monad types deep
    c <- Future(1).dm1
  } yield a * b + c

  // Returns List(None, Some(3), None, Some(5))
  println(Await.result(result, Duration.Inf))

}

// A more complex example
object ComplexExample extends MonadLiberator[String] {

  val result: Future[List[Either[Option[Int]]]] = for {
    a <- Option(Future(Right(List(1, 2)))).dm4
    b <- Option(Option(2)).dm2
    c <- (a + b).dm0 // Currently the library does not suppert _ = _ in for comprehension. Instread use `.dm0` (a 0 nested Monad)
    d <- (if (c % 2 == 0) Right(c) else Left[Int](s"$c is not even")).dm1 // Using custom Right and Left type aliases. Left must specify the right type
  } yield d

  // Returns List(Left(Not even), Right(Some(4)))
  println(Await.result(result, Duration.Inf))

}

// Overriding precedence (the order nesting monads) is possible:
// In this example I put List after Option
// Ensure that there is only a single precedence defined in the implicit scope for a precedence of length N
object ExampleChangingPrecedence {
  // If you want to use Eithers in precedence, currently you'll need to define a type alias with a fixed left type
  type Either[A] = scala.Either[String, A]
  // All types must be a cats Traverse and Monad
  // The left most type, (Future) only needs to be a Monad, not Traversable
  // It would not be possible to put Future to the right, since it is not Traversable (without providing your own blocking implementation of cats Traverse for Future).
  type CustomPrecedenceType = Future[_] :>>: Try[_] :>>: Either[String] :>>:  Option[_] :>>: List[_] :>>: PNil
  val monadLiberator = new MonadLiberatorCustomPrecedence[CustomPrecedenceType] {
    override implicit def overridePrecedence = new Precedence[CustomPrecedenceType] {}
  }

  import monadLiberator._

  val result: Future[Option[List[Int]]] = for {
    a <- List(1, 2).!?
    b <- List(None, Some(2)).!? // Intellij Scala plugin doesn't think this will compile, but it does. Use dm[numberOfNestedMonads] to be explicit for intellij, see below
    c <- Future(1).!?
  } yield a * b + c

  // Returns None since we have overridden the nesting order so that Options come before lists
  println(Await.result(result, Duration.Inf))
}

