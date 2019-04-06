package monad.liberator

import scala.language.higherKinds

sealed trait PList

final case class :>:[+H, +T <: PList](head: H, tail: T) extends PList

sealed trait PNil extends PList {
  def :>:[H](h: H) = monad.liberator.:>:(h, this)
}

case object PNil extends PNil

trait Precedence[+A <: PList]

trait PrecedenceGreaterThan[A, B]

trait PListGreaterThan[A, B]

trait PrecedenceImplicits extends PrecedenceTailImplicits {

  implicit def precedenceTail[A1, A2, A3](implicit precedence: Precedence[A1 :>: A2 :>: A3 :>: PNil]): Precedence[A2 :>: A3 :>: PNil] = new Precedence[A2 :>: A3 :>: PNil] {}
  implicit def precedenceTail2[A, B, C](implicit precedence: Precedence[A :>: B :>: C :>: PNil]): Precedence[C :>: PNil] = new Precedence[C :>: PNil] {}

  implicit def hListBaseGtEvidence[A, B <: PList]: PListGreaterThan[A :>: B, PNil] =
    new PListGreaterThan[A :>: B, PNil] {}


  implicit def gtEvidence[A, B <: PList, C, D <: PList](implicit
    precedenceEvidence: Precedence[A :>: B],
    precedenceEvidence2: Precedence[C :>: D],
    pListGreaterThanEvidence: PListGreaterThan[B, D],
  ): PrecedenceGreaterThan[A, C] = {
    new PrecedenceGreaterThan[A, C] {}
  }
}
