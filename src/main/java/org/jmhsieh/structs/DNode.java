package org.jmhsieh.structs;

public class DNode<U> {
  DNode<U> next, prev;
  U elem;

  DNode(U value, DNode<U> next, DNode<U> prev) {
    this.elem = value;
    this.next = next;
    this.prev = prev;
  }

  DNode(U value) {
    this.elem = value;
    this.next = this.prev = this;
  }

  static <U> DNode<U> createCircular(U value) {
    return new DNode<U>(value);
  }

  static <U> DNode<U> create(U value) {
    return new DNode<U>(value, null, null);
  }

  public DNode<U> append(U v) {
    return appendNode(create(v));
  }

  public DNode<U> appendNode(DNode<U> n) {
    n.prev = this;
    n.next = this.next;
    this.next.prev = n;
    this.next = n;

    return n;
  }

  public U remove() {
    if (this == next && this == prev) {
      // make any other references an node that points nowhere.
      next = null;
      prev = null;
      return elem;
    }

    next.prev = prev;
    prev.next = next;
    next = null;
    prev = null;
    return elem;
  }

  @Override
  public String toString() {
    String s = "[ node " + elem.toString() + " || ";

    if (this.next != null) {

      for (DNode<U> n = this.next; n != this; n = n.next) {
        s += n.elem.toString() + " ";
      }
    }
    return s + "]";
  }
}
