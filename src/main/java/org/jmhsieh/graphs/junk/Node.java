package org.jmhsieh.graphs.junk;

import java.util.Collection;

public interface Node<T, U> {

  // number of child nodes
  public int size();

  public Node<T, U> get(U b);

  public void put(U b, Node<T, U> s);

  public U[] keys();

  public void addOutput(T o);

  public Collection<T> getOutputs();
}
