package monad.liberator

import cats.Functor
import scala.language.higherKinds

trait DeepMap[A, B, C, D] {
  type Aux = D
  def apply(a: A)(f: B => C): D
}

object DeepMap {
  def apply[A, B, C, D](a: A)(f: B => C)(implicit dm: DeepMap[A, B, C, D]): D = dm(a)(f)
}

trait DeepMapLowPriorityImplicits {
  implicit def deepMapRule[F[_] : Functor, A, B, C, D](implicit dm: DeepMap[A, B, C, D]) = new DeepMap[F[A], B, C, F[D]] {
    override def apply(a: F[A])(f: B => C): F[D] = {
      Functor[F].map(a)(dm(_)(f))
    }
  }
}

trait DeepMapImplicits extends DeepMapLowPriorityImplicits {
  implicit def deepMapBase[A, B] = new DeepMap[A, A, B, B] {
    override def apply(a: A)(f: A => B): B = f(a)
  }
}
