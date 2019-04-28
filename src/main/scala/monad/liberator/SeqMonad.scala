package monad.liberator

import cats._

import scala.annotation.tailrec

// Taken from cats source
trait SeqMonad {
  implicit val seqMonad = new Monad[Seq] with Traverse[Seq] {
    override def pure[A](x: A): Seq[A] = Vector(x)

    override def flatMap[A, B](fa: Seq[A])(f: A => Seq[B]): Seq[B] = fa.flatMap(f)

    def tailRecM[A, B](a: A)(fn: A => Seq[Either[A, B]]): Seq[B] = {
      val buf = Vector.newBuilder[B]
      var state = List(fn(a).iterator)

      @tailrec
      def loop(): Unit = state match {
        case Nil => ()
        case h :: tail if h.isEmpty =>
          state = tail
          loop()
        case h :: tail =>
          h.next match {
            case Right(b) =>
              buf += b
              loop()
            case Left(l) =>
              state = fn(l).iterator :: h :: tail
              loop()
          }
      }

      loop()
      buf.result
    }

    override def traverse[G[_], A, B](fa: Seq[A])(f: A => G[B])(implicit G: Applicative[G]): G[Seq[B]] =
      foldRight[A, G[Seq[B]]](fa, Always(G.pure(Vector.empty))) { (a, lgvb) =>
        G.map2Eval(f(a), lgvb)(_ +: _)
      }.value

    def foldLeft[A, B](fa: Seq[A], b: B)(f: (B, A) => B): B =
      fa.foldLeft(b)(f)

    def foldRight[A, B](fa: Seq[A], lb: Eval[B])(f: (A, Eval[B]) => Eval[B]): Eval[B] = {
      def loop(i: Int): Eval[B] =
        if (i < fa.length) f(fa(i), Eval.defer(loop(i + 1))) else lb
      Eval.defer(loop(0))
    }
  }
}
