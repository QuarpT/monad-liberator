package monad.liberator

import cats._
import cats.implicits._
import scala.language.higherKinds

trait DeepFlattenTraverseFastRules[A, B] {
  type Aux = B
  def apply(a: A): Aux
}

object DeepFlattenTraverseFastRules {
  def apply[A, B](a: A)(implicit deepFlatten: DeepFlattenTraverseFastRules[A, B]): deepFlatten.Aux = deepFlatten(a)
}

trait DeepFlattenTraverseFastImplicits1 {
  implicit def dftFastBase[A]: DeepFlattenTraverseFastRules[A, A] = new DeepFlattenTraverseFastRules[A, A] {
    override def apply(a: A): A = a
  }
}

trait DeepFlattenTraverseFastImplicits2 extends DeepFlattenTraverseFastImplicits1 {
  implicit def dftFastTraverseRule[F[_] : Monad : Traverse, G[_] : Monad, A, B](implicit
    ev: PrecedenceGreaterThan[G[_], F[_]],
    mpInner: DeepFlattenTraverseFastRules[F[A], B]): DeepFlattenTraverseFastRules[F[G[A]], G[B]] =
    new DeepFlattenTraverseFastRules[F[G[A]], G[B]] {
      override def apply(a: F[G[A]]): G[B] = {
        Traverse[F].sequence(a).map(mpInner.apply)
      }
    }
}

trait DeepFlattenTraverseFastImplicits extends DeepFlattenTraverseFastImplicits2 {
  implicit def dftFastFlattenRule[F[_] : Monad, P <: PList, FF, A, B <: PList, C, D](implicit
    precedence: Precedence[P],
    precedenceEvidence: PrecedenceEvidence[EvidenceOf[F[_]], Precedence[P], FF]): DeepFlattenTraverseFastRules[F[F[A]], F[A]] =
    new DeepFlattenTraverseFastRules[F[F[A]], F[A]] {
      override def apply(a: F[F[A]]): F[A] = {
        Monad[F].flatten(a)
      }
    }
}

