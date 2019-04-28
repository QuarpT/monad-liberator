package monad.liberator

import cats._
import cats.implicits._
import scala.language.higherKinds

trait DeepFlattenTraverse[A, B] {
  type Aux = B
  def apply(a: A): Aux
}

object DeepFlattenTraverse {
  def  apply[A, B](a: A)(implicit deepFlatten: DeepFlattenTraverse[A, B]): deepFlatten.Aux = deepFlatten(a)
}

trait DeepFlattenTraverseRules[A, B] {
  type Aux = B
  def apply(a: A): Aux
}

object DeepFlattenTraverseRules {
  def apply[A, B](a: A)(implicit deepFlatten: DeepFlattenTraverseRules[A, B]): deepFlatten.Aux = deepFlatten(a)
}

trait DeepFlattenTraverseImplicits extends DeepFlattenTraverseRuleImplicits with DeepFlattenTraverseFastImplicits {
  implicit def deepFlattenTraverseBase[A, B, C](implicit
    mppr: MonadTypeWitnessRecur[A, B],
    dft: DeepFlattenTraverseRules[B, C]
  ) = new DeepFlattenTraverse[A, C] {
    override def apply(a: A): C = dft(mppr(a))
  }
}

trait DeepFlattenTraverseRuleImplicits0 {
  implicit def dftUnorderedRule[F[_] : Monad, A, B, C](implicit
    mpInner: DeepFlattenTraverseRules[A, B],
    mpOuter: DeepFlattenTraverseFastRules[F[B], C]
  ) = new DeepFlattenTraverseRules[F[A], C] {
    override def apply(a: F[A]): C = mpOuter(a.map(mpInner.apply))
  }
}

trait DeepFlattenTraverseRuleImplicits1 extends DeepFlattenTraverseRuleImplicits0 {
  implicit def dftBase[A, P](implicit correctPrecedence: CorrectPrecedence[A, P])= new DeepFlattenTraverseRules[A, A] {
    override def apply(a: A): A = a
  }
}

trait DeepFlattenTraverseRuleImplicits2 extends DeepFlattenTraverseRuleImplicits1 {
  implicit def dftTraverseRule[F[_] : Monad : Traverse, G[_] : Monad, A, B, C](implicit
    ev: PrecedenceGreaterThan[G[_], F[_]],
    mpInner: DeepFlattenTraverseRules[F[A], B],
    mpOuter: DeepFlattenTraverseFastRules[G[B], C]): DeepFlattenTraverseRules[F[G[A]], C] =
    new DeepFlattenTraverseRules[F[G[A]], C] {
      override def apply(a: F[G[A]]): C = {
        mpOuter(Traverse[F].sequence(a).map(mpInner.apply))
      }
    }
}

trait DeepFlattenTraverseRuleImplicits extends DeepFlattenTraverseRuleImplicits2 {
  implicit def dftFlattenRule[F[_] : Monad, P <: PList, FF, A, B <: PList, C, D](implicit
    precedence: Precedence[P],
    precedenceEvidence: PrecedenceEvidence[EvidenceOf[F[_]], Precedence[P], FF],
    mpInner: DeepFlattenTraverseRules[A, C],
    mpOuter: DeepFlattenTraverseFastRules[F[C], D]): DeepFlattenTraverseRules[F[F[A]], D] = new DeepFlattenTraverseRules[F[F[A]], D] {
    override def apply(a: F[F[A]]): D = {
      mpOuter(Monad[F].flatten(a).map(mpInner.apply))
    }
  }
}

