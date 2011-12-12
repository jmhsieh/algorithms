package org.jmhsieh.graphs.junk;

import java.util.Collection;

abstract public class NodeImpl<T, U> {

  Edges<T, U> edgeList;
  Collection<T> values;

  // number of child nodes
  abstract public int size();

  public Node<T, U> get(U b) {
    return this.edgeList.get(b);
  }

  public void put(U b, Node<T, U> s) {
    this.edgeList.put(b, s);
  }

  public Collection<U> keys() {
    return this.edgeList.keys();
  }

  public void addOutput(T o) {
    this.values.add(o);
  }

  // public Set<T> getOutputs() {
  public Collection<T> getOutputs() {
    return this.values;
  }
}
