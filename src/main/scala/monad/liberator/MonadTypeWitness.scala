package monad.liberator

import cats.Functor
import cats.implicits._
import scala.language.higherKinds
import scala.concurrent.Future
import scala.util.Try

class RecursionStep[A](val value: A)

trait MonadTypeWitnessRecur[A, B] {
  type Aux = B
  def apply(a: A): B
}

object MonadTypeWitnessRecur {
  def apply[A, B](a: A)(implicit tp: MonadTypeWitnessRecur[A, B]): B = tp(a)
}

trait MonadTypeWitnessRecurImplicits1 {
  implicit def monadPreprocessorRecurOnOuter[A, B, C](implicit
    mp1: MonadTypeWitness[A, B],
    mprr: MonadTypeWitnessRecur[RecursionStep[B], C]
  ) = new MonadTypeWitnessRecur[A, C] {
    override def apply(a: A): C = {
      mprr(new RecursionStep(mp1(a)))
    }
  }
}

trait MonadTypeWitnessRecurImplicits2 extends MonadTypeWitnessRecurImplicits1 {
  implicit def monadPreprocessorRecurBase[A] = new MonadTypeWitnessRecur[RecursionStep[A], A] {
    override def apply(a: RecursionStep[A]): A = a.value
  }
}

trait MonadTypeWitnessRecurImplicits extends MonadTypeWitnessRecurImplicits2 {
  implicit def monadPreprocessorRecurOnInner[F[_] : Functor, A, B, C](implicit
    mprr: MonadTypeWitnessRecur[A, B]
  ) = new MonadTypeWitnessRecur[RecursionStep[F[A]], F[B]] {
    override def apply(a: RecursionStep[F[A]]): F[B] = a.value.map(mprr.apply)
  }
}

trait MonadTypeWitness[A, B] {
  type Aux = B
  def apply(a: A): B
}

object MonadTypeWitness {
  def apply[A, B](a: A)(implicit tp: MonadTypeWitness[A, B]): B = tp(a)
}

trait MonadTypeWitnessLowPriorityImplicits {
  implicit def defaultMonadTypeWitness[F] = new MonadTypeWitness[F, F] {
    override def apply(a: F): F = a
  }
}

trait MonadTypeWitnessImplicits extends MonadTypeWitnessLowPriorityImplicits {
  implicit def optionMonadTypeWitness[F, A](implicit ev: F <:< Option[A]) = new MonadTypeWitness[F, Option[A]] {
    override def apply(a: F): Option[A] = a
  }
  implicit def futureMonadTypeWitness[F, A](implicit ev: F <:< Future[A]) = new MonadTypeWitness[F, Future[A]] {
    override def apply(a: F): Future[A] = a
  }
  implicit def tryMonadTypeWitness[F, A](implicit ev: F <:< Try[A]) = new MonadTypeWitness[F, Try[A]] {
    override def apply(a: F): Try[A] = a
  }
}

