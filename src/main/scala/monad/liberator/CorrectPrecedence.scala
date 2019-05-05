package monad.liberator

import scala.language.higherKinds

trait CorrectPrecedence[A, P]

trait CorrectPrecedenceHelper[A, P, B]

trait CorrectPrecedenceLowPriorityImplicits {
  implicit def correctPrecedenceBase[A, B, P <: PList](implicit
    inner: DeepNestedType[A, B],
    ev: A =:= B
  ) = new CorrectPrecedence[A, Precedence[P]] {}

  implicit def correctPrecedenceHelperBase[A, P <: PList] = new CorrectPrecedenceHelper[A, Precedence[P], A] {}
}

trait CorrectPrecedenceImplicits extends CorrectPrecedenceLowPriorityImplicits {
  implicit def correctPrecedenceRule[F[_], A, P <: PList, Q, B](implicit
    precedence: Precedence[P],
    precedenceEvidence: PrecedenceEvidence[EvidenceOf[F[_]], Precedence[P], Q],
    inner: DeepNestedType[F[A], B],
    correctPrecedence: CorrectPrecedenceHelper[A, Q, B]
  ) = new CorrectPrecedence[F[A], Precedence[P]] {}

  implicit def correctPrecedenceHelperRule[F[_], A, PHead, P <: PList, Q, B](implicit
    precedenceEvidence: PrecedenceEvidence[EvidenceOf[F[_]], Precedence[P], Q],
    correctPrecedence: CorrectPrecedenceHelper[A, Q, B]
  ) = new CorrectPrecedenceHelper[F[A], Precedence[P], B] {}
}

object CorrectPrecedence {
  def apply[A, P, B](a: A)(implicit correctPrecedence: CorrectPrecedence[A, P]): CorrectPrecedence[A, P] = correctPrecedence
}
