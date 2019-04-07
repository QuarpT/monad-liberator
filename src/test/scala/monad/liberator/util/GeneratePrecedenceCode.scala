package monad.liberator.util

object GeneratePrecedenceCode extends App {

  for {
    inputTypeCount <- 2 to 10
    outputTypeCount <- 1 until inputTypeCount
  } yield {
    if (outputTypeCount == 1)
      println(s"trait PrecedenceTailImplicits$inputTypeCount extends PrecedenceTailImplicits${inputTypeCount + 1} {")

    val typeParams = (1 to inputTypeCount).map(i => s"A$i").mkString(", ")
    val implicitParam = (1 to inputTypeCount).map(i => s"A$i").mkString(" :>>: ")
    val outputType = (1 to outputTypeCount).map(i => s"A${i + inputTypeCount - outputTypeCount}").mkString(" :>>: ")

    println(s"  implicit def precedenceTail_${inputTypeCount}_$outputTypeCount[$typeParams](implicit precedence: Precedence[${implicitParam} :>>: PNil]): Precedence[${outputType} :>>: PNil] = new Precedence[${outputType} :>>: PNil] {}")

    if (outputTypeCount == inputTypeCount - 1)
      println(s"}\n")
  }

}
