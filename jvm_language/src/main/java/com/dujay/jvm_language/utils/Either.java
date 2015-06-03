package com.dujay.jvm_language.utils;

import java.util.function.Function;

public class Either<A, B> {

  private A left = null;
  private B right = null;

  private Either(A a, B b) {
    left = a;
    right = b;
  }

  public static <A, B> Either<A, B> left(A a) {
    return new Either<A, B>(a, null);
  }

  public static <A, B> Either<A, B> right(B b) {
    return new Either<A, B>(null, b);
  }

  /* Here's the important part: */
  public <C, D> Either<C, D> fold(Function<A, C> ifLeft, Function<B, D> ifRight) {
    if (left != null) {
      return Either.left(ifLeft.apply(left));
    } else {
      return Either.right(ifRight.apply(right));
    }
  }

  /**
   * Control.Arrow.left equivalent in Haskell
   * @param ifLeft
   * @return
   */
  public <C> Either<C, B> leftMap(Function<A, Either<C, B>> ifLeft) {
    return fold(ifLeft.andThen(x -> x.left), x -> x);
  }

  /**
   * Control.Arrow.right equivalent in Haskell
   * @param ifRight
   * @return
   */
  public <D> Either<A, D> rightMap(Function<B, Either<A, D>> ifRight) {
    return fold(x -> x, ifRight.andThen(x -> x.right));
  }
}
