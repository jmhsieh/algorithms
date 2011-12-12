package org.jmhsieh.util;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Simple class to consolidate benchmarking and timing code. This will help me
 * find the bottlenecks.
 * 
 * Usage: create new benchmark. call mark with comments whenever wanted. call
 * done to flush print writer.
 * 
 * I'll make this smarter later.
 * 
 * @author jmhsieh
 */
public class Benchmark {
  long start;
  long last;
  PrintWriter out;
  PrintWriter log;
  List<String> values = new ArrayList<String>();
  String name;

  public Benchmark(String name, PrintWriter o, PrintWriter l) {
    this.name = name;
    start = System.nanoTime();
    last = start;
    out = o;
    log = l;
    try {
      String host = InetAddress.getLocalHost().getHostName();
      values.add(host);
    } catch (UnknownHostException e) {
      values.add("localhost");
      e.printStackTrace();
    }
    // do a GC cleanup before each benchmark test.
    // Runtime rt = Runtime.getRuntime();
    // rt.gc();
    flushMemory();

    Runtime r = Runtime.getRuntime();
    long fmem = r.freeMemory();
    long tmem = r.totalMemory();
    long umem = tmem - fmem; // memory used
    out.println("[ 0us, " + (umem / 1024) + "k mem] ] \tStarting (after gc) ");
  }

  // default to standard out.
  public Benchmark() {
    this(null, new PrintWriter(new OutputStreamWriter(System.out)),
        new PrintWriter(new OutputStreamWriter(System.out)));
  }

  // this was named, make the values go to std err.
  public Benchmark(String name) {
    this(name, new PrintWriter(new OutputStreamWriter(System.out)),
        new PrintWriter(new OutputStreamWriter(System.err)));
  }

  public void mark(String s, Object... logs) {
    StringBuffer b = new StringBuffer();
    boolean first = true;
    for (Object o : logs) {
      if (!first)
        b.append(",");
      b.append(o.toString());
      first = false;
      values.add(o.toString());
    }
    mark(s + " : " + b);
  }

  public void mark(String s) {
    long now = System.nanoTime();
    long delta = (now - last) / 1000; // us
    long cumulative = (now - start) / 1000; // us

    flushMemory();

    Runtime r = Runtime.getRuntime();

    long fmem = r.freeMemory();
    long tmem = r.totalMemory();
    long umem = tmem - fmem; // memory used
    out.println("[" + cumulative + "us d " + delta + "us " + (umem / 1024)
        + "k mem]\t" + s);
    values.add("" + delta);
    values.add("" + umem / 1024);

    // last = now;
    last = System.nanoTime(); // don't count gc time.
  }

  public void done() {
    out.flush();
    boolean first = true;
    if (name != null) {
      log.print(name);
      first = false;
    }

    for (String s : values) {
      if (!first)
        log.print(",");
      log.print(s);
      first = false;
    }
    log.println();
    log.flush();
  }

  // Runtime.gc is just a hint! This forces the issue!
  public static void flushMemory() {
    System.gc();
    System.gc();
    System.gc();
  }

  // alternate method to force GC.
  public static void flushMemoryExhaust() {

    // Use a vector to hold the memory.
    Vector<byte[]> v = new Vector<byte[]>();
    int count = 0;
    // increment in megabyte chunks initially
    int size = 1048576;
    // Keep going until we would be requesting
    // chunks of 1 byte
    while (size > 1) {
      try {
        for (; true; count++) {
          // request and hold onto more memory
          v.addElement(new byte[size]);
        }
      }
      // If we encounter an OutOfMemoryError, keep
      // trying to get more memory, but asking for
      // chunks half as big.
      catch (OutOfMemoryError bounded) {
        size = size / 2;
      }
    }
    // Now release everything for GC
    v = null;
    // and ask for a new Vector as a new small object
    // to make sure garbage collection kicks in before
    // we exit the method.
    v = new Vector<byte[]>();
  }

}
