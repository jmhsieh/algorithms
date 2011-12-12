package org.jmhsieh.graphs.junk;

import java.util.Collection;

public interface Edges<T, U> {
  Node<T, U> get(U ch);

  void put(U ch, Node<T, U> state);

  Collection<U> keys();
  
  Collection<Node<T,U>> children();
}
