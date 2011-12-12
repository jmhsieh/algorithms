package org.jmhsieh.strings.aho;

/**
 * Linked list implementation of the EdgeList should be less memory-intensive.
 */

class SparseEdgeList<T> implements EdgeList<T> {
  private Cons<T> head;

  public SparseEdgeList() {
    head = null;
  }

  public State<T> get(byte b) {
    Cons<T> c = head;
    while (c != null) {
      if (c.b == b)
        return c.s;
      c = c.next;
    }
    return null;
  }

  public void put(byte b, State<T> s) {
    this.head = new Cons<T>(b, s, head);
  }

  public byte[] keys() {
    int length = 0;
    Cons<T> c = head;
    while (c != null) {
      length++;
      c = c.next;
    }
    byte[] result = new byte[length];
    c = head;
    int j = 0;
    while (c != null) {
      result[j] = c.b;
      j++;
      c = c.next;
    }
    return result;
  }

  static private class Cons<T> {
    byte b;
    State<T> s;
    Cons<T> next;

    public Cons(byte b, State<T> s, Cons<T> next) {
      this.b = b;
      this.s = s;
      this.next = next;
    }
  }

}
