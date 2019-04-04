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

//trait Unused[A]

case class Unused[A](a: A)

trait MegaMapT[A[_], B] {
  def apply[C](a: A[B])(f: B => C): A[C]
}

trait MegaMap1[F[_], B] extends MegaMapT[F, B]

trait MegaMap2[A, B] {
  type Aux = B
  def apply(a: A): B
}

object MegaMap2 {
  def apply[A, B](a: A)(implicit mm: MegaMap2[A, B]): mm.Aux = mm(a)
}

object MegaMap1 {
  def apply[F[_], G[_], B, C](a: F[G[B]])(f: B => C)(implicit mm: MegaMap1[Lambda[X => F[G[X]]], B]) = mm(a)(f)
//  def apply2[F[_], G[_], B, C](a: F[G[B]])(f: B => C)(implicit mm: MegaMap1[Lambda[X => F[G[X]]], B]) = mm(a)(f)
}

//trait MegaMap2[F[_], G[_], B] extends MegaMapT[F[_ <: G], B]


object MegaMapT {
  def apply[A[_], B, C](a: A[B])(f: B => C)(implicit mm: MegaMapT[A,B]): A[C] = mm(a)(f)
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

  implicit def megaMapRule2Base[F[_] : Functor, A] = new MegaMap2[F[A], Functor[F]] {
    override def apply(a: F[A]): Functor[F] = Functor[F]
  }

//  implicit def megaMapBase[F[_] : Functor, A] = new MegaMap[F, A] {
//    override def apply[B](functor: F[A])(f: A => B): F[B] = Functor[F].map(functor)(f)
//  }

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

//  implicit def megaMapRule[F[_] : Functor, B] = new MegaMap1[F, B]  {
//    override def apply[C](a: F[B])(f: B => C): F[C] = Functor[F].map(a)(f)
//  }

  implicit def megaMapRule1[F[_] : Functor, G[_] : Functor, B] = new MegaMap1[Lambda[X => F[G[X]]], B]  {
    override def apply[C](a: F[G[B]])(f: B => C): F[G[C]] = {
      Functor[F].map(a)(_.map(f))
    }
  }


  implicit def megaMapRule2Rule[F[_] : Functor, G[_], B, C[_]: Functor](implicit mm: MegaMap2[G[B], Functor[C]]) =
    new MegaMap2[F[G[B]], Functor[Lambda[α => F[C[α]]]]] {
      override def apply(a: F[G[B]])= {
         Functor[F].compose[C]
      }
    }

//  implicit def megaMapRule2[F[_] : Functor, G[_] : Functor, B[_]] = new MegaMap1[Lambda[X => F[G[X]]], B]  {
//    override def apply[C](a: F[G[B]])(f: B => C): F[G[C]] = {
//      Functor[F].map(a)(_.map(f))
//    }
//  }

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
import scala.reflect.runtime.universe.reify
import scala.reflect.runtime.universe._
    implicit val precedence = new Precedence[Future[_] :: List[_] :: Option[_] :: HNil] {}
    val a: Option[Future[Option[Future[List[List[Future[Int]]]]]]] = Option(Future(Option(Future(List(List(Future(5)))))))
    val z: Future[List[Option[Int]]] = MonadP(a)

//  val x = MegaMapT(List(List(5)))(s => s)

//  type LL[E] = List[List[E]]
//  implicit val  zzz: List[List[Int]] = implicitly[MegaMap1[LL, Int]].apply(List(List(5)))(x => x)
  println(MegaMap1(List(Option(5)))(x=>x*2))
  val aa = MegaMap1(List(Option(5)))(x=> x.toString)
  val zz: MegaMap2[List[Int], Functor[List]] = megaMapRule2Base(Functor[List])
  val xxxx: Seq[Int] = MegaMap2.apply(List(1)).map(List(1))(x => x + 1)

//  val adfs = megaMapRule2Rule(Functor[List], Functor[List], zz)(List(List(1))).map(List(List(1)))(x => x)
  val adsfs = megaMapRule2Rule(Functor[List], Functor[List], zz)

//  val xx2xx = MegaMap2.apply(List(List(1)))(adsfs).map(List(List(1)))(x => x + 1)
//  MegaMap2(List(List(1))).map(List(List(1)))(x=>x+1)
//  MegaMap2(List(List(1))).map(List(List(1)))(x=>x + 1)
//
//  adsfs(List(List(1))).map(List(List(1)))(x => x + 1)
//  println(MegaMap2(List(List(5)))(x => x))
//    megaMapSplitterRule[Int]

//    val x = implicitly[MegaMap[Unused, List, Int]].apply()()
//    println(Await.result(z, 5 seconds))
}