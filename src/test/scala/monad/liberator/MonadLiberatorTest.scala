package monad.liberator

import monad.liberator.examples._
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{FlatSpec, Matchers}

class MonadLiberatorTest extends FlatSpec with ScalaFutures with Matchers {

  "The examples" should "return the expected results" in {
    whenReady(BasicExample.result)(_ shouldBe Seq(None, Some(3), None, Some(5)))
    whenReady(ComplexExample.result)(_ shouldBe Seq(Left("3 is not even" ), Right(Some(4))))
    whenReady(ExampleChangingPrecedence.result)(_ shouldBe Seq(Some(Right(6)), Some(Left("hi"))))
    whenReady(DeepFlattenExample.result)(_ shouldBe Seq(Right(Some(5))))
    whenReady(DeepMapExample.result)(_ shouldBe Seq(Right(Some(10))))
  }

}
