package monad.liberator

import cats.Functor
import monadp.DeepFlatMap

import scala.language.higherKinds

trait DeepMap[A[_], B] {
  def apply[C](a: A[B])(f: B => C): A[C]
}

trait DeepFlatMapImplicits {

  implicit def deepMapRule1[F[_] : Functor, B] = new DeepFlatMap[Lambda[X => F[X]], B] {
    override def apply[C](a: F[B])(f: B => C): F[C] = {
      Functor[F].map(a)(f)
    }
  }


}
