package org.jmhsieh.strings.rabinkarp;

import java.util.AbstractSet;
import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class RKHashset extends AbstractSet<byte[]> {
  PolynomialRollingHash rh;
  int n;

  transient Entry[] table;
  int size = 0;
  private int maxdepth = 0;
  int dupes = 0, miss = 0, hits = 0;
  int tableMiss = 0, chainMiss = 0, conflictMiss = 0, lookups = 0;

  // single entry in hash table (has link to next in case of hash collision)
  class Entry {
    Entry next;
    byte[] val;

    Entry(byte[] val, Entry next) {
      this.val = val.clone();
      this.next = next;
    }
  };

  public RKHashset(int n, int cap, int factor) {
    this.n = n;
    table = new Entry[cap];
    rh = new PolynomialRollingHash(n, cap, factor);
  }

  @Override
  public boolean add(byte[] b) {
    if (b.length < n) {
      return false;
    }
    byte[] bs = Arrays.copyOf(b, n);
    PolynomialRollingHash rh = new PolynomialRollingHash(this.rh.n,
        this.rh.cap, this.rh.FACTOR);
    int idx = rh.hashN(bs, n);
    Entry cur = table[idx];
    int depth = 0;
    while (cur != null) {
      // check if element already present, bs.equals does not work
      if (equals(cur.val, bs)) {
        dupes++;
        return false;
      }
      // if not try next
      cur = cur.next;
      depth++;
    }

    maxdepth = ((maxdepth < depth) ? depth : maxdepth);
    table[idx] = new Entry(bs, table[idx]);
    size++;
    return true;
  }

  // @Override
  // public boolean add(String e) {
  // if (e.length() < n) {
  // // System.out.println(" Skipping "+ e);
  // return false;
  // }
  // byte[] bs = e.substring(0, n).getBytes();
  // PolynomialRollingHash rh = new PolynomialRollingHash(this.rh.n,
  // this.rh.cap, this.rh.FACTOR);
  // int idx = rh.hashN(bs, n);
  // Entry cur = table[idx];
  // int depth = 0;
  // while (cur != null) {
  // // check if element already present, bs.equals does not work
  // if (equals(cur.val, bs)) {
  // dupes++;
  // return false;
  // }
  // // if not try next
  // cur = cur.next;
  // depth++;
  // }
  //
  // maxdepth = ((maxdepth < depth) ? depth : maxdepth);
  // table[idx] = new Entry(bs, table[idx]);
  // size++;
  // return true;
  // }

  public boolean contains(byte[] bs) {
    if (bs.length < n) {
      // byte array too short for proper n gram match!
      return false;
    }

    PolynomialRollingHash rh = new PolynomialRollingHash(this.n, this.rh.cap,
        this.rh.FACTOR);
    int h = rh.hashN(bs, n);
    Entry e = table[h];
    while (e != null) {
      if (equals(bs, e.val)) {
        hits++;
        return true;
      }
      e = e.next;
    }
    miss++;
    return false;
  }

  public boolean contains(String s) {
    // TODO this may break the contract
    if (s == null)
      throw new RuntimeException("Null passed as argument to contains.");

    byte[] bs = s.substring(0, n).getBytes();
    return contains(bs);
  }

  public void addPartitions(byte[] buf) {
    int len = buf.length;
    if (len < n)
      throw new RuntimeException("string not long enough");

    while (len >= 2 * n) {
      add(buf);
      buf = Arrays.copyOfRange(buf, n, buf.length);
      // s = s.substring(n);
      // len = s.length();
      len = buf.length;
    }

    // add(s);
    add(buf);

    // / aaabc chops into 3s to yield aaa abc with 1 overlap
    if (len != n) {
      byte[] b2 = Arrays.copyOfRange(buf, buf.length - n, buf.length);
      add(b2);
      // String s1 = s.substring(s.length() - n);
      // add(s1); // overlaps by s.length() - n bytes)
    }
  }

  // public void addPartitions(String s) {
  // int len = s.length();
  // if (len < n)
  // throw new RuntimeException("string not long enough");
  //
  // while (len >= 2 * n) {
  // add(s);
  // s = s.substring(n);
  // len = s.length();
  // }
  //
  // add(s);
  //
  // // / aaabc chops into 3s to yield aaa abc with 1 overlap
  // if (len != n) {
  // String s1 = s.substring(s.length() - n);
  // add(s1); // overlaps by s.length() - n bytes)
  // }
  // }

  boolean equals(byte[] b1, byte[] b2) {
    for (int i = 0; i < b1.length; i++) {
      if (b1[i] != b2[i]) {
        return false;
      }
    }
    return true;
  }

  // null if no match, else bytes if is match.
  public byte[] matched(byte b) {
    lookups++;
    int h = rh.hash(b);
    Entry e = table[h];
    if (e == null) {
      tableMiss++;
      miss++;
      return null;
    }

    while (e != null) {
      byte[] ng = rh.getNGram();

      if (equals(ng, e.val)) {
        hits++;
        return ng;
      }
      chainMiss++;
      e = e.next;
    }
    conflictMiss++;
    miss++;
    return null;
  }

  class RKIter implements Iterator<byte[]> {
    int idx = 0;
    Entry next = null;
    Entry current = null;

    RKIter() {
      while (table[idx] == null && idx < table.length) {
        if (idx > table.length)
          return;
        idx++;
      }
      next = table[idx];
      current = table[idx];
    }

    public boolean hasNext() {
      return (next != null);
    }

    public byte[] next() {
      if (next == null)
        throw new NoSuchElementException();

      // String ret = new String(next.val);
      byte[] ret = next.val;

      // setup for next.
      if (next.next == null) {
        idx++;
        while (table[idx] == null) {
          if (idx > table.length)
            throw new NoSuchElementException();
          idx++;
        }
        current = table[idx];
        next = table[idx];
      }

      return ret;
    }

    public void remove() {
      throw new RuntimeException("RKHashset iterator: Remove not supported");
    }
  };

  @Override
  public Iterator<byte[]> iterator() {
    return new RKIter();
  }

  @Override
  public int size() {
    return size;
  }

  public void dump() {
    System.out.println("inserted " + size() + " filters.");
    System.out.println("# dupe filters  " + dupes);
    System.out.println("max depth " + getMaxdepth());
    System.out.println("lookups: " + lookups);
    System.out.println("hits: " + hits);
    System.out.println("misses: " + miss);
    System.out.println(" table miss: " + tableMiss);
    System.out.println(" chain miss: " + chainMiss);
    System.out.println(" conflict miss: " + conflictMiss);
  }

  public int getMaxdepth() {
    return maxdepth;
  }
}
