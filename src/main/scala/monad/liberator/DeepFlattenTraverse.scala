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

trait DeepFlattenTraverseHelper[A, B] {
  type Aux = B
  def apply(a: A): Aux
}

object DeepFlattenTraverseHelper {
  def apply[A, B](a: A)(implicit deepFlatten: DeepFlattenTraverseHelper[A, B]): deepFlatten.Aux = deepFlatten(a)
}

trait DeepFlattenTraverseImplicits extends DeepFlattenTraverseHelperImplicits {
  implicit def deepFlattenTraverseBase[A, B, C](implicit
    mppr: MonadPreprocessorRecur[A, B],
    dft: DeepFlattenTraverseHelper[B, C]
  ) = new DeepFlattenTraverse[A, C] {
    override def apply(a: A): C = dft(mppr(a))
  }
}

trait DeepFlattenTraverseHelperImplicits1 {
  implicit def deepFlattenTraverseHelperIdentity[A]: DeepFlattenTraverseHelper[A, A] = new DeepFlattenTraverseHelper[A, A] {
    override def apply(a: A): A = a
  }
}

trait DeepFlattenTraverseHelperImplicits2 extends DeepFlattenTraverseHelperImplicits1 {
  implicit def swapPrecedence[F[_] : Monad : Traverse, G[_] : Monad, A, B, C](implicit
    ev: PrecedenceGreaterThan[G[_], F[_]],
    mpInner: DeepFlattenTraverseHelper[F[A], B],
    mpOuter: DeepFlattenTraverseHelper[G[B], C]): DeepFlattenTraverseHelper[F[G[A]], C] =
    new DeepFlattenTraverseHelper[F[G[A]], C] {
      override def apply(a: F[G[A]]): C = {
        mpOuter(Traverse[F].sequence(a).map(mpInner.apply))
      }
    }
}

trait DeepFlattenTraverseHelperImplicits extends DeepFlattenTraverseHelperImplicits2 {
  implicit def flattenPrecedence[F[_] : Monad, P <: PList, FF, A, B <: PList, C, D](implicit
    precedence: Precedence[P],
    precedenceEvidence: PrecedenceEvidence[EvidenceOf[F[_]], Precedence[P], FF],
    mpInner: DeepFlattenTraverseHelper[A, C],
    mpOuter: DeepFlattenTraverseHelper[F[C], D]): DeepFlattenTraverseHelper[F[F[A]], D] = new DeepFlattenTraverseHelper[F[F[A]], D] {
    override def apply(a: F[F[A]]): D = {
      mpOuter(Monad[F].flatten(a).map(mpInner.apply))
    }
  }
}

