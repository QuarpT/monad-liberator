package monad.liberator

import scala.language.higherKinds

trait DeepMonadImplicits {

  implicit class DeepMonad[AA, A, B](m: AA)(implicit mppr: MonadTypeWitnessRecur[AA, A], deepNestedType: DeepNestedType[A, B]) {
    def M = this
    def dm = this
    def deepFlatMap[C, D, E](f: B => C)(implicit dm: DeepMap[A, B, C, D], dft: DeepFlattenTraverseRules[D, E]): E = dft(dm(mppr(m))(f))
    def deepMap[C, D, E](f: B => C)(implicit dm: DeepMap[A, B, C, D], dft: DeepFlattenTraverseRules[D, E]): E = deepFlatMap(f)
    def map[C, D, E](f: B => C)(implicit dm: DeepMap[A, B, C, D], dft: DeepFlattenTraverseRules[D, E]): E = deepFlatMap(f)
    def flatMap[C, D, E](f: B => C)(implicit dm: DeepMap[A, B, C, D], dft: DeepFlattenTraverseRules[D, E]): E = deepFlatMap(f)
  }

}
