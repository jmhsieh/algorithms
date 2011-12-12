package org.jmhsieh.structs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * Code originally from slides here:
 * 
 * http://www.cs.princeton.edu/~wayne/teaching/fibonacci-heap.pdf
 * 
 */

public class FibonacciHeap<T> {

  // static class DLNode<U> {
  // U elem;
  // DLNode<U> next;
  // DLNode<U> prev;
  //
  // DLNode(U elem) {
  // this.elem = elem;
  // this.next = this.prev = this;
  // }
  //
  // DLNode<U> append(DLNode<U> n) {
  // n.prev = this;
  // n.next = this.next;
  // this.next.prev = n;
  // this.next = n;
  //
  // return n;
  // }
  //
  // U remove() {
  // if (this == next && this == prev) {
  // // make any other references an node that points nowhere.
  // next = null;
  // prev = null;
  // return elem;
  // }
  //
  // next.prev = prev;
  // prev.next = next;
  // next = null;
  // prev = null;
  // return elem;
  // }
  //
  // @Override
  // public String toString() {
  // String s = "[ node " + elem.toString() + " || ";
  //
  // if (this.next != null) {
  //
  // for (DLNode<U> n = this.next; n != this; n = n.next) {
  // s += n.elem.toString() + " ";
  // }
  // }
  // return s + "]";
  // }
  // }

  class Heap {
    T v;
    DNode<Heap> children;
    int numCh;

    Heap(T v) {
      this.v = v;
    }

    void addChild(Heap v) {
      if (children == null) {
        children = new DNode<Heap>(v);
        numCh++;
        return;
      }

      DNode<Heap> v2 = new DNode<Heap>(v);
      children.appendNode(v2);
      numCh++;
    }

    @Override
    public String toString() {

      String s = "( heap " + v.toString() + " :: ";

      if (this.children != null) {
        if (this.children.next == this.children) {
          s += " => " + this.children.elem.toString();
        } else {
          s += " => " + this.children.elem.toString();
          for (DNode<Heap> n = this.children.next; n != this.children; n = n.next) {
            s += " => " + n.elem.toString();
          }
        }
      }
      return s + ")";
    }
  }

  int sz; // # nodes
  int maxSz;

  DNode<Heap> min;
  DNode<Heap> roots;
  private final Comparator<? super T> comparator;

  public FibonacciHeap(Comparator<? super T> comp) {
    this.comparator = comp;
  }

  public FibonacciHeap() {
    this(null);
  }

  @SuppressWarnings("unchecked")
  int compare(T v1, T v2) {
    if (v1 == v2)
      return 0;
    if (comparator != null) {
      return comparator.compare(v1, v2);
    }

    // TODO (jon) this is a hack, not sure if this can go bad
    Comparable<? super T> key = (Comparable<? super T>) v1;
    return key.compareTo((T) v2);
  }

  int rank(DNode<Heap> x) {
    return x.elem.numCh;
  }

  int rank(Heap h) {
    return h.numCh;
  }

  // int trees(Heap h) {
  // return 0;
  // }

  int marks(Heap h) {
    return 0;
  }

  // int potential(Heap h) {
  // return trees(h) + 2 * marks(h);
  // }

  public void insert(T v) {
    // create new tree, insert into root list
    Heap h = new Heap(v);
    DNode<Heap> n = new DNode<Heap>(h);

    if (roots != null) {
      roots.appendNode(n);
    } else {
      roots = n;
    }

    // update min pointer if necessary.
    if (min == null) {
      min = n;
    } else if (compare(v, min.elem.v) < 0) {
      min = n;
    }

    // update max size
    sz++;
    if (sz > maxSz)
      maxSz = sz;

  }

  public T peekMin() {
    if (min == null)
      return null;
    return min.elem.v;
  }

  public T deleteMin() {
    // delete min from root set.
    if (min == roots) {
      if (roots == roots.next) {
        // only one element?
        roots = null;
      } else {
        roots = roots.next;
      }
    }

    // meld children into root list
    Heap hmin = min.remove();
    ArrayList<DNode<Heap>> hl = new ArrayList<DNode<Heap>>();
    if (hmin.children != null) {
      if (hmin.children.next == hmin.children) {
        hl.add(hmin.children);
      } else {
        hl.add(hmin.children);
        for (DNode<Heap> h = hmin.children.next; h != hmin.children; h = h.next) {
          hl.add(h);
        }
      }

      for (DNode<Heap> h : hl) {
        Heap h1 = h.remove();
        if (roots == null) {
          roots = new DNode<Heap>(h1);
        } else {
          roots.appendNode(new DNode<Heap>(h1));
        }
      }
    }

    // consolidate trees so no two roots have same rank.
    // This is the tricky part.
    combine();

    // update min.
    DNode<Heap> newMin = roots;
    if (roots != null) {
      for (DNode<Heap> n = roots.next; n != roots; n = n.next) {
        if (compare(n.elem.v, newMin.elem.v) < 0) {
          newMin = n;
        }
      }
    }
    min = newMin;
    sz--;
    return hmin.v;
  }

  @SuppressWarnings("unchecked")
  private void combine() {
    // The key invariant of this data structure is that there can only be one
    // member of the root list that can have f(n) children. This bounds the
    // number of roots to to some n where fib(n) > total entries in fib heap

    // The maximum number of root nodes r can handle fib(r) elements

    // The closed for solution for the nth fibonacci number is :
    // fib(n) = phi^n - (1- phi)^n / sqrt(5);
    // where phi = 1 + sqrt(5) / 2; (the golden ratio)

    // F(N) is actually the closest integer to phi^n / sqrt(5).

    // for 2B entries, 2B ~=< fib(46), so we will fit in n= slots.
    // fib(40) ~= 107M
    // fib(20) ~= 6.8k

    // No nodes left, nothing to consolidate!
    if (roots == null)
      return;

    if (roots.next == roots)
      return; // only one root -- nothing to combine.x

    // I'll just over allocate and pay if the queue gets larger.
    DNode<Heap>[] aRanks = new DNode[40];
    List<DNode<Heap>> ranks = Arrays.asList(aRanks);

    ArrayList<DNode<Heap>> list = new ArrayList<DNode<Heap>>();

    list.add(roots);
    for (DNode<Heap> w = roots.next; w != roots; w = w.next) {
      list.add(w);
    }

    // DLNode<Heap> last = null;
    // DLNode<Heap> next = roots;

    // for (DLNode<Heap> w = roots; last == null || w != roots; w = next) {
    // if (next == null) {
    // break; // WTF!
    // }
    // next = next.next;
    // last = w;
    for (DNode<Heap> w : list) {
      // System.out.println("@ root " + w.elem.v);
      DNode<Heap> x = w;
      int deg = w.elem.numCh;
      while (ranks.get(deg) != null) {
        DNode<Heap> y = ranks.get(deg);
        // System.out.println(" another root with degree " + deg + " : "
        // + y.elem.v);
        if (compare(x.elem.v, y.elem.v) > 0) { // x > y
          DNode<Heap> temp = x;
          x = y;
          y = temp;
        }
        link(y, x);
        ranks.set(deg, null);
        // System.out.println("deg[" + deg + "] = " + null);
        deg++; // new linked/combined heap just increased degree by 1
      }
      // System.out.println("deg[" + deg + "] = " + x.elem.v);
      ranks.set(deg, x);
    }

  }

  // precondition: Y is larger than X.
  private void link(DNode<Heap> y, DNode<Heap> x) {
    // make a larger root be a child of a smaller root
    // heap attach

    if (y == roots) {
      roots = y.next;
    }

    Heap yh = y.remove();
    x.elem.addChild(yh);

    // System.out.println("  linked " + yh.v + " under " + x.elem.v);
  }

  /**
   * Take the roots list from argument and stitch it into our roos list
   * 
   * NOTE this breaks the fh2.. Do not use previous references after doing this
   * 
   * take A D E and union it with B C where A is root. => A B C D E
   */
  public void union(FibonacciHeap<T> fh2) {

    if (fh2.sz <= 0) {
      // nothing to do.
      return;
    }

    DNode<FibonacciHeap<T>.Heap> fh2last = fh2.roots.prev;
    roots.next.prev = fh2last;
    fh2last.next = roots.next;
    roots.next = fh2.roots;
    fh2.roots.prev = roots;

    sz += fh2.sz;

    if (compare(min.elem.v, fh2.min.elem.v) > 0) {
      min = fh2.min;
    }

  }

  // TODO decrease key
  // CUT
  // CASCADE CUT

}
