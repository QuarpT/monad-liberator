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

trait MonadLiberatorMixin[EitherLeftType] extends DeepMonadImplicits
  with DeepMapImplicits
  with DeepFlattenTraverseImplicits
  with PrecedenceImplicits
  with DeepNestedTypeImplicits
  with CatsInstancesImplicits
  with MonadPreprocessorWithEitherImplicits[EitherLeftType]
  with MonadPreprocessorRecurImplicits
  with SeqMonad {

  implicit val monadPrecedence = new Precedence[Future[_] :>>: Seq[_] :>>: EitherM[_] :>>: Try[_] :>>:  Option[_] :>>: PNil]
}

class MonadLiberator[EitherLeftType] extends MonadLiberatorMixin[EitherLeftType]

/**
  *  For Eithers use EitherM from EitherMonadPreprocessor[EitherLeftType], see examples
  */
trait MonadLiberatorCustomPrecedenceMixin[P <: PList] extends DeepMonadImplicits
  with DeepMapImplicits
  with DeepFlattenTraverseImplicits
  with PrecedenceImplicits
  with DeepNestedTypeImplicits
  with CatsInstancesImplicits
  with MonadPreprocessorImplicits
  with MonadPreprocessorRecurImplicits
  with SeqMonad {

  implicit val monadPrecedence: Precedence[P] = new Precedence[P]
}

class MonadLiberatorCustomPrecedence[P <: PList] extends MonadLiberatorCustomPrecedenceMixin[P]

trait CatsInstancesImplicits extends instances.AllInstances
  with instances.AllInstancesBinCompat0
  with instances.AllInstancesBinCompat1
  with instances.AllInstancesBinCompat2
  with instances.AllInstancesBinCompat3
