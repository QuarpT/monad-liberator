package monad.liberator

import scala.language.higherKinds

sealed trait PList

final case class :>>:[+H, +T <: PList](head: H, tail: T) extends PList

sealed trait PNil extends PList {
  def :>>:[H](h: H) = monad.liberator.:>>:(h, this)
}

case object PNil extends PNil

trait Precedence[+A <: PList]

trait PrecedenceGreaterThan[A, B]

trait PListGreaterThan[A, B]

trait PrecedenceLowPriorityImplicits {
  implicit def hListGtEvidence[A, B <: PList, C, D <: PList](implicit ev1: PListGreaterThan[B, D]): PListGreaterThan[A :>>: B, C :>>: D] = {
    new PListGreaterThan[A :>>: B, C :>>: D] {}
  }
}

trait PrecedenceImplicits extends PrecedenceLowPriorityImplicits with PrecedenceTailImplicits {

  implicit def hListBaseGtEvidence[A, B <: PList]: PListGreaterThan[A :>>: B, PNil] =
    new PListGreaterThan[A :>>: B, PNil] {}


  implicit def greaterThanEvidenceRule[A, B <: PList, C, D <: PList](implicit
    precedenceEvidence: Precedence[A :>>: B],
    precedenceEvidence2: Precedence[C :>>: D],
    pListGreaterThanEvidence: PListGreaterThan[B, D]
  ): PrecedenceGreaterThan[A, C] = {
    new PrecedenceGreaterThan[A, C] {}
  }
}
