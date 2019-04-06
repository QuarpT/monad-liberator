package monad.liberator.util

/**
  *
  */
object GenerateDeepMonadCode extends App {
  for {
    inputTypeCount <- 1 to 8
  } yield {
    val inputTypes = (1 to inputTypeCount).map(i => s"F$i[_]").mkString(", ")
    def wrapTypes(inner: String): String = (1 to inputTypeCount).foldRight(inner)((i, s) => s"F$i[$s]")
    println(
      s"""implicit class DeepMonad$inputTypeCount[$inputTypes, A](functor: ${wrapTypes("A")})(implicit dfm: DeepMap[Lambda[X => ${wrapTypes("X")}], A]) {
        |  def !? = this
        |  def dm = this
        |  def dm$inputTypeCount = this
        |  def deepMap[B](f: A => B): ${wrapTypes("B")} = dfm(functor)(f)
        |  def deepFlatMap[B, C](f: A => B)(implicit dft: DeepFlattenTraverse[${wrapTypes("B")}, C]): C = dft(dfm(functor)(f))
        |  def flatMap[B, C](f: A => B)(implicit dft: DeepFlattenTraverse[${wrapTypes("B")}, C]): C = deepFlatMap(f)
        |  def map[B, C](f: A => B)(implicit dft: DeepFlattenTraverse[${wrapTypes("B")}, C]): C = deepFlatMap(f)
        |}
        |""".stripMargin)
  }
}
