package monad.liberator

import monad.liberator.examples._
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{FlatSpec, Matchers}

class MonadLiberatorTest extends FlatSpec with ScalaFutures with Matchers {

  "The examples" should "return the expected results" in {
    whenReady(BasicExample.result)(_ shouldBe List(None, Some(3), None, Some(5)))
    whenReady(ComplexExample.result)(_ shouldBe List(Left("3 is not even" ), Right(Some(4))))
    whenReady(ExampleChangingPrecedence.result)(_ shouldBe None)
    whenReady(DeepFlattenExample.result)(_ shouldBe List(Right(Some(5))))
    whenReady(DeepMapExample.result)(_ shouldBe List(Right(Some(10))))
  }

}
