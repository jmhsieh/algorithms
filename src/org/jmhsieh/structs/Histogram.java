package org.jmhsieh.structs;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;


public class Histogram<T> {
  Map<T, Integer> hist = new HashMap<T, Integer>();

  public void increment(T val) {
    if (hist.containsKey(val)) {
      int i = hist.get(val);
      i++;
      hist.put(val, i);
    } else {
      hist.put(val, 1);
    }
  }

  public void incrementN(T val, int j) {
    assert (j > 0);
    if (hist.containsKey(val)) {
      int i = hist.get(val);
      i += j;
      hist.put(val, i);
    } else {
      hist.put(val, j);
    }
  }
  
  public void merge(Histogram<T> h2) {
    if (h2 == null) return ;
    for (T k : h2.keys()) {      
      incrementN(k, h2.get(k));
    }
  }

  public int get(T val) {
    assert(val != null);
    if (hist.containsKey(val)) {
      return hist.get(val);
    }
    return 0;
  }

  public Set<T> keys() {
    return hist.keySet();
  }

  public SortedSet<Pair<T, Integer>> sorted() {

    SortedSet<Pair<T, Integer>> sort = new TreeSet<Pair<T, Integer>>(
        new Comparator<Pair<T, Integer>>() {
          @SuppressWarnings("unchecked")
          @Override
          public int compare(Pair<T, Integer> o1, Pair<T, Integer> o2) {
            int delta = o2.getRight() - o1.getRight();
            if (delta != 0)
              return delta;

            // TODO this is gross
            if (o1 instanceof Comparable && o2 instanceof Comparable) {
              Comparable c1 = ((Comparable) o1.getLeft());
              Comparable c2 = ((Comparable) o2.getLeft());
              return c1.compareTo(c2);
            } else {
              // this is a performance killer for VBytes
              return o1.getLeft().toString().compareTo(o2.getLeft().toString());
            }
          }
        });

    for (T k : hist.keySet()) {
      sort.add(new Pair<T, Integer>(k, hist.get(k)));
    }

    return sort;
  }

  /**
   * Return the number of keys present in the histogram
   * 
   * @return
   */
  public int size() {
    return hist.size();
  }

  public int total() {
    int hits1 = 0;
    for (T v : hist.keySet()) {
      hits1 += hist.get(v);
    }
    return hits1;
  }

  @Override
  public String toString() {
    StringBuffer b = new StringBuffer();
    for (T key : hist.keySet()) {
      b.append(String.format("%6d :: %s,\n", hist.get(key), key));
    }
    return b.toString();
  }

}
