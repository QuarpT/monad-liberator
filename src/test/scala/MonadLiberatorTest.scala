import monad.liberator.MonadLiberator
import monad.liberator.examples._
import org.scalatest.{FlatSpec, Matchers}
import org.scalatest.concurrent.ScalaFutures

class MonadLiberatorTest extends FlatSpec with  MonadLiberator[String] with ScalaFutures with Matchers {

  "The examples" should "return the expected results" in {
    whenReady(BasicExample.result)(_ shouldBe List(None, Some(3), None, Some(5)))
    whenReady(ExampleExplicitlyIndicatingMonadDepth.result)(_ shouldBe List(None, Some(3), None, Some(5)))
    whenReady(ComplexExample.result)(_ shouldBe List(Left("3 is not even" ), Right(Some(4))))
    whenReady(ExampleChangingPrecedence.result)(_ shouldBe None)
  }

}
