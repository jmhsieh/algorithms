package org.jmhsieh.strings.rabinkarp;

import org.jmhsieh.strings.rabinkarp.RKHashset;

import junit.framework.TestCase;

public class TestRKHashset extends TestCase {
  public void testHash3() {
    // tests
    // RKHashset rk = new RKHashset(3, 201, 37); // 3-grams, 201 hash cap, 37 as
    // factor
    // somehow these miraculously collide :( !
    RKHashset rk = new RKHashset(3, 201, 31); // 3-grams, 201 hash cap, 31 as
    // factor

    rk.add("abc".getBytes());
    rk.add("def".getBytes());
    rk.add("cde".getBytes());
    int matches = 0;
    byte[] bs = "aaaabcdabdefcdacde".getBytes();
    // from index 0 to length - ngram size
    for (int i = 0; i < bs.length; i++) {
      byte[] m = rk.matched(bs[i]);
      if (m != null) {
        System.out
            .println("matched @ " + (i - (3 - 1)) + " : " + new String(m));
        matches++;
      }
    }
    assertEquals(matches, 3);
  }

  public void testHash6() {
    // tests
    RKHashset rk = new RKHashset(6, 201, 37); // 6-grams, 201 hash cap

    rk.add("aaaabc".getBytes());
    rk.add("defcda".getBytes());
    rk.add("bcdabd".getBytes());
    int matches = 0;
    byte[] bs = "aaaabcdabdefcdacde".getBytes();
    // from index 0 to length - ngram size
    for (int i = 0; i < bs.length; i++) {
      byte[] m = rk.matched(bs[i]);
      if (m != null) {
        System.out
            .println("matched @ " + (i - (6 - 1)) + " : " + new String(m));
        matches++;
      }
    }
    assertEquals(matches, 3);
  }

  public void testHashA7() {
    // tests
    RKHashset rk = new RKHashset(6, 15437, 7); // 6-grams, cap, factor

    rk.add("aaaabc".getBytes());
    rk.add("defcda".getBytes());
    rk.add("bcdabd".getBytes());
    int matches = 0;
    byte[] bs = "aaaabcdabdefcdacde".getBytes();
    // from index 0 to length - ngram size
    for (int i = 0; i < bs.length; i++) {
      byte[] m = rk.matched(bs[i]);
      if (m != null) {
        System.out
            .println("matched @ " + (i - (6 - 1)) + " : " + new String(m));
        matches++;
      }
    }
    assertEquals(matches, 3);
  }
}
