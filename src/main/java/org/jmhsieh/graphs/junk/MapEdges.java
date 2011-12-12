package org.jmhsieh.graphs.junk;

import java.util.Collection;
import java.util.HashMap;

/**
 * Represents an Edges using a HashMap
 */

class MapEdges<T, U> implements Edges<T, U> {
  private HashMap<U, Node<T, U>> map = new HashMap<U, Node<T, U>>();

  public MapEdges() {
  }

  /**
   * Helps in converting to dense representation.
   */
  public static <T, U> MapEdges<T, U> convert(Edges<T, U> list) {
    MapEdges<T, U> newInstance = new MapEdges<T, U>();
    for (U k : list.keys()) {
      newInstance.put(k, list.get(k));
    }
    return newInstance;
  }

  public Node<T, U> get(U b) {
    return map.get(b);
  }

  public void put(U b, Node<T, U> s) {
    map.put(b, s);
  }

  public Collection<U> keys() {
    return map.keySet();
  }

  public Collection<Node<T, U>> children() {
    return map.values();
  }

}
