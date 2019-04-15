package monad.liberator

import cats._
import scala.concurrent.Future
import scala.util.Try

/*
 *
 * It should be possible to refactor out the EitherLeftType,
 * and provide a solution for DeepMonadImplicits for Monad types with extra fixed type parameters
 *
 */

trait MonadLiberator[EitherLeftType] extends DeepMonadImplicits
  with DeepMapImplicits
  with DeepFlattenTraverseImplicits
  with PrecedenceImplicits
  with DeepNestedTypeImplicits
  with CatsInstancesImplicits {

  type Either[R] = scala.Either[EitherLeftType, R]
  def Right[A](value: A): Either[A] = scala.Right(value)
  def Left[SetMeToRightType](value: EitherLeftType): Either[SetMeToRightType] = scala.Left(value)
  implicit lazy val defaultPrecedence = new Precedence[Future[_] :>>: List[_] :>>: Either[_] :>>: Try[_] :>>:  Option[_] :>>: PNil]
}

/**
  *  You'll need to define your own either type for this, with the Left type filled in.
  *  Do not mix with any other MonadLiberators, multiple precedences in scope will break things.
  */
trait MonadLiberatorCustomPrecedence[P <: PList] extends DeepMonadImplicits
  with DeepMapImplicits
  with DeepFlattenTraverseImplicits
  with PrecedenceImplicits
  with DeepNestedTypeImplicits
  with CatsInstancesImplicits {

  implicit def overridePrecedence: Precedence[P] = new Precedence[P]
}

trait CatsInstancesImplicits extends instances.AllInstances
  with instances.AllInstancesBinCompat0
  with instances.AllInstancesBinCompat1
  with instances.AllInstancesBinCompat2
  with instances.AllInstancesBinCompat3
