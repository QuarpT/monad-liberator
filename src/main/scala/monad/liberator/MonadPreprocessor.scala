package monad.liberator

import scala.language.higherKinds
import scala.concurrent.Future
import scala.util.Try

trait MonadPreprocessor[A, B] {
  type Aux = B
  def apply(a: A): B
}

class RecursionStep[A](val value: A)

object MonadPreprocessor {
  def apply[A, B](a: A)(implicit tp: MonadPreprocessor[A, B]): B = tp(a)
}

trait MonadPreprocessorLowPriorityImplicits {
  implicit def defaultMonadPreprocessor[F] = new MonadPreprocessor[F, F] {
    override def apply(a: F): F = a
  }
}

trait MonadPreprocessorImplicits extends MonadPreprocessorLowPriorityImplicits {
  implicit def seqMonadPreprocessor[F, A](implicit ev: F <:< Seq[A]) = new MonadPreprocessor[F, Seq[A]] {
    override def apply(a: F): Seq[A] = a
  }
  implicit def optionMonadPreprocessor[F, A](implicit ev: F <:< Option[A]) = new MonadPreprocessor[F, Option[A]] {
    override def apply(a: F): Option[A] = a
  }
  implicit def futureMonadPreprocessor[F, A](implicit ev: F <:< Future[A]) = new MonadPreprocessor[F, Future[A]] {
    override def apply(a: F): Future[A] = a
  }
  implicit def tryMonadPreprocessor[F, A](implicit ev: F <:< Try[A]) = new MonadPreprocessor[F, Try[A]] {
    override def apply(a: F): Try[A] = a
  }
}

trait MonadPreprocessorWithEitherImplicits[LeftType] extends MonadPreprocessorImplicits {
  type EitherM[R] = Either[LeftType, R]
  implicit def eitherMonadPreprocessor[F, A](implicit ev: F <:< EitherM[A]) = new MonadPreprocessor[F, EitherM[A]] {
    override def apply(a: F): EitherM[A] = a
  }
}
