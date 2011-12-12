package org.jmhsieh.strings.aho;

import java.nio.Buffer;
import java.nio.CharBuffer;
import java.util.Collection;
import java.util.Iterator;

import org.jmhsieh.strings.aho.AhoCorasickIncremental;
import org.jmhsieh.strings.aho.SearchResult;


import junit.framework.TestCase;

/**
 * These tests setup incrementally adding strings to the aho corasick state
 * machines. After adding strings a prepare call is made to make it read to run.
 * If new strings are to be added, an unprepare call must be made, strings
 * added, and then prepare call made again.
 * 
 * @author Administrator
 * 
 */

public class TestAhoIncremental extends TestCase {
  @Override
  public void setUp() {
    System.out.println("===");
  }

  String dump(Collection<Buffer> vs) {
    StringBuffer b = new StringBuffer();
    for (Buffer v : vs) {
      b.append(v + " ");
    }
    return b.toString();
  }

  /*
   * To see what the data structure looks like
   */

  public void testAhoIncremental() {
    AhoCorasickIncremental<Buffer> aho = new AhoCorasickIncremental<Buffer>();

    CharBuffer v1 = CharBuffer.wrap("abcde");
    CharBuffer v2 = CharBuffer.wrap("abxyz");

    aho.add(v1.toString().getBytes(), v1);
    aho.add(v2.toString().getBytes(), v2);

    System.out.println(aho.root.dump());
    aho.prepare();
    System.out.println(aho.root.dump());

    CharBuffer doc = CharBuffer.wrap("abcdexffkwjefsabxyz");
    Iterator<SearchResult<Buffer>> it = aho.search(doc.toString().getBytes());

    SearchResult<Buffer> res = it.next();
    System.out.println("@" + res.getLastIndex() + " " + dump(res.getOutputs()));
    assertEquals(res.getOutputs().size(), 1);
    assertEquals(res.getOutputs().toArray()[0], v1);

    res = it.next();
    System.out.println("@" + res.getLastIndex() + " " + dump(res.getOutputs()));
    assertEquals(res.getOutputs().size(), 1);
    assertEquals(res.getOutputs().toArray()[0], v2);
  }

  /**
   * To test unprepare and the adding more entries
   */
  public void testAhoIncremental2() {
    AhoCorasickIncremental<Buffer> aho = new AhoCorasickIncremental<Buffer>();

    CharBuffer v1 = CharBuffer.wrap("abcde");
    CharBuffer v2 = CharBuffer.wrap("abxyz");

    aho.add(v1.toString().getBytes(), v1);
    aho.add(v2.toString().getBytes(), v2);

    aho.prepare();

    CharBuffer doc = CharBuffer.wrap("abcdexffkwjefsabxyz");
    Iterator<SearchResult<Buffer>> it = aho.search(doc.toString().getBytes());
    while (it.hasNext()) {
      SearchResult<Buffer> res = it.next();
      System.out.println("@" + res.getLastIndex() + " "
          + dump(res.getOutputs()));
    }

    System.out.println(aho.root.dump());

    aho.unprepare();
    System.out.println(aho.root.dump());

    CharBuffer v3 = CharBuffer.wrap("dexff");
    aho.add(v3.toString().getBytes(), v3);
    System.out.println(aho.root.dump());
    aho.prepare();

    System.out.println(aho.root.dump());

    Iterator<SearchResult<Buffer>> it2 = aho.search(doc.toString().getBytes());

    SearchResult<Buffer> res = it2.next();
    System.out.println("@" + res.getLastIndex() + " " + dump(res.getOutputs()));
    assertEquals(res.getOutputs().size(), 1);
    assertEquals(res.getOutputs().toArray()[0], v1);

    res = it2.next();
    System.out.println("@" + res.getLastIndex() + " " + dump(res.getOutputs()));
    assertEquals(res.getOutputs().size(), 1);
    assertEquals(res.getOutputs().toArray()[0], v3);

    res = it2.next();
    System.out.println("@" + res.getLastIndex() + " " + dump(res.getOutputs()));
    assertEquals(res.getOutputs().size(), 1);
    assertEquals(res.getOutputs().toArray()[0], v2);

  }

  /**
   * Something with trickier backtracking / error state recovery
   */
  public void testAhoIncremental3() {
    AhoCorasickIncremental<Buffer> aho = new AhoCorasickIncremental<Buffer>();

    CharBuffer v1 = CharBuffer.wrap("abcde");
    CharBuffer v2 = CharBuffer.wrap("xyabc");

    aho.add(v1.toString().getBytes(), v1);
    aho.add(v2.toString().getBytes(), v2);

    aho.prepare();

    System.out.println("first try");
    CharBuffer doc = CharBuffer.wrap("dffxyabcdexf");
    Iterator<SearchResult<Buffer>> it = aho.search(doc.toString().getBytes());
    while (it.hasNext()) {
      SearchResult<Buffer> res = it.next();
      System.out.println("@" + res.getLastIndex() + " "
          + dump(res.getOutputs()));
    }

    aho.unprepare();

    CharBuffer v3 = CharBuffer.wrap("dffxy");
    aho.add(v3.toString().getBytes(), v3);
    aho.prepare();

    System.out.println("second try");
    Iterator<SearchResult<Buffer>> it2 = aho.search(doc.toString().getBytes());

    SearchResult<Buffer> res = it2.next();
    System.out.println("@" + res.getLastIndex() + " " + dump(res.getOutputs()));
    assertEquals(res.getOutputs().size(), 1);
    assertEquals(res.getOutputs().toArray()[0], v3);

    res = it2.next();
    System.out.println("@" + res.getLastIndex() + " " + dump(res.getOutputs()));
    assertEquals(res.getOutputs().size(), 1);
    assertEquals(res.getOutputs().toArray()[0], v2);

    res = it2.next();
    System.out.println("@" + res.getLastIndex() + " " + dump(res.getOutputs()));
    assertEquals(res.getOutputs().size(), 1);
    assertEquals(res.getOutputs().toArray()[0], v1);

  }

  /**
   * Trying to get multiple outputs, returing to the same spot
   */
  public void testAhoIncremental4() {
    AhoCorasickIncremental<Buffer> aho = new AhoCorasickIncremental<Buffer>();

    CharBuffer v1 = CharBuffer.wrap("abcde");
    CharBuffer v2 = CharBuffer.wrap("xyabc");

    aho.add(v1.toString().getBytes(), v1);
    aho.add(v2.toString().getBytes(), v2);

    aho.prepare();

    System.out.println("first try");
    CharBuffer doc = CharBuffer.wrap("dffxyabcdexf");
    Iterator<SearchResult<Buffer>> it = aho.search(doc.toString().getBytes());
    while (it.hasNext()) {
      SearchResult<Buffer> res = it.next();
      System.out.println("@" + res.getLastIndex() + " "
          + dump(res.getOutputs()));
    }

    // System.out.println(aho.root.dump());

    aho.unprepare();
    // System.out.println(aho.root.dump());

    CharBuffer v3 = CharBuffer.wrap("dffxy");
    CharBuffer v4 = CharBuffer.wrap("fxyabcde");
    aho.add(v3.toString().getBytes(), v3);
    aho.add(v4.toString().getBytes(), v4);
    // System.out.println(aho.root.dump());
    aho.prepare();

    // System.out.println(aho.root.dump());
    System.out.println("second try");
    Iterator<SearchResult<Buffer>> it2 = aho.search(doc.toString().getBytes());
    // while (it2.hasNext()) {
    // SearchResult<Buffer> res = it2.next();
    // System.out.println("@" + res.getLastIndex() + " "
    // + dump(res.getOutputs()));
    // }

    SearchResult<Buffer> res = it2.next();
    System.out.println("@" + res.getLastIndex() + " " + dump(res.getOutputs()));
    assertEquals(res.getOutputs().size(), 1);
    assertEquals(res.getOutputs().toArray()[0], v3);

    res = it2.next();
    System.out.println("@" + res.getLastIndex() + " " + dump(res.getOutputs()));
    assertEquals(res.getOutputs().size(), 1);
    assertEquals(res.getOutputs().toArray()[0], v2);

    res = it2.next();
    System.out.println("@" + res.getLastIndex() + " " + dump(res.getOutputs()));
    assertEquals(res.getOutputs().size(), 2);
    assertEquals(res.getOutputs().toArray()[0], v4);
    assertEquals(res.getOutputs().toArray()[1], v1);

  }

}
