package monadp

import scala.concurrent.{Await, Future}
import cats._
import cats.implicits._
import scala.concurrent.duration._
import scala.collection.immutable
import scala.concurrent.ExecutionContext.Implicits.global
import scala.language.higherKinds
import scala.reflect.ClassTag

sealed trait HList

final case class ::[+H, +T <: HList](head: H, tail: T) extends HList

sealed trait HNil extends HList {
  def ::[H](h: H) = monadp.::(h, this)
}

case object HNil extends HNil

trait Precedence[+A <: HList]

trait PrecedenceGreaterThan[A, B]

trait HListGreaterThan[A, B]

trait DeepMap[A[_], B] {
  def apply[C](a: A[B])(f: B => C): A[C]
}

object DeepMap {
  def apply[F[_], G[_], A, B](a: F[G[A]])(f: A => B)(implicit mm: DeepMap[Lambda[X => F[G[X]]], A]): F[G[B]] = mm(a)(f)
  def apply2[F1[_], F2[_], F3[_], B, C](a: F1[F2[F3[B]]])(f: B => C)(implicit mm: DeepMap[Lambda[X => F1[F2[F3[X]]]], B]): F1[F2[F3[C]]] = mm(a)(f)
  implicit class DeepMap1[F1[_], A](functor: F1[A])(implicit mm: DeepMap[Lambda[X => F1[X]], A]) {
    def mp = this
    def deepMap[B](f: A => B): F1[B] = mm(functor)(f)
    def deepFlatMap[B, C](f: A => B)(implicit mp: MonadP[F1[B], C]): C = mp(mm(functor)(f))
    def flatMap[B, C](f: A => B)(implicit mp: MonadP[F1[B], C]): C = deepFlatMap(f)
    def map[B, C](f: A => B)(implicit mp: MonadP[F1[B], C]): C = deepFlatMap(f)
  }
  implicit class DeepMap2[F1[_], F2[_], A](functor: F1[F2[A]])(implicit mm: DeepMap[Lambda[X => F1[F2[X]]], A]) {
    def mp = this
    def deepMap[B](f: A => B): F1[F2[B]] = mm(functor)(f)
    def deepFlatMap[B, C](f: A => B)(implicit mp: MonadP[F1[F2[B]], C]): C = mp(mm(functor)(f))
    def flatMap[B, C](f: A => B)(implicit mp: MonadP[F1[F2[B]], C]): C = deepFlatMap(f)
    def map[B, C](f: A => B)(implicit mp: MonadP[F1[F2[B]], C]): C = deepFlatMap(f)
  }
  implicit class DeepMap3[F1[_], F2[_], F3[_], A](functor: F1[F2[F3[A]]])(implicit mm: DeepMap[Lambda[X => F1[F2[F3[X]]]], A]) {
    def mp = this
    def deepMap[B](f: A => B): F1[F2[F3[B]]] = mm(functor)(f)
    def deepFlatMap[B, C](f: A => B)(implicit mp: MonadP[F1[F2[F3[B]]], C]): C = mp(mm(functor)(f))
    def flatMap[B, C](f: A => B)(implicit mp: MonadP[F1[F2[F3[B]]], C]): C = deepFlatMap(f)
    def map[B, C](f: A => B)(implicit mp: MonadP[F1[F2[F3[B]]], C]): C = deepFlatMap(f)
  }
}

trait MonadP[A, B] {
  type Aux = B

  def apply(a: A): Aux
}

object MonadP {
  def apply[A, B](a: A)(implicit monadP: MonadP[A, B]): monadP.Aux = monadP(a)
}


trait LowPriorityImplicits {
  implicit def identityPrecedence[A] = new MonadP[A, A] {
    override def apply(a: A): A = a
  }

  implicit def hListGtEvidence[A, B <: HList, C, D <: HList](implicit ev1: HListGreaterThan[B, D]): HListGreaterThan[A :: B, C :: D] = {
    new HListGreaterThan[A :: B, C :: D] {}
  }

}

trait MediumPriorityImplicits extends LowPriorityImplicits {

  implicit def swapPrecedence[F[_] : Monad : Traverse, G[_] : Monad, A, B, C](implicit
    ev: PrecedenceGreaterThan[G[_], F[_]],
    mpInner: MonadP[F[A], B],
    mpOuter: MonadP[G[B], C]): MonadP[F[G[A]], C] =
    new MonadP[F[G[A]], C] {
      override def apply(a: F[G[A]]): C = {
        mpOuter(Traverse[F].sequence(a).map(mpInner.apply))
      }
    }



}

trait HighPriorityImplicits extends MediumPriorityImplicits {

  implicit def deepMapRule1[F[_] : Functor, B] = new DeepMap[Lambda[X => F[X]], B] {
    override def apply[C](a: F[B])(f: B => C): F[C] = {
      Functor[F].map(a)(f)
    }
  }
  implicit def deepMapRule2[F[_] : Functor, G[_] : Functor, B] = new DeepMap[Lambda[X => F[G[X]]], B] {
    override def apply[C](a: F[G[B]])(f: B => C): F[G[C]] = {
      Functor[F].map(a)(_.map(f))
    }
  }
  implicit def deepMapRule3[F1[_] : Functor, F2[_] : Functor, F3[_] : Functor, B] = new DeepMap[Lambda[X => F1[F2[F3[X]]]], B] {
    override def apply[C](a: F1[F2[F3[B]]])(f: B => C): F1[F2[F3[C]]] = {
      Functor[F1].compose[F2].compose[F3].map(a)(f)
    }
  }

  implicit def flattenPrecedence[F[_] : Monad, A, B <: HList, C, D](implicit
    precedenceEvidence: Precedence[F[_] :: B],
    mpInner: MonadP[A, C],
    mpOuter: MonadP[F[C], D]): MonadP[F[F[A]], D] = new MonadP[F[F[A]], D] {
    override def apply(a: F[F[A]]): D = {
      mpOuter(Monad[F].flatten(a).map(mpInner.apply))
    }
  }

  implicit def precedenceTail[A, B, C](implicit precedence: Precedence[A :: B :: C :: HNil]) = new Precedence[B :: C :: HNil] {}

  implicit def precedenceTail2[A, B, C](implicit precedence: Precedence[A :: B :: C :: HNil]) = new Precedence[C :: HNil] {}

  implicit def hListBaseGtEvidence[A, B <: HList]: HListGreaterThan[A :: B, HNil] = {
    new HListGreaterThan[A :: B, HNil] {}
  }

  implicit def gtEvidence[A, B <: HList, C, D <: HList](implicit
    precedenceEvidence: Precedence[A :: B],
    precedenceEvidence2: Precedence[C :: D],
    gtEvidence2: HListGreaterThan[B, D],
  ): PrecedenceGreaterThan[A, C] = {
    new PrecedenceGreaterThan[A, C] {}
  }
}


object Main extends App with HighPriorityImplicits {
 import DeepMap._
  import scala.reflect.runtime.universe.reify
  import scala.reflect.runtime.universe._

  implicit val precedence = new Precedence[Future[_] :: List[_] :: Option[_] :: HNil] {}
  val a: Option[Future[Option[Future[List[List[Future[Int]]]]]]] = Option(Future(Option(Future(List(List(Future(5)))))))
  val z: Future[List[Option[Int]]] = MonadP(a)

  println(DeepMap(List(Option(5)))(x => x * 2))
  val aa: Seq[Option[String]] = DeepMap.apply(List(Option(5)))(x => x.toString)

//  val e: Either[Nothing, List[Option[Int]]] = Right(List(Option(5)))
//  val bb: Either[Nothing, List[Option[String]]] = DeepMap.apply2(e)(_.toString)
//  Functor[Either[String, _]]

  val zz: Future[List[Option[Int]]] = for {
    a <- Option(List(1,2,3,4)).mp
    b <- Option(Future(5)).mp
    c <- Future(b * a).mp
  } yield c

  println(Await.result(zz, 5 seconds))

//  val e = Option(Option(List(5))).deepFlatMap(_ * 5)
//  println(e)

}