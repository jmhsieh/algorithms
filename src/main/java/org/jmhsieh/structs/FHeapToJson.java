package org.jmhsieh.structs;

public class FHeapToJson<T> {

  public static <T> String toJson(FibonacciHeap<T> f) {
    if (f == null || f.roots == null) {
      return "{ }";
    }

    String s = " { ";

    s += "min :  " + f.min.elem.v.toString() + ", ";
    s += "roots: " + toJsonValue(f.roots);

    return s + " }";
  }

  public static <T> String toJsonValue(FibonacciHeap<T>.Heap h) {
    String s = "[" + h.v.toString();

    if (h.children != null) {
      if (h.children.next == h.children) {
        s += ",[" + h.children.elem.v.toString() + "]";
      } else {
        s += ",[" + h.children.elem.v.toString();
        DNode<FibonacciHeap<T>.Heap> n;
        for (n = h.children.next; n != h.children; n = n.next) {
          s += ", " + n.elem.v.toString();
        }
        s += "]";
      }
    }
    return s + "]";
  }

  public static <T> String toJsonValue(DNode<FibonacciHeap<T>.Heap> dl) {
    if (dl == null) {
      return "[ ]";
    }

    String s = "[ " + toJsonValue(dl.elem);
    if (dl.next != null) {
      DNode<FibonacciHeap<T>.Heap> n;
      for (n = dl.next; n != dl; n = n.next) {
        s += ", " + toJsonValue(n.elem);
      }
    }
    return s + " ]";
  }
}
