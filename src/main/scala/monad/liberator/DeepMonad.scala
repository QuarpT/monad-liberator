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
 * But sadly the Scala implicit heuristics didn't seem to allow generating a deep functor. I still think it might be possible.
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
trait DeepMonadImplicits {
    implicit class DeepMonad1[F1[_], A](functor: F1[A])(implicit dfm: DeepMap[Lambda[X => F1[X]], A]) {
    def !? = this
    def dm = this
    def dm1 = this
    def deepMap[B](f: A => B): F1[B] = dfm(functor)(f)
    def deepFlatMap[B, C](f: A => B)(implicit dft: DeepFlattenTraverse[F1[B], C]): C = dft(dfm(functor)(f))
    def flatMap[B, C](f: A => B)(implicit dft: DeepFlattenTraverse[F1[B], C]): C = deepFlatMap(f)
    def map[B, C](f: A => B)(implicit dft: DeepFlattenTraverse[F1[B], C]): C = deepFlatMap(f)
  }

  implicit class DeepMonad2[F1[_], F2[_], A](functor: F1[F2[A]])(implicit dfm: DeepMap[Lambda[X => F1[F2[X]]], A]) {
    def !? = this
    def dm = this
    def dm2 = this
    def deepMap[B](f: A => B): F1[F2[B]] = dfm(functor)(f)
    def deepFlatMap[B, C](f: A => B)(implicit dft: DeepFlattenTraverse[F1[F2[B]], C]): C = dft(dfm(functor)(f))
    def flatMap[B, C](f: A => B)(implicit dft: DeepFlattenTraverse[F1[F2[B]], C]): C = deepFlatMap(f)
    def map[B, C](f: A => B)(implicit dft: DeepFlattenTraverse[F1[F2[B]], C]): C = deepFlatMap(f)
  }

  implicit class DeepMonad3[F1[_], F2[_], F3[_], A](functor: F1[F2[F3[A]]])(implicit dfm: DeepMap[Lambda[X => F1[F2[F3[X]]]], A]) {
    def !? = this
    def dm = this
    def dm3 = this
    def deepMap[B](f: A => B): F1[F2[F3[B]]] = dfm(functor)(f)
    def deepFlatMap[B, C](f: A => B)(implicit dft: DeepFlattenTraverse[F1[F2[F3[B]]], C]): C = dft(dfm(functor)(f))
    def flatMap[B, C](f: A => B)(implicit dft: DeepFlattenTraverse[F1[F2[F3[B]]], C]): C = deepFlatMap(f)
    def map[B, C](f: A => B)(implicit dft: DeepFlattenTraverse[F1[F2[F3[B]]], C]): C = deepFlatMap(f)
  }

  implicit class DeepMonad4[F1[_], F2[_], F3[_], F4[_], A](functor: F1[F2[F3[F4[A]]]])(implicit dfm: DeepMap[Lambda[X => F1[F2[F3[F4[X]]]]], A]) {
    def !? = this
    def dm = this
    def dm4 = this
    def deepMap[B](f: A => B): F1[F2[F3[F4[B]]]] = dfm(functor)(f)
    def deepFlatMap[B, C](f: A => B)(implicit dft: DeepFlattenTraverse[F1[F2[F3[F4[B]]]], C]): C = dft(dfm(functor)(f))
    def flatMap[B, C](f: A => B)(implicit dft: DeepFlattenTraverse[F1[F2[F3[F4[B]]]], C]): C = deepFlatMap(f)
    def map[B, C](f: A => B)(implicit dft: DeepFlattenTraverse[F1[F2[F3[F4[B]]]], C]): C = deepFlatMap(f)
  }

  implicit class DeepMonad5[F1[_], F2[_], F3[_], F4[_], F5[_], A](functor: F1[F2[F3[F4[F5[A]]]]])(implicit dfm: DeepMap[Lambda[X => F1[F2[F3[F4[F5[X]]]]]], A]) {
    def !? = this
    def dm = this
    def dm5 = this
    def deepMap[B](f: A => B): F1[F2[F3[F4[F5[B]]]]] = dfm(functor)(f)
    def deepFlatMap[B, C](f: A => B)(implicit dft: DeepFlattenTraverse[F1[F2[F3[F4[F5[B]]]]], C]): C = dft(dfm(functor)(f))
    def flatMap[B, C](f: A => B)(implicit dft: DeepFlattenTraverse[F1[F2[F3[F4[F5[B]]]]], C]): C = deepFlatMap(f)
    def map[B, C](f: A => B)(implicit dft: DeepFlattenTraverse[F1[F2[F3[F4[F5[B]]]]], C]): C = deepFlatMap(f)
  }

  implicit class DeepMonad6[F1[_], F2[_], F3[_], F4[_], F5[_], F6[_], A](functor: F1[F2[F3[F4[F5[F6[A]]]]]])(implicit dfm: DeepMap[Lambda[X => F1[F2[F3[F4[F5[F6[X]]]]]]], A]) {
    def !? = this
    def dm = this
    def dm6 = this
    def deepMap[B](f: A => B): F1[F2[F3[F4[F5[F6[B]]]]]] = dfm(functor)(f)
    def deepFlatMap[B, C](f: A => B)(implicit dft: DeepFlattenTraverse[F1[F2[F3[F4[F5[F6[B]]]]]], C]): C = dft(dfm(functor)(f))
    def flatMap[B, C](f: A => B)(implicit dft: DeepFlattenTraverse[F1[F2[F3[F4[F5[F6[B]]]]]], C]): C = deepFlatMap(f)
    def map[B, C](f: A => B)(implicit dft: DeepFlattenTraverse[F1[F2[F3[F4[F5[F6[B]]]]]], C]): C = deepFlatMap(f)
  }

  implicit class DeepMonad7[F1[_], F2[_], F3[_], F4[_], F5[_], F6[_], F7[_], A](functor: F1[F2[F3[F4[F5[F6[F7[A]]]]]]])(implicit dfm: DeepMap[Lambda[X => F1[F2[F3[F4[F5[F6[F7[X]]]]]]]], A]) {
    def !? = this
    def dm = this
    def dm7 = this
    def deepMap[B](f: A => B): F1[F2[F3[F4[F5[F6[F7[B]]]]]]] = dfm(functor)(f)
    def deepFlatMap[B, C](f: A => B)(implicit dft: DeepFlattenTraverse[F1[F2[F3[F4[F5[F6[F7[B]]]]]]], C]): C = dft(dfm(functor)(f))
    def flatMap[B, C](f: A => B)(implicit dft: DeepFlattenTraverse[F1[F2[F3[F4[F5[F6[F7[B]]]]]]], C]): C = deepFlatMap(f)
    def map[B, C](f: A => B)(implicit dft: DeepFlattenTraverse[F1[F2[F3[F4[F5[F6[F7[B]]]]]]], C]): C = deepFlatMap(f)
  }

  implicit class DeepMonad8[F1[_], F2[_], F3[_], F4[_], F5[_], F6[_], F7[_], F8[_], A](functor: F1[F2[F3[F4[F5[F6[F7[F8[A]]]]]]]])(implicit dfm: DeepMap[Lambda[X => F1[F2[F3[F4[F5[F6[F7[F8[X]]]]]]]]], A]) {
    def !? = this
    def dm = this
    def dm8 = this
    def deepMap[B](f: A => B): F1[F2[F3[F4[F5[F6[F7[F8[B]]]]]]]] = dfm(functor)(f)
    def deepFlatMap[B, C](f: A => B)(implicit dft: DeepFlattenTraverse[F1[F2[F3[F4[F5[F6[F7[F8[B]]]]]]]], C]): C = dft(dfm(functor)(f))
    def flatMap[B, C](f: A => B)(implicit dft: DeepFlattenTraverse[F1[F2[F3[F4[F5[F6[F7[F8[B]]]]]]]], C]): C = deepFlatMap(f)
    def map[B, C](f: A => B)(implicit dft: DeepFlattenTraverse[F1[F2[F3[F4[F5[F6[F7[F8[B]]]]]]]], C]): C = deepFlatMap(f)
  }
}
