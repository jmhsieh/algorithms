package org.jmhsieh.strings.aho;

/**
 * Represents an EdgeList by using a single array. Very fast lookup (just an
 * array access), but expensive in terms of memory.
 */

class DenseEdgeList<T> implements EdgeList<T> {
  private State<T>[] array;

  @SuppressWarnings("unchecked")
  public DenseEdgeList() {

    // this has no type parameter because java arrays have a array/generics
    // covariance problem.
    this.array = new State[256];

    for (int i = 0; i < array.length; i++)
      this.array[i] = null;
  }

  /**
   * Helps in converting to dense representation.
   */
  public static <T> DenseEdgeList<T> fromSparse(SparseEdgeList<T> list) {
    byte[] keys = list.keys();
    DenseEdgeList<T> newInstance = new DenseEdgeList<T>();
    for (int i = 0; i < keys.length; i++) {
      newInstance.put(keys[i], list.get(keys[i]));
    }
    return newInstance;
  }

  public State<T> get(byte b) {
    return this.array[(int) b & 0xFF];
  }

  public void put(byte b, State<T> s) {
    this.array[(int) b & 0xFF] = s;
  }

  public byte[] keys() {
    int length = 0;
    for (int i = 0; i < array.length; i++) {
      if (array[i] != null)
        length++;
    }
    byte[] result = new byte[length];
    int j = 0;
    for (int i = 0; i < array.length; i++) {
      if (array[i] != null) {
        result[j] = (byte) i;
        j++;
      }
    }
    return result;
  }

}
