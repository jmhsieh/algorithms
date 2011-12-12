package org.jmhsieh.graphs;

import java.util.Collection;
import java.util.Set;

import org.jmhsieh.util.F;
import org.jmhsieh.util.F.Fun;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

/**
 * This is my generalized graph representation.
 * 
 * @param <T>
 *          type of value on Node
 * @param <S>
 *          type of value on Edge
 */
public class Graph<T, S> {

  final Multimap<V<T>, E<T, S>> node2edges = HashMultimap.create();

  public static class E<T, S> {
    S value;
    V<T> src;
    V<T> dst;

    // An alternate way to represent this is to have a adjacency list but have U
    // be a pair -- an edge and a value
    E(S value, V<T> src, V<T> dst) {
      this.value = value;
      this.src = src;
      this.dst = dst;
    }

    public S value() {
      return value;
    }

    public void setValue(S v2) {
      this.value = v2;
    }

    public V<T> getSrc() {
      return src;
    }

    public V<T> getDest() {
      return dst;
    }

    @Override
    public String toString() {
      return src + "-" + value.toString() + "->" + dst;
    }
  }

  public static class V<T> {
    T value;

    V(T value) {
      this.value = value;
    }

    public T value() {
      return value;
    }

    @Override
    public String toString() {
      return value.toString();
    }

  }

  public V<T> newNode(T v) {
    return new V<T>(v);
  }

  public E<T, S> newEdge(S v, V<T> src, V<T> dst) {
    E<T, S> e = new E<T, S>(v, src, dst);
    node2edges.put(src, e);
    return e;
  }

  public void removeEdge(E<T, S> e) {
    node2edges.remove(e.getSrc(), e);
  }

  public Collection<E<T, S>> getEdges(V<T> n) {
    return node2edges.get(n);
  }

  Fun<E<T, S>, V<T>> getDst = new Fun<E<T, S>, V<T>>() {

    @Override
    public V<T> call(E<T, S> t) {
      return t.getDest();
    }
  };

  public Set<V<T>> getVerticies() {
    return node2edges.keySet();
  }

  public Collection<E<T,S>> getEdges() {
    return node2edges.values();
  }
  
  public Collection<V<T>> getChildren(V<T> n) {
    return F.map(getDst, getEdges(n));
  }

  public E<T, S> getEdge(V<T> src, V<T> dst) {
    for (E<T, S> e : node2edges.get(src)) {
      if (dst == e.getDest()) {
        return e;
      }
    }
    return null;
  }

  public boolean forallEdges(final Fun<S, Boolean> f) {
    Fun<E<T, S>, Boolean> f2 = new Fun<E<T, S>, Boolean>() {
      @Override
      public Boolean call(E<T, S> t) {
        return f.call(t.value());
      }

    };
    return F.forall(f2, node2edges.values());
  }

}
