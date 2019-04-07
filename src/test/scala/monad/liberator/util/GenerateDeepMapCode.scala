package monad.liberator.util

object GenerateDeepMapCode extends App {

  for {
    inputTypeCount <- 1 to 8
  } yield {
    val inputTypes = (1 to inputTypeCount).map(i => s"F$i[_] : Functor").mkString(", ")
    val composeStr = (1 to inputTypeCount).map(i => s"[F$i]").mkString(".compose")

    def wrapTypes(inner: String): String = (1 to inputTypeCount).foldRight(inner)((i, s) => s"F$i[$s]")

    println(
      s"""implicit def deepMapRule$inputTypeCount[$inputTypes, B] = new DeepFlatMap[Lambda[X => ${wrapTypes("X")}], B] {
         |  override def apply[C](a: ${wrapTypes("B")})(f: B => C): ${wrapTypes("C")} = {
         |    Functor${composeStr}.map(a)(f)
         |  }
         |}
         |""".stripMargin)


  }
}
