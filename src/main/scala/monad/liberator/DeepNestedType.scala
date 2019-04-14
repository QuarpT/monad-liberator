package monad.liberator

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
  implicit def deepNestedTypeRule[A[_], B, C](implicit
    precedence: Precedence[A[_] :>>: PList],
    deepNestedType: DeepNestedType[B, C]) = new DeepNestedType[A[B], C] {}
}
