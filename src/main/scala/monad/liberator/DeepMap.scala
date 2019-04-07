package monad.liberator

import cats.Functor

import scala.language.higherKinds

/*
 *
 * See comments for writing a single implicit rule and a base case in monad.liberator.DeepMonad
 *
 * Peter Colley
 *
 */

trait DeepMap[A[_], B] {
  def apply[C](a: A[B])(f: B => C): A[C]
}

trait DeepMapImplicits {

  implicit def deepMapRule1[F1[_] : Functor, B] = new DeepMap[Lambda[X => F1[X]], B] {
    override def apply[C](a: F1[B])(f: B => C): F1[C] = {
      Functor[F1].map(a)(f)
    }
  }

  implicit def deepMapRule2[F1[_] : Functor, F2[_] : Functor, B] = new DeepMap[Lambda[X => F1[F2[X]]], B] {
    override def apply[C](a: F1[F2[B]])(f: B => C): F1[F2[C]] = {
      Functor[F1].compose[F2].map(a)(f)
    }
  }

  implicit def deepMapRule3[F1[_] : Functor, F2[_] : Functor, F3[_] : Functor, B] = new DeepMap[Lambda[X => F1[F2[F3[X]]]], B] {
    override def apply[C](a: F1[F2[F3[B]]])(f: B => C): F1[F2[F3[C]]] = {
      Functor[F1].compose[F2].compose[F3].map(a)(f)
    }
  }

  implicit def deepMapRule4[F1[_] : Functor, F2[_] : Functor, F3[_] : Functor, F4[_] : Functor, B] = new DeepMap[Lambda[X => F1[F2[F3[F4[X]]]]], B] {
    override def apply[C](a: F1[F2[F3[F4[B]]]])(f: B => C): F1[F2[F3[F4[C]]]] = {
      Functor[F1].compose[F2].compose[F3].compose[F4].map(a)(f)
    }
  }

  implicit def deepMapRule5[F1[_] : Functor, F2[_] : Functor, F3[_] : Functor, F4[_] : Functor, F5[_] : Functor, B] = new DeepMap[Lambda[X => F1[F2[F3[F4[F5[X]]]]]], B] {
    override def apply[C](a: F1[F2[F3[F4[F5[B]]]]])(f: B => C): F1[F2[F3[F4[F5[C]]]]] = {
      Functor[F1].compose[F2].compose[F3].compose[F4].compose[F5].map(a)(f)
    }
  }

  implicit def deepMapRule6[F1[_] : Functor, F2[_] : Functor, F3[_] : Functor, F4[_] : Functor, F5[_] : Functor, F6[_] : Functor, B] = new DeepMap[Lambda[X => F1[F2[F3[F4[F5[F6[X]]]]]]], B] {
    override def apply[C](a: F1[F2[F3[F4[F5[F6[B]]]]]])(f: B => C): F1[F2[F3[F4[F5[F6[C]]]]]] = {
      Functor[F1].compose[F2].compose[F3].compose[F4].compose[F5].compose[F6].map(a)(f)
    }
  }

  implicit def deepMapRule7[F1[_] : Functor, F2[_] : Functor, F3[_] : Functor, F4[_] : Functor, F5[_] : Functor, F6[_] : Functor, F7[_] : Functor, B] = new DeepMap[Lambda[X => F1[F2[F3[F4[F5[F6[F7[X]]]]]]]], B] {
    override def apply[C](a: F1[F2[F3[F4[F5[F6[F7[B]]]]]]])(f: B => C): F1[F2[F3[F4[F5[F6[F7[C]]]]]]] = {
      Functor[F1].compose[F2].compose[F3].compose[F4].compose[F5].compose[F6].compose[F7].map(a)(f)
    }
  }

  implicit def deepMapRule8[F1[_] : Functor, F2[_] : Functor, F3[_] : Functor, F4[_] : Functor, F5[_] : Functor, F6[_] : Functor, F7[_] : Functor, F8[_] : Functor, B] = new DeepMap[Lambda[X => F1[F2[F3[F4[F5[F6[F7[F8[X]]]]]]]]], B] {
    override def apply[C](a: F1[F2[F3[F4[F5[F6[F7[F8[B]]]]]]]])(f: B => C): F1[F2[F3[F4[F5[F6[F7[F8[C]]]]]]]] = {
      Functor[F1].compose[F2].compose[F3].compose[F4].compose[F5].compose[F6].compose[F7].compose[F8].map(a)(f)
    }
  }

}
