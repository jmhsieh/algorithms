package org.jmhsieh.sets.unionfind;

import java.util.HashMap;

public class UnionFind<T> {

  HashMap<T, UF<T>> clusters = new HashMap<T, UF<T>>();

  public static class UF<T> {
    T value;
    UF<T> parent;
    int rank;

    public UF(T v) {
      this.value = v;
      this.rank = 0;
    }
  }

  public void addValue(T v) {
    if (clusters.get(v) != null) {
      return; // only allow a single insert.
    }
    clusters.put(v, new UF<T>(v));
  }

  public T find(T v) {
    UF<T> s = clusters.get(v);
    UF<T> r = _find(s);
    if (r == null)
      return null;
    return r.value;
  }

  public T union(T x, T y) {
    UF<T> u = _union(clusters.get(x), clusters.get(y));
    if (u == null)
      return null;
    return u.value;
  }

  private static <T> UF<T> _union(UF<T> x, UF<T> y) {
    UF<T> xroot = _find(x);
    UF<T> yroot = _find(y);

    if (xroot == yroot)
      return xroot;

    if (xroot.rank > yroot.rank) {
      yroot.parent = xroot;
      return xroot;
    }

    if (xroot.rank < yroot.rank) {
      xroot.parent = yroot;
      return yroot;
    }

    // (xroot != yroot)
    yroot.parent = xroot;
    xroot.rank = xroot.rank + 1;
    return xroot;
  }

  private static <T> UF<T> _find(UF<T> x) {
    if (x.parent == x)
      return x;
    else
      return _find(x.parent);
  }

}
