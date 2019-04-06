package monad.liberator

import cats._
import cats.implicits._
import scala.language.higherKinds

trait DeepFlattenTraverse[A, B] {
  type Aux = B
  def apply(a: A): Aux
}

object DeepFlattenTraverse {
  def apply[A, B](a: A)(implicit deepFlatten: DeepFlattenTraverse[A, B]): deepFlatten.Aux = deepFlatten(a)
}

trait DeepFlattenTraverseImplicits0 {
  implicit def deepFlattenTraverseIdentity[A]: DeepFlattenTraverse[A, A] = new DeepFlattenTraverse[A, A] {
    override def apply(a: A): A = a
  }
}

trait DeepFlattenTraverseImplicits1 extends DeepFlattenTraverseImplicits0 {
  implicit def swapPrecedence[F[_] : Monad : Traverse, G[_] : Monad, A, B, C](implicit
    ev: PrecedenceGreaterThan[G[_], F[_]],
    mpInner: DeepFlattenTraverse[F[A], B],
    mpOuter: DeepFlattenTraverse[G[B], C]): DeepFlattenTraverse[F[G[A]], C] =
    new DeepFlattenTraverse[F[G[A]], C] {
      override def apply(a: F[G[A]]): C = {
        mpOuter(Traverse[F].sequence(a).map(mpInner.apply))
      }
    }
}

trait DeepFlattenTraverseImplicits2 extends DeepFlattenTraverseImplicits1 {
  implicit def flattenPrecedence[F[_] : Monad, A, B <: PList, C, D](implicit
    precedenceEvidence: Precedence[F[_] :>: B],
    mpInner: DeepFlattenTraverse[A, C],
    mpOuter: DeepFlattenTraverse[F[C], D]): DeepFlattenTraverse[F[F[A]], D] = new DeepFlattenTraverse[F[F[A]], D] {
    override def apply(a: F[F[A]]): D = {
      mpOuter(Monad[F].flatten(a).map(mpInner.apply))
    }
  }
}

