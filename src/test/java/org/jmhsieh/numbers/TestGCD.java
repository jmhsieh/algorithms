package org.jmhsieh.numbers;

import org.jmhsieh.numbers.GCD;

import junit.framework.TestCase;

public class TestGCD extends TestCase {
  public void testGcd() {
    assertEquals(3, GCD.gcd(24, 15));
    assertEquals(25, GCD.gcd(300, 25));
    assertEquals(7, GCD.gcd(49, 91));

  }
}
