package monad.liberator

/*
 *
 * Note, the Scala compiler's implicit heuristics do not always work with the the simple implicit:
 * implicit def precedenceTail[A, B <: PList](implicit precedence: Precedence[A :: B]) = new Precedence[B] {}
 *
 * As a workaround I have generated this code using:
 * monad-precedence/src/test/scala/monad/liberator/util/GeneratePrecedenceCode.scala
 *
 * I would like to improve this.
 *
 * Peter Colley
 *
 */

trait PrecedenceTailImplicits extends PrecedenceTailImplicits2

trait PrecedenceTailImplicits2 extends PrecedenceTailImplicits3 {
  implicit def precedenceTail_2_1[A1, A2](implicit precedence: Precedence[A1 :>>: A2 :>>: PNil]): Precedence[A2 :>>: PNil] = new Precedence[A2 :>>: PNil] {}
}

trait PrecedenceTailImplicits3 extends PrecedenceTailImplicits4 {
  implicit def precedenceTail_3_1[A1, A2, A3](implicit precedence: Precedence[A1 :>>: A2 :>>: A3 :>>: PNil]): Precedence[A3 :>>: PNil] = new Precedence[A3 :>>: PNil] {}
  implicit def precedenceTail_3_2[A1, A2, A3](implicit precedence: Precedence[A1 :>>: A2 :>>: A3 :>>: PNil]): Precedence[A2 :>>: A3 :>>: PNil] = new Precedence[A2 :>>: A3 :>>: PNil] {}
}

trait PrecedenceTailImplicits4 extends PrecedenceTailImplicits5 {
  implicit def precedenceTail_4_1[A1, A2, A3, A4](implicit precedence: Precedence[A1 :>>: A2 :>>: A3 :>>: A4 :>>: PNil]): Precedence[A4 :>>: PNil] = new Precedence[A4 :>>: PNil] {}
  implicit def precedenceTail_4_2[A1, A2, A3, A4](implicit precedence: Precedence[A1 :>>: A2 :>>: A3 :>>: A4 :>>: PNil]): Precedence[A3 :>>: A4 :>>: PNil] = new Precedence[A3 :>>: A4 :>>: PNil] {}
  implicit def precedenceTail_4_3[A1, A2, A3, A4](implicit precedence: Precedence[A1 :>>: A2 :>>: A3 :>>: A4 :>>: PNil]): Precedence[A2 :>>: A3 :>>: A4 :>>: PNil] = new Precedence[A2 :>>: A3 :>>: A4 :>>: PNil] {}
}

trait PrecedenceTailImplicits5 extends PrecedenceTailImplicits6 {
  implicit def precedenceTail_5_1[A1, A2, A3, A4, A5](implicit precedence: Precedence[A1 :>>: A2 :>>: A3 :>>: A4 :>>: A5 :>>: PNil]): Precedence[A5 :>>: PNil] = new Precedence[A5 :>>: PNil] {}
  implicit def precedenceTail_5_2[A1, A2, A3, A4, A5](implicit precedence: Precedence[A1 :>>: A2 :>>: A3 :>>: A4 :>>: A5 :>>: PNil]): Precedence[A4 :>>: A5 :>>: PNil] = new Precedence[A4 :>>: A5 :>>: PNil] {}
  implicit def precedenceTail_5_3[A1, A2, A3, A4, A5](implicit precedence: Precedence[A1 :>>: A2 :>>: A3 :>>: A4 :>>: A5 :>>: PNil]): Precedence[A3 :>>: A4 :>>: A5 :>>: PNil] = new Precedence[A3 :>>: A4 :>>: A5 :>>: PNil] {}
  implicit def precedenceTail_5_4[A1, A2, A3, A4, A5](implicit precedence: Precedence[A1 :>>: A2 :>>: A3 :>>: A4 :>>: A5 :>>: PNil]): Precedence[A2 :>>: A3 :>>: A4 :>>: A5 :>>: PNil] = new Precedence[A2 :>>: A3 :>>: A4 :>>: A5 :>>: PNil] {}
}

trait PrecedenceTailImplicits6 extends PrecedenceTailImplicits7 {
  implicit def precedenceTail_6_1[A1, A2, A3, A4, A5, A6](implicit precedence: Precedence[A1 :>>: A2 :>>: A3 :>>: A4 :>>: A5 :>>: A6 :>>: PNil]): Precedence[A6 :>>: PNil] = new Precedence[A6 :>>: PNil] {}
  implicit def precedenceTail_6_2[A1, A2, A3, A4, A5, A6](implicit precedence: Precedence[A1 :>>: A2 :>>: A3 :>>: A4 :>>: A5 :>>: A6 :>>: PNil]): Precedence[A5 :>>: A6 :>>: PNil] = new Precedence[A5 :>>: A6 :>>: PNil] {}
  implicit def precedenceTail_6_3[A1, A2, A3, A4, A5, A6](implicit precedence: Precedence[A1 :>>: A2 :>>: A3 :>>: A4 :>>: A5 :>>: A6 :>>: PNil]): Precedence[A4 :>>: A5 :>>: A6 :>>: PNil] = new Precedence[A4 :>>: A5 :>>: A6 :>>: PNil] {}
  implicit def precedenceTail_6_4[A1, A2, A3, A4, A5, A6](implicit precedence: Precedence[A1 :>>: A2 :>>: A3 :>>: A4 :>>: A5 :>>: A6 :>>: PNil]): Precedence[A3 :>>: A4 :>>: A5 :>>: A6 :>>: PNil] = new Precedence[A3 :>>: A4 :>>: A5 :>>: A6 :>>: PNil] {}
  implicit def precedenceTail_6_5[A1, A2, A3, A4, A5, A6](implicit precedence: Precedence[A1 :>>: A2 :>>: A3 :>>: A4 :>>: A5 :>>: A6 :>>: PNil]): Precedence[A2 :>>: A3 :>>: A4 :>>: A5 :>>: A6 :>>: PNil] = new Precedence[A2 :>>: A3 :>>: A4 :>>: A5 :>>: A6 :>>: PNil] {}
}

trait PrecedenceTailImplicits7 extends PrecedenceTailImplicits8 {
  implicit def precedenceTail_7_1[A1, A2, A3, A4, A5, A6, A7](implicit precedence: Precedence[A1 :>>: A2 :>>: A3 :>>: A4 :>>: A5 :>>: A6 :>>: A7 :>>: PNil]): Precedence[A7 :>>: PNil] = new Precedence[A7 :>>: PNil] {}
  implicit def precedenceTail_7_2[A1, A2, A3, A4, A5, A6, A7](implicit precedence: Precedence[A1 :>>: A2 :>>: A3 :>>: A4 :>>: A5 :>>: A6 :>>: A7 :>>: PNil]): Precedence[A6 :>>: A7 :>>: PNil] = new Precedence[A6 :>>: A7 :>>: PNil] {}
  implicit def precedenceTail_7_3[A1, A2, A3, A4, A5, A6, A7](implicit precedence: Precedence[A1 :>>: A2 :>>: A3 :>>: A4 :>>: A5 :>>: A6 :>>: A7 :>>: PNil]): Precedence[A5 :>>: A6 :>>: A7 :>>: PNil] = new Precedence[A5 :>>: A6 :>>: A7 :>>: PNil] {}
  implicit def precedenceTail_7_4[A1, A2, A3, A4, A5, A6, A7](implicit precedence: Precedence[A1 :>>: A2 :>>: A3 :>>: A4 :>>: A5 :>>: A6 :>>: A7 :>>: PNil]): Precedence[A4 :>>: A5 :>>: A6 :>>: A7 :>>: PNil] = new Precedence[A4 :>>: A5 :>>: A6 :>>: A7 :>>: PNil] {}
  implicit def precedenceTail_7_5[A1, A2, A3, A4, A5, A6, A7](implicit precedence: Precedence[A1 :>>: A2 :>>: A3 :>>: A4 :>>: A5 :>>: A6 :>>: A7 :>>: PNil]): Precedence[A3 :>>: A4 :>>: A5 :>>: A6 :>>: A7 :>>: PNil] = new Precedence[A3 :>>: A4 :>>: A5 :>>: A6 :>>: A7 :>>: PNil] {}
  implicit def precedenceTail_7_6[A1, A2, A3, A4, A5, A6, A7](implicit precedence: Precedence[A1 :>>: A2 :>>: A3 :>>: A4 :>>: A5 :>>: A6 :>>: A7 :>>: PNil]): Precedence[A2 :>>: A3 :>>: A4 :>>: A5 :>>: A6 :>>: A7 :>>: PNil] = new Precedence[A2 :>>: A3 :>>: A4 :>>: A5 :>>: A6 :>>: A7 :>>: PNil] {}
}

trait PrecedenceTailImplicits8 extends PrecedenceTailImplicits9 {
  implicit def precedenceTail_8_1[A1, A2, A3, A4, A5, A6, A7, A8](implicit precedence: Precedence[A1 :>>: A2 :>>: A3 :>>: A4 :>>: A5 :>>: A6 :>>: A7 :>>: A8 :>>: PNil]): Precedence[A8 :>>: PNil] = new Precedence[A8 :>>: PNil] {}
  implicit def precedenceTail_8_2[A1, A2, A3, A4, A5, A6, A7, A8](implicit precedence: Precedence[A1 :>>: A2 :>>: A3 :>>: A4 :>>: A5 :>>: A6 :>>: A7 :>>: A8 :>>: PNil]): Precedence[A7 :>>: A8 :>>: PNil] = new Precedence[A7 :>>: A8 :>>: PNil] {}
  implicit def precedenceTail_8_3[A1, A2, A3, A4, A5, A6, A7, A8](implicit precedence: Precedence[A1 :>>: A2 :>>: A3 :>>: A4 :>>: A5 :>>: A6 :>>: A7 :>>: A8 :>>: PNil]): Precedence[A6 :>>: A7 :>>: A8 :>>: PNil] = new Precedence[A6 :>>: A7 :>>: A8 :>>: PNil] {}
  implicit def precedenceTail_8_4[A1, A2, A3, A4, A5, A6, A7, A8](implicit precedence: Precedence[A1 :>>: A2 :>>: A3 :>>: A4 :>>: A5 :>>: A6 :>>: A7 :>>: A8 :>>: PNil]): Precedence[A5 :>>: A6 :>>: A7 :>>: A8 :>>: PNil] = new Precedence[A5 :>>: A6 :>>: A7 :>>: A8 :>>: PNil] {}
  implicit def precedenceTail_8_5[A1, A2, A3, A4, A5, A6, A7, A8](implicit precedence: Precedence[A1 :>>: A2 :>>: A3 :>>: A4 :>>: A5 :>>: A6 :>>: A7 :>>: A8 :>>: PNil]): Precedence[A4 :>>: A5 :>>: A6 :>>: A7 :>>: A8 :>>: PNil] = new Precedence[A4 :>>: A5 :>>: A6 :>>: A7 :>>: A8 :>>: PNil] {}
  implicit def precedenceTail_8_6[A1, A2, A3, A4, A5, A6, A7, A8](implicit precedence: Precedence[A1 :>>: A2 :>>: A3 :>>: A4 :>>: A5 :>>: A6 :>>: A7 :>>: A8 :>>: PNil]): Precedence[A3 :>>: A4 :>>: A5 :>>: A6 :>>: A7 :>>: A8 :>>: PNil] = new Precedence[A3 :>>: A4 :>>: A5 :>>: A6 :>>: A7 :>>: A8 :>>: PNil] {}
  implicit def precedenceTail_8_7[A1, A2, A3, A4, A5, A6, A7, A8](implicit precedence: Precedence[A1 :>>: A2 :>>: A3 :>>: A4 :>>: A5 :>>: A6 :>>: A7 :>>: A8 :>>: PNil]): Precedence[A2 :>>: A3 :>>: A4 :>>: A5 :>>: A6 :>>: A7 :>>: A8 :>>: PNil] = new Precedence[A2 :>>: A3 :>>: A4 :>>: A5 :>>: A6 :>>: A7 :>>: A8 :>>: PNil] {}
}

trait PrecedenceTailImplicits9 extends PrecedenceTailImplicits10 {
  implicit def precedenceTail_9_1[A1, A2, A3, A4, A5, A6, A7, A8, A9](implicit precedence: Precedence[A1 :>>: A2 :>>: A3 :>>: A4 :>>: A5 :>>: A6 :>>: A7 :>>: A8 :>>: A9 :>>: PNil]): Precedence[A9 :>>: PNil] = new Precedence[A9 :>>: PNil] {}
  implicit def precedenceTail_9_2[A1, A2, A3, A4, A5, A6, A7, A8, A9](implicit precedence: Precedence[A1 :>>: A2 :>>: A3 :>>: A4 :>>: A5 :>>: A6 :>>: A7 :>>: A8 :>>: A9 :>>: PNil]): Precedence[A8 :>>: A9 :>>: PNil] = new Precedence[A8 :>>: A9 :>>: PNil] {}
  implicit def precedenceTail_9_3[A1, A2, A3, A4, A5, A6, A7, A8, A9](implicit precedence: Precedence[A1 :>>: A2 :>>: A3 :>>: A4 :>>: A5 :>>: A6 :>>: A7 :>>: A8 :>>: A9 :>>: PNil]): Precedence[A7 :>>: A8 :>>: A9 :>>: PNil] = new Precedence[A7 :>>: A8 :>>: A9 :>>: PNil] {}
  implicit def precedenceTail_9_4[A1, A2, A3, A4, A5, A6, A7, A8, A9](implicit precedence: Precedence[A1 :>>: A2 :>>: A3 :>>: A4 :>>: A5 :>>: A6 :>>: A7 :>>: A8 :>>: A9 :>>: PNil]): Precedence[A6 :>>: A7 :>>: A8 :>>: A9 :>>: PNil] = new Precedence[A6 :>>: A7 :>>: A8 :>>: A9 :>>: PNil] {}
  implicit def precedenceTail_9_5[A1, A2, A3, A4, A5, A6, A7, A8, A9](implicit precedence: Precedence[A1 :>>: A2 :>>: A3 :>>: A4 :>>: A5 :>>: A6 :>>: A7 :>>: A8 :>>: A9 :>>: PNil]): Precedence[A5 :>>: A6 :>>: A7 :>>: A8 :>>: A9 :>>: PNil] = new Precedence[A5 :>>: A6 :>>: A7 :>>: A8 :>>: A9 :>>: PNil] {}
  implicit def precedenceTail_9_6[A1, A2, A3, A4, A5, A6, A7, A8, A9](implicit precedence: Precedence[A1 :>>: A2 :>>: A3 :>>: A4 :>>: A5 :>>: A6 :>>: A7 :>>: A8 :>>: A9 :>>: PNil]): Precedence[A4 :>>: A5 :>>: A6 :>>: A7 :>>: A8 :>>: A9 :>>: PNil] = new Precedence[A4 :>>: A5 :>>: A6 :>>: A7 :>>: A8 :>>: A9 :>>: PNil] {}
  implicit def precedenceTail_9_7[A1, A2, A3, A4, A5, A6, A7, A8, A9](implicit precedence: Precedence[A1 :>>: A2 :>>: A3 :>>: A4 :>>: A5 :>>: A6 :>>: A7 :>>: A8 :>>: A9 :>>: PNil]): Precedence[A3 :>>: A4 :>>: A5 :>>: A6 :>>: A7 :>>: A8 :>>: A9 :>>: PNil] = new Precedence[A3 :>>: A4 :>>: A5 :>>: A6 :>>: A7 :>>: A8 :>>: A9 :>>: PNil] {}
  implicit def precedenceTail_9_8[A1, A2, A3, A4, A5, A6, A7, A8, A9](implicit precedence: Precedence[A1 :>>: A2 :>>: A3 :>>: A4 :>>: A5 :>>: A6 :>>: A7 :>>: A8 :>>: A9 :>>: PNil]): Precedence[A2 :>>: A3 :>>: A4 :>>: A5 :>>: A6 :>>: A7 :>>: A8 :>>: A9 :>>: PNil] = new Precedence[A2 :>>: A3 :>>: A4 :>>: A5 :>>: A6 :>>: A7 :>>: A8 :>>: A9 :>>: PNil] {}
}

trait PrecedenceTailImplicits10 {
  implicit def precedenceTail_10_1[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10](implicit precedence: Precedence[A1 :>>: A2 :>>: A3 :>>: A4 :>>: A5 :>>: A6 :>>: A7 :>>: A8 :>>: A9 :>>: A10 :>>: PNil]): Precedence[A10 :>>: PNil] = new Precedence[A10 :>>: PNil] {}
  implicit def precedenceTail_10_2[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10](implicit precedence: Precedence[A1 :>>: A2 :>>: A3 :>>: A4 :>>: A5 :>>: A6 :>>: A7 :>>: A8 :>>: A9 :>>: A10 :>>: PNil]): Precedence[A9 :>>: A10 :>>: PNil] = new Precedence[A9 :>>: A10 :>>: PNil] {}
  implicit def precedenceTail_10_3[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10](implicit precedence: Precedence[A1 :>>: A2 :>>: A3 :>>: A4 :>>: A5 :>>: A6 :>>: A7 :>>: A8 :>>: A9 :>>: A10 :>>: PNil]): Precedence[A8 :>>: A9 :>>: A10 :>>: PNil] = new Precedence[A8 :>>: A9 :>>: A10 :>>: PNil] {}
  implicit def precedenceTail_10_4[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10](implicit precedence: Precedence[A1 :>>: A2 :>>: A3 :>>: A4 :>>: A5 :>>: A6 :>>: A7 :>>: A8 :>>: A9 :>>: A10 :>>: PNil]): Precedence[A7 :>>: A8 :>>: A9 :>>: A10 :>>: PNil] = new Precedence[A7 :>>: A8 :>>: A9 :>>: A10 :>>: PNil] {}
  implicit def precedenceTail_10_5[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10](implicit precedence: Precedence[A1 :>>: A2 :>>: A3 :>>: A4 :>>: A5 :>>: A6 :>>: A7 :>>: A8 :>>: A9 :>>: A10 :>>: PNil]): Precedence[A6 :>>: A7 :>>: A8 :>>: A9 :>>: A10 :>>: PNil] = new Precedence[A6 :>>: A7 :>>: A8 :>>: A9 :>>: A10 :>>: PNil] {}
  implicit def precedenceTail_10_6[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10](implicit precedence: Precedence[A1 :>>: A2 :>>: A3 :>>: A4 :>>: A5 :>>: A6 :>>: A7 :>>: A8 :>>: A9 :>>: A10 :>>: PNil]): Precedence[A5 :>>: A6 :>>: A7 :>>: A8 :>>: A9 :>>: A10 :>>: PNil] = new Precedence[A5 :>>: A6 :>>: A7 :>>: A8 :>>: A9 :>>: A10 :>>: PNil] {}
  implicit def precedenceTail_10_7[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10](implicit precedence: Precedence[A1 :>>: A2 :>>: A3 :>>: A4 :>>: A5 :>>: A6 :>>: A7 :>>: A8 :>>: A9 :>>: A10 :>>: PNil]): Precedence[A4 :>>: A5 :>>: A6 :>>: A7 :>>: A8 :>>: A9 :>>: A10 :>>: PNil] = new Precedence[A4 :>>: A5 :>>: A6 :>>: A7 :>>: A8 :>>: A9 :>>: A10 :>>: PNil] {}
  implicit def precedenceTail_10_8[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10](implicit precedence: Precedence[A1 :>>: A2 :>>: A3 :>>: A4 :>>: A5 :>>: A6 :>>: A7 :>>: A8 :>>: A9 :>>: A10 :>>: PNil]): Precedence[A3 :>>: A4 :>>: A5 :>>: A6 :>>: A7 :>>: A8 :>>: A9 :>>: A10 :>>: PNil] = new Precedence[A3 :>>: A4 :>>: A5 :>>: A6 :>>: A7 :>>: A8 :>>: A9 :>>: A10 :>>: PNil] {}
  implicit def precedenceTail_10_9[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10](implicit precedence: Precedence[A1 :>>: A2 :>>: A3 :>>: A4 :>>: A5 :>>: A6 :>>: A7 :>>: A8 :>>: A9 :>>: A10 :>>: PNil]): Precedence[A2 :>>: A3 :>>: A4 :>>: A5 :>>: A6 :>>: A7 :>>: A8 :>>: A9 :>>: A10 :>>: PNil] = new Precedence[A2 :>>: A3 :>>: A4 :>>: A5 :>>: A6 :>>: A7 :>>: A8 :>>: A9 :>>: A10 :>>: PNil] {}
}

