package org.jmhsieh.graphs.junk;

import java.util.Arrays;
import java.util.Collection;

/**
 * Represents an EdgeList by using a single array. Very fast lookup (just an
 * array access), but expensive in terms of memory.
 * 
 * (There is a penalty for boxing/unboxing that happens here!)
 */

class DenseEdgeList<T> implements Edges<T, Byte> {
  private Node<T, Byte>[] array;

  @SuppressWarnings("unchecked")
  public DenseEdgeList() {

    // this has no type parameter because java arrays have a array/generics
    // covariance problem.
    this.array = new Node[256];

    for (int i = 0; i < array.length; i++)
      this.array[i] = null;
  }

  /**
   * Helps in converting to dense representation.
   */
  public static <T> DenseEdgeList<T> fromSparse(Edges<T, Byte> list) {
    Byte[] keys = list.keys().toArray(new Byte[0]);
    DenseEdgeList<T> newInstance = new DenseEdgeList<T>();
    for (int i = 0; i < keys.length; i++) {
      newInstance.put(keys[i], list.get(keys[i]));
    }
    return newInstance;
  }

  public Node<T, Byte> get(Byte b) {
    return this.array[(int) b & 0xFF];
  }

  public void put(Byte b, Node<T, Byte> s) {
    this.array[(int) b & 0xFF] = s;
  }

  public Collection<Byte> keys() {
    int length = 0;
    for (int i = 0; i < array.length; i++) {
      if (array[i] != null)
        length++;
    }
    Byte[] result = new Byte[length];
    int j = 0;
    for (int i = 0; i < array.length; i++) {
      if (array[i] != null) {
        result[j] = (byte) i;
        j++;
      }
    }
    return Arrays.asList(result);
  }

  @Override
  public Collection<Node<T, Byte>> children() {
    return null;
  }
}
