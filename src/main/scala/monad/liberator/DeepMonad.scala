package monad.liberator

import scala.language.higherKinds

trait DeepMonadImplicits {

  implicit class DeepMonad[A, B](m: A)(implicit deepNestedType: DeepNestedType[A, B]) {
    def !? = this
    def dm = this
    def deepFlatMap[C, D, E](f: B => C)(implicit dm: DeepMap[A, B, C, D], dft: DeepFlattenTraverse[D, E]): E = dft(dm(m)(f))
    def deepMap[C, D, E](f: B => C)(implicit dm: DeepMap[A, B, C, D], dft: DeepFlattenTraverse[D, E]): E = deepFlatMap(f)
    def map[C, D, E](f: B => C)(implicit dm: DeepMap[A, B, C, D], dft: DeepFlattenTraverse[D, E]): E = deepFlatMap(f)
    def flatMap[C, D, E](f: B => C)(implicit dm: DeepMap[A, B, C, D], dft: DeepFlattenTraverse[D, E]): E = deepFlatMap(f)
  }

}
