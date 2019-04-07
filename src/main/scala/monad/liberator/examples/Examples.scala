package monad.liberator.examples

import monad.liberator.MonadLiberator

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object Examples extends App with MonadLiberator[String] {

  val result: Future[List[Option[Int]]] = for {
    a <- List(1, 2, 3, 4).!?
    b <- Future(1).!?
    c <- Option(2).!?
  } yield a + b + c

  println(result)

}
