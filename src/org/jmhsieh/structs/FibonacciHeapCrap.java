package org.jmhsieh.structs;

/**
 * Code originally from here:
 * 
 * http://www.cs.northwestern.edu/~agupta/_projects/algorithms/Fibonacci%20Heaps
 * /
 * 
 * TODO: This code is seriously buggy
 * 
 */

public class FibonacciHeapCrap<T> {

  static class FBNode<T> {
    T node = null;
    int key;
    int outdegree = 0;
    boolean marked = false;

    // there are two double linked lists here.
    FBNode<T> next = null, prev = null; // roots list chain
    FBNode<T> child = null, parent = null; // child tree chain

    FBNode(T value, int key) {
      this.key = key;
      node = value;
      prev = next = this;
    }

  }

  private FBNode<T> head = null; // pointer to roots
  private FBNode<T> min = null; // pointer to min.
  private int n = 0; // number of elements
  private int maxn = 0;

  static int MIN = -5000;

  public FibonacciHeapCrap() {
  }

  public FBNode<T> insert(T node, int value) {
    // TODO: Add your code here
    FBNode<T> newHead = new FBNode<T>(node, value);

    // debug("heap2");

    if (n == 0 || head == null) {
      // first insert anywhere
      head = newHead;
      min = head;
      n++;
    } else {
      // ___Insert in beginning of list___
      newHead.next = head;
      newHead.prev = head.prev;
      head.prev.next = newHead;
      head.prev = newHead;

      head = newHead;

      if (min == null || min.key > newHead.key)
        min = newHead;
      n++;

    }

    if (n > maxn)
      maxn = n;
    return newHead;

  }

  private void insert(FBNode<T> h) {
    // TODO: Add your code here
    h.parent = null;
    h.marked = false;

    if (n == 0 || head == null) {
      n = 0;
      h.prev = h.next = h;
      head = h;
      min = head;
      n++;
    } else {
      // ___Insert in beginning of list___
      h.next = head;
      h.prev = head.prev;
      head.prev.next = h;
      head.prev = h;

      head = h;

      if (min == null || min.key > h.key)
        min = h;

      n++;
    }

    if (n > maxn)
      maxn = n;

  }

  // ____return null if empty____
  public T returnMin() {
    if (n == 0)
      return null;
    return min.node;
  }

  public T deleteMin() {
    FBNode<T> m = min;
    // debug("delete min start");
    // display(head,0);
    if (n == 0)
      return null;
    if (m != null) {

      // debug("adding its children to root list");
      int oldn = n;

      FBNode<T> next = m.child;
      FBNode<T> childt = next;
      if (next != null) {
        FBNode<T> lastchild = m.child.prev;

        do {
          childt = next;
          // debug("child is : " + childt.node.returnkey());
          next = childt.next;
          insert(childt);
        } while (childt != lastchild);
      } else {
        // debug("no child");
      }

      // debug("removing min from list of nodes");
      // ____remove m from the list of nodes
      m.prev.next = m.next;
      m.next.prev = m.prev;
      if (head == m)
        head = m.next;

      // debug("going to combine");
      // display(head,0);

      // ____update minimum pointer
      if (m.next == m)
        min = null;
      else
        min = m.next;

      // ___combine

      combine();
      n = oldn - 1;

      // debug("n= " + n);
    }
    return m.node;
  }

  public FBNode<T> link(FBNode<T> head1, FBNode<T> head2) {
    if (head1.outdegree != head2.outdegree)
      return null;

    FBNode<T> parent, child;

    parent = head1;
    child = head2;

    if (parent.child != null) {
      FBNode<T> lastchild = parent.child.prev;
      lastchild.next = child;
      child.next = parent.child;
      child.prev = lastchild;
      parent.child.prev = child;

    } else {
      parent.child = child;
      child.next = child.prev = child;

    }

    child.parent = parent;

    child.marked = false;

    parent.outdegree++;

    return parent;

  }

  @SuppressWarnings("unchecked")
  public void combine() {
    // debug("no of nodes : " + n);
    int arraysize = (int) (Math.log((double) maxn) / Math.log(1.62));
    FBNode<T> array[] = new FBNode[arraysize];
    int i = 0;
    for (i = 0; i < arraysize; i++)
      array[i] = null;

    FBNode<T> t = head;
    FBNode<T> x, y;
    int d;

    // debug("Consolidating");
    // display(head,0);
    do {
      // ___Deal with special case___
      if (t == null)
        break;

      FBNode<T> next = t.next;
      x = t;
      d = x.outdegree;
      // debug("Con : " + x.node.returnkey() + "degree : " + d);

      while (array[d] != null) {
        // debug("linking...");
        y = array[d];
        if (x.key > y.key) {
          FBNode<T> t1 = x;
          x = y;
          y = t1;
        }

        link(x, y); // ___x is the parent

        array[d] = null;

        d++;

      }
      // debug("assigning...");
      array[d] = x;

      t = next;
      // debug("t : " + t.node.returnkey());
    } while (t != head);

    min = null;
    head = null;

    // debug("array to list");
    n = 0;
    for (i = 0; i < arraysize; i++) {
      // debug("on array pos : " +i);
      if (array[i] != null) {
        insert(array[i]);
        if (min == null || array[i].key < min.key)
          min = array[i];
      }
    }

    // display(head,0);

  }

  public boolean decreaseKey(FBNode<T> fnode, int val) {
    if (val > fnode.key)
      return false;

    fnode.key = val;

    FBNode<T> p = fnode.parent;
    if (p != null && fnode.key < p.key) {
      cut(fnode);
      cascadeCut(p);
    }

    if (fnode.key < min.key)
      min = fnode;

    return true;

  }

  public T returnNode(FBNode<T> fnode) {
    return fnode.node;
  }

  private void cut(FBNode<T> child) {

    // ___Remove child from its parent list
    // ___If only one child___
    if (child.next == child) {
      child.parent.child = null;
    } else {
      child.prev.next = child.next;
      child.next.prev = child.prev;
      if (child.parent.child == child) {
        child.parent.child = child.next;
      }
    }

    child.parent.outdegree--;

    insert(child);

  }

  private void cascadeCut(FBNode<T> c) {
    FBNode<T> p = c.parent;
    if (p != null) // If parent exists
    {
      if (c.marked == false) {
        c.marked = true;
      } else {
        cut(c);
        cascadeCut(p);
      }
    }
  }

  public void display(FBNode<T> head, int space) {
    if (head == null)
      return;

    FBNode<T> t = head;
    do {
      // //debug("infinite");
      int i;
      for (i = 0; i < space; i++)
        System.out.print(" ");
      // System.out.println(t.key);
      display(t.child, space + 2);

      t = t.next;
    } while (t != head);

    System.out.flush();
  }

  public void debug(String str) {
    System.out.println(str);
    System.out.flush();
  }

}
