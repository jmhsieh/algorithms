package org.jmhsieh.maps;

import org.jmhsieh.sets.consistant.HashFunction;
import org.jmhsieh.structs.Pair;

/**
 * First implementation of a cuckoo hash map. This is just a proof of concept
 * and not a robust implementation.
 * 
 * http://en.wikipedia.org/wiki/Cuckoo_hashing
 * 
 * http://citeseerx.ist.psu.edu/viewdoc/summary?doi=10.1.1.25.4189
 * 
 * TODO convert into standard map interface
 * 
 * TODO make the rehash function smarter about how it grows, or make an option
 * so that it does not grow.
 * 
 * @param <K>
 *          Key type
 * @param <V>
 *          Value type
 */
public class CuckooHashMap<K, V> {
  Pair<K, V>[] vals1;
  Pair<K, V>[] vals2;
  HashFunction hf1;
  HashFunction hf2;

  @SuppressWarnings("unchecked")
  public CuckooHashMap(int capacity) {
    vals1 = (Pair<K, V>[]) new Pair[capacity];
    vals2 = (Pair<K, V>[]) new Pair[capacity];
    hf1 = new HashFunction() {
      @Override
      public int hash(Object s) {
        return s.hashCode() % vals1.length;
      }
    };

    hf2 = new HashFunction() {
      @Override
      public int hash(Object s) {
        int len = vals1.length;
        return (s.hashCode() / len) % len;
      }
    };
  }

  public void put(K k, V v) {
    Pair<K, V> p = new Pair<K, V>(k, v);
    boolean success = cuckooPut(p);
    if (success) {
      return;
    }
    // rehash
    rehash();

    // insert
    put(k, v);
  }

  @SuppressWarnings("unchecked")
  void rehash() {
    System.out.println("Rehashing");
    Pair<K, V>[] oldVals1 = vals1;
    Pair<K, V>[] oldVals2 = vals2;

    vals1 = (Pair<K, V>[]) new Pair[13];
    vals2 = (Pair<K, V>[]) new Pair[13];
    for (Pair<K, V> p1 : oldVals1) {
      if (p1 == null)
        continue;
      cuckooPut(p1);
    }
    for (Pair<K, V> p2 : oldVals2) {
      if (p2 == null)
        continue;
      cuckooPut(p2);
    }
  }

  boolean cuckooPut(Pair<K, V> p) {
    // actually need to check equality first
    int idx1 = hf1.hash(p.getLeft());
    int idx2 = hf2.hash(p.getLeft());
    Pair<K, V> p1 = vals1[idx1];
    Pair<K, V> p2 = vals2[idx2];

    // overwrite cases
    if (p1 != null && p1.getLeft().equals(p.getLeft())) {
      vals1[idx1] = p;
      return true;
    }
    if (p2 != null && p2.getLeft().equals(p.getLeft())) {
      vals1[idx1] = p;
      return true;
    }

    // insert case
    int maxLoop = vals1.length;
    for (int i = 0; i < maxLoop; i++) {
      // calculate new hashes
      idx1 = hf1.hash(p.getLeft());
      p1 = vals1[idx1];

      // normal case, put item in table 1
      if (p1 == null) {
        vals1[idx1] = p;
        return true;
      }

      // hash 1 was occupied, swap
      Pair<K, V> temp = vals1[idx1];
      vals1[idx1] = p;
      p = temp;

      // calculate new hashes
      idx2 = hf2.hash(p.getLeft());
      p2 = vals2[idx2];

      // normal case 2, put item in to table 2
      if (p2 == null) {
        vals2[idx2] = p;
        return true;
      }

      // hash 2 was occupied also, swap
      temp = vals2[idx2];
      vals2[idx2] = p;
      p = temp;
    }
    return false;

  }

  public V get(K k) {
    int idx1 = hf1.hash(k);
    Pair<K, V> p1 = vals1[idx1];
    if (p1.getLeft().equals(k)) {
      return p1.getRight();
    }

    int idx2 = hf2.hash(k);
    Pair<K, V> p2 = vals2[idx2];
    if (p2.getLeft().equals(k)) {
      return p2.getRight();
    }

    return null;
  }

}
