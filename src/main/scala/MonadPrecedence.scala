package monadp

import scala.concurrent.Future
import cats._
import cats.implicits._
import shapeless.Lazy

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

trait PrecedenceEvidence[A <: Precedence[B], B <: HList]

trait PrecedenceGreaterThan[+A, +B]

trait HListGreaterThan[A, B]

object PrecedenceGreaterThan {
  def apply[A, B](implicit evidence: PrecedenceGreaterThan[A, B]): PrecedenceGreaterThan[A, B] = evidence
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
  def other = {
    implicit val precedence = new Precedence[Future[_] :: List[_] :: Option[_] :: HNil] {}
    val a: Option[Future[Option[Future[List[List[Future[Int]]]]]]] = Option(Future(Option(Future(List(List(Future(5)))))))
    val z: Future[List[Option[Int]]] = MonadP(a)
  }

  implicit val precedence = new Precedence[Future[_] :: Set[_] :: Option[_] :: HNil] {}
  PrecedenceGreaterThan[Future[_], Set[_]]
  PrecedenceGreaterThan[Future[_], Option[_]]
//  val y: PrecedenceGreaterThan[Future[_], Option[_]] = PrecedenceGreaterThan[Future[_], Option[_]]
//  val y = PrecedenceGreaterThan[Future[_], Option[_]]
}