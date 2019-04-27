package monad.liberator

import cats.Functor
import cats.implicits._

trait MonadPreprocessorRecur[A, B] {
  type Aux = B
  def apply(a: A): B
}

object MonadPreprocessorRecur {
  def apply[A, B](a: A)(implicit tp: MonadPreprocessorRecur[A, B]): B = tp(a)
}

trait MonadPreprocessorRecurImplicits1 {
  implicit def monadPreprocessorRecurOnOuter[A, B, C](implicit
    mp1: MonadPreprocessor[A, B],
    mprr: MonadPreprocessorRecur[RecursionStep[B], C]
  ) = new MonadPreprocessorRecur[A, C] {
    override def apply(a: A): C = {
      mprr(new RecursionStep(mp1(a)))
    }
  }
}

trait MonadPreprocessorRecurImplicits2 extends MonadPreprocessorRecurImplicits1 {
  implicit def monadPreprocessorRecurBase[A] = new MonadPreprocessorRecur[RecursionStep[A], A] {
    override def apply(a: RecursionStep[A]): A = a.value
  }
}

trait MonadPreprocessorRecurImplicits extends MonadPreprocessorRecurImplicits2 {
  implicit def monadPreprocessorRecurOnInner[F[_] : Functor, A, B, C](implicit
    mprr: MonadPreprocessorRecur[A, B]
  ) = new MonadPreprocessorRecur[RecursionStep[F[A]], F[B]] {
    override def apply(a: RecursionStep[F[A]]): F[B] = a.value.map(mprr.apply)
  }
}