package monad.liberator.util

object GeneratePrecedenceCode extends App {

  for {
    inputTypeCount <- 1 to 22
    outputTypeCount <- 1 until inputTypeCount
  } yield {
    val typeParams = (1 to inputTypeCount).map(i => s"A$i").mkString(", ")
    val implicitParam = (1 to inputTypeCount).map(i => s"A$i").mkString(" :>: ")
    val outputType = (1 to outputTypeCount).map(i => s"A${i + inputTypeCount - outputTypeCount}").mkString(" :>: ")
    println(s"implicit def precedenceTail_${inputTypeCount}_$outputTypeCount[$typeParams](implicit precedence: Precedence[${implicitParam} :>: PNil]): Precedence[${outputType} :>: PNil] = new Precedence[${outputType} :>: PNil] {}")
  }

}
