package org.jmhsieh.numbers;

/**
 * A implementation of the euclidean algorithm.
 * 
 */
public class GCD {

  /**
   * 
   * @param a
   *          assumed positive
   * @param b
   *          assumed positive
   * @return
   */
  public static long gcd(long a, long b) {
    while (b != 0) {
      long t = b;
      b = a % b;
      a = t;
    }
    return a;
  }

  public static boolean isRelPrime(long a, long b) {
    return gcd(a, b) == 1;
  }

  /**
   * Get me the largest number close to a that is relatively prime to b This
   * should be useful for finding relative primes in hashtables.
   * 
   * @param a
   * @param b
   * @return
   */
  public static long getRelPrime(long a, long b) {
    if (b > a)
      return getRelPrime(b, a);

    while (!isRelPrime(a, b)) {
      a--;
    }
    return a;
  }
}
