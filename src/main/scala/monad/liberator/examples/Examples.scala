package monad.liberator.examples

import monad.liberator._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.Try

// The default precedence is Future[_] :>>: List[_] :>>: Either[_] :>>: Try[_] :>>: Option[_]
// All monads must be defined as part of the implicit monad precedence order.
// It is possible to provide your own monad precedence, see ExampleChangingPrecedence

object BasicExample extends MonadLiberator[String] {

//  implicitly[PrecedenceGreaterThan[Future[_], List[_]]]

//   implicit val peF = PrecedenceEvidence[EvidenceOf[Future[_]], Precedence[Future[_] :>>: List[_] :>>: Either[_] :>>: Try[_] :>>:  Option[_] :>>: PNil], Precedence[Future[_] :>>: List[_] :>>: Either[_] :>>: Try[_] :>>:  Option[_] :>>: PNil]]
//   implicit val peL = PrecedenceEvidence[EvidenceOf[List[_]], Precedence[Future[_] :>>: List[_] :>>: Either[_] :>>: Try[_] :>>:  Option[_] :>>: PNil], Precedence[List[Any] :>>: Either[Any] :>>: Try[Any] :>>:  Option[Any] :>>: PNil]]
//  implicit val xasdf = PrecedenceEvidence.apply2(new EvidenceOf[List[_]], defaultPrecedence)
//
//  val xxx = implicitly[PListGreaterThan[Future[_] :>>: List[_] :>>: Either[_] :>>: Try[_] :>>:  Option[_] :>>: PNil, List[_] :>>: Either[_] :>>: Try[_] :>>:  Option[_] :>>: PNil]]
//
//  val xsdf: PrecedenceGreaterThan[Future[_], List[_]] = greaterThanEvidenceRule(defaultPrecedence, peF, peL, xxx)
//
  val xasdgd = PrecedenceGreaterThan.apply[Future[_], List[_]]()

//  val xsddf: PrecedenceGreaterThan[List[_], Future[_]] = greaterThanEvidenceRule(defaultPrecedence, peL, peF, xxx)

//  val p = PrecedenceEvidence(new EvidenceOf[List[_]], defaultPrecedence)
//  val ax: Option[Int] = DeepFlattenTraverse(Option(Option(5)))
//  val x: Future[Option[Int]] = DeepFlattenTraverse(Option(Future(5)))
//  val result: Future[List[Option[Int]]] = for {
//    a <- List(1, 2).!?
//    b <- List(None, Some(2)).!?
//    c <- Future(1).!?
//  } yield a * b + c

  // Returns Future(List(None, Some(3), None, Some(5)))
  // For Options, if flatMapping on Some or None by themselves, specify the Option type explicitly, e.g. `None: Option[Int]`
}
/*
object DeepFlattenExample extends MonadLiberator[String] {
  val result: Future[List[Either[Option[Int]]]] =
    DeepFlattenTraverse(Option(Future(Future(Right(Option(Option(List(5))))))))
  // Returns Future(List(Right(Some(5)))
}

object DeepMapExample extends MonadLiberator[String] {
  val result: Future[List[Either[Option[Int]]]] = Future(List(Right(Option(5)))).deepMap(_ * 2)
  // Returns Future(List(Right(Some(10)))
}

// A more complex example
object ComplexExample extends MonadLiberator[String] {

  val result: Future[List[Either[Option[Int]]]] = for {
    a <- Option(Future(Right(List(1, 2)))).!?
    b <- Option(Option(2)).!?
    c <- (a + b).!?
    d <- (if (c % 2 == 0) Right(c) else Left[Int](s"$c is not even")).!?
  } yield d

  // Returns Future(List(Left("3 is not even"), Right(Some(4))))
  // Currently the library does not support _ = _ in for comprehension. Instread use `.dm0` (a 0 nested Monad)
  // For Eithers, use the custom Right and Left type aliases from MonadLiberator trait. The Left type parameter must specify the right type
}

// Using a custom precedence (the order nesting monads) is possible:
// In this example I put List after Option
// Ensure that there is only a single precedence type defined in the implicit scope (other than the monad.liberator.PrecedenceTail rules)
object ExampleChangingPrecedence {
  // With MonadLiberatorCustomPrecedence[CustomPrecedenceType]
  // if you want to add Eithers to the precedence, currently you'll need to define a type alias with a fixed left type.
  // MonadLiberator[EitherLeftType] already provides this for you.
  type Either[A] = scala.Either[String, A]
  // All types must require an implemented Cats Traverse and Monad type class
  // The left most type, (Future) only needs a Monad, not Traversable
  // It would not be possible to put Future to the right of another type, since it is not Traversable
  //    (you would need to provide your own blocking implementation of cats Traverse for Future if you require this).
  type CustomPrecedenceType = Future[_] :>>: Try[_] :>>: Either[String] :>>:  Option[_] :>>: List[_] :>>: PNil
  val monadLiberator = new MonadLiberatorCustomPrecedence[CustomPrecedenceType] {
    override implicit def overridePrecedence = new Precedence[CustomPrecedenceType]
  }

  import monadLiberator._

  val result: Future[Option[List[Int]]] = for {
    a <- List(1, 2).!?
    b <- List(None, Some(2)).!?
    c <- Future(1).!?
  } yield a * b + c

  // Returns Future(None) since we have overridden the nesting order so that Options come before lists
}

*/
