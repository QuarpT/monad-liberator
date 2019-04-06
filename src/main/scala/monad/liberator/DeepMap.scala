package monad.liberator

import scala.language.higherKinds

/*
 *
 * I was unable to find a nice solution for deepMap. I feel I came close with:
 *
 *     trait DeepMap[A, B] {
 *       type Aux = B
 *       def apply(a: A): B
 *     }
 *
 *     implicit def deepMapBase[F[_] : Functor, A] = new DeepMap[F[A], Functor[F]] {
 *       override def apply(a: F[A]): Functor[F] = Functor[F]
 *     }
 *
 *     implicit def deepMapRule[F[_] : Functor, G[_], B, C[_]: Functor](implicit mm: DeepMap[G[B], Functor[C]]) =
 *       new DeepMap[F[G[B]], Functor[Lambda[α => F[C[α]]]]] {
 *         override def apply(a: F[G[B]]): Functor[Lambda[α => F[C[α]]]] = Functor[F].compose[C]
 *       }
 *
 * But sadly the Scala implicit heuristics wouldn't allow me to generate my a deep functor.
 * I still think it might be possible to do this another way.
 *
 * As a workaround I have generated this code using:
 * monad-precedence/src/test/scala/monad/liberator/util/GenerateDeepMonadCode.scala
 * monad-precedence/src/test/scala/monad/liberator/util/GenerateDeepMapCode.scala
 *
 * I would like to improve this.
 *
 * Peter Colley
 *
 */

trait DeepMap[A[_], B] {
  def apply[C](a: A[B])(f: B => C): A[C]
}

trait DeepFlatMapImplicits {



}
