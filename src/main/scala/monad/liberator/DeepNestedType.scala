package monad.liberator

import scala.language.higherKinds

trait DeepNestedType[A, B] {
  type Aux = B
}

object DeepNestedType {
  def apply[A, B](a: A)(implicit deepNestedType: DeepNestedType[A, B]): DeepNestedType[A, B] = deepNestedType
}

trait DeepNestedTypeLowPriorityImplicits {
  implicit def deepNestedTypeBase[A] = new DeepNestedType[A, A] {}
}

trait DeepNestedTypeImplicits extends DeepNestedTypeLowPriorityImplicits {
  implicit def deepNestedTypeRule[P <: PList, A[_], AA, B, C](implicit
    precedence: Precedence[P],
    precedenceEvidence: PrecedenceEvidence[EvidenceOf[A[_]], Precedence[P], AA],
    deepNestedType: DeepNestedType[B, C]) = new DeepNestedType[A[B], C] {}
}
