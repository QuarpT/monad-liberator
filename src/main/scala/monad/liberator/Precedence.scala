package monad.liberator

import scala.language.higherKinds

sealed trait PList

final case class :>>:[+H, +T <: PList](head: H, tail: T) extends PList

sealed trait PNil extends PList {
  def :>>:[H](h: H) = monad.liberator.:>>:(h, this)
}

case object PNil extends PNil

class Precedence[+A <: PList]

trait PrecedenceEvidence[A, B, C] {
  type Aux = C
  def apply(a: A, b: B): C
}

class EvidenceOf[A]

object PrecedenceEvidence {
  def apply[A, B, C](implicit pe: PrecedenceEvidence[A, B, C]) = pe
}

class PrecedenceGreaterThan[A, B]

object PrecedenceGreaterThan {
  def apply[A,B]()(implicit pgt: PrecedenceGreaterThan[A, B]) = pgt
}

trait PListGreaterThan[A, B]

trait PrecedenceLowPriorityImplicits {
  implicit def pListGtEvidence[A, B <: PList, C, D <: PList](implicit ev1: PListGreaterThan[B, D]): PListGreaterThan[A :>>: B, C :>>: D] = {
    new PListGreaterThan[A :>>: B, C :>>: D] {}
  }


  implicit def precedenceEvidenceRule[A, B, C <: PList, D](implicit pe: PrecedenceEvidence[EvidenceOf[A], Precedence[C], D]): PrecedenceEvidence[EvidenceOf[A], Precedence[B :>>: C], D] = new PrecedenceEvidence[EvidenceOf[A], Precedence[B :>>: C], D] {
    override def apply(a: EvidenceOf[A], b: Precedence[B :>>: C]): D =  {
      pe(a, new Precedence[C])
    }
  }
}

trait PrecedenceImplicits extends PrecedenceLowPriorityImplicits with PrecedenceTailImplicits {

  implicit def hListBaseGtEvidence[A, B <: PList]: PListGreaterThan[A :>>: B, PNil] =
    new PListGreaterThan[A :>>: B, PNil] {}


  implicit def greaterThanEvidenceRule[P <: PList, A, B, C, D](implicit
    precedence: Precedence[P],
    precedenceEvidence: PrecedenceEvidence[EvidenceOf[A], Precedence[P], B],
    precedenceEvidence2: PrecedenceEvidence[EvidenceOf[C], Precedence[P], D],
//    pListGreaterThanEvidence: PListGreaterThan[B, D]
  ): PrecedenceGreaterThan[A, C] = {
    new PrecedenceGreaterThan[A, C]
  }

  implicit def precedenceEvidenceBase[A, B <: PList] = new PrecedenceEvidence[EvidenceOf[A], Precedence[A :>>: B], Precedence[A :>>: B]] {
    override def apply(a: EvidenceOf[A], b: Precedence[A :>>: B]): Precedence[A :>>: B] = b
  }
}
