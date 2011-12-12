package org.jmhsieh.structs;

import org.jmhsieh.structs.Histogram;

import junit.framework.TestCase;

public class TestHistogram extends TestCase {
  public void testHistogram() {
    Histogram<String> h = new Histogram<String>();

    h.increment("A");
    h.increment("A");

    h.increment("B");
    h.increment("B");
    h.increment("B");

    h.increment("C");
    h.increment("D");
    h.increment("E");

    assertEquals(2, h.get("A"));
    assertEquals(3, h.get("B"));
    assertEquals(1, h.get("C"));
    assertEquals(1, h.get("D"));
    assertEquals(1, h.get("E"));
  }

  public void testHistoMerge() {
    Histogram<String> h1 = new Histogram<String>();

    h1.increment("A");
    h1.increment("B");
    h1.increment("C");
    h1.increment("D");

    Histogram<String> h2 = new Histogram<String>();
    h2.increment("A");
    h2.increment("B");
    h2.increment("B");
    h2.increment("E");

    h1.merge(h2);

    assertEquals(2, h1.get("A"));
    assertEquals(3, h1.get("B"));
    assertEquals(1, h1.get("C"));
    assertEquals(1, h1.get("D"));
    assertEquals(1, h1.get("E"));

  }
}
