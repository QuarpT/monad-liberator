package monad.liberator

import cats._
import scala.concurrent.Future
import scala.util.Try

class MonadLiberator[EitherLeftType] extends MonadLiberatorMixin {
  implicit def seqMonadTypeWitness[F, A](implicit ev: F <:< Seq[A]) = new MonadTypeWitness[F, Seq[A]] {
    override def apply(a: F): Seq[A] = a
  }

  type EitherM[R] = Either[EitherLeftType, R]
  implicit def eitherMonadTypeWitness[F, A](implicit ev: F <:< EitherM[A]) = new MonadTypeWitness[F, EitherM[A]] {
    override def apply(a: F): EitherM[A] = a
  }

  implicit val monadPrecedence = new Precedence[Future[_] :>>: Seq[_] :>>: EitherM[_] :>>: Try[_] :>>:  Option[_] :>>: PNil]
}

trait MonadLiberatorMixin extends DeepMonadImplicits
  with DeepMapImplicits
  with DeepFlattenTraverseImplicits
  with PrecedenceImplicits
  with DeepNestedTypeImplicits
  with CatsInstancesImplicits
  with CorrectPrecedenceImplicits
  with MonadTypeWitnessRecurImplicits
  with MonadTypeWitnessImplicits
  with SeqMonad

trait CatsInstancesImplicits extends instances.AllInstances
  with instances.AllInstancesBinCompat0
  with instances.AllInstancesBinCompat1
  with instances.AllInstancesBinCompat2
  with instances.AllInstancesBinCompat3
