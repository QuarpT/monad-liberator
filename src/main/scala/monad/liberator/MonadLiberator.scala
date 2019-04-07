package monad.liberator

import cats._
import scala.concurrent.Future
import scala.util.Try

/*
 *
 * It should be possible to refactor out the EitherLeftType,
 * and providing a solution for DeepMonadImplicits for Monad types with extra fixed type parameters
 *
 */
trait MonadLiberator[EitherLeftType] extends DeepMonadImplicits
  with DeepMapImplicits
  with DeepFlattenTraverseImplicits
  with PrecedenceImplicits
  with CatsImplicits {

  type Either[R] = scala.Either[EitherLeftType, R]
  def Right[A](value: A): Either[A] = scala.Right(value)
  def Left(value: EitherLeftType): Either[EitherLeftType] = scala.Left(value)
  implicit val defaultPrecedence = new Precedence[Future[_] :>>: List[_] :>>: Either[_] :>>: Try[_] :>>:  Option[_] :>>: PNil] {}
}

trait CatsImplicits extends syntax.AllSyntax
    with syntax.AllSyntaxBinCompat0
    with syntax.AllSyntaxBinCompat1
    with syntax.AllSyntaxBinCompat2
    with syntax.AllSyntaxBinCompat3
    with syntax.AllSyntaxBinCompat4
    with instances.AllInstances
    with instances.AllInstancesBinCompat0
    with instances.AllInstancesBinCompat1
    with instances.AllInstancesBinCompat2
    with instances.AllInstancesBinCompat3
