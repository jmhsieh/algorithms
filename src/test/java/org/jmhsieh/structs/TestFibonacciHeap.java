package org.jmhsieh.structs;

import org.jmhsieh.structs.DNode;
import org.jmhsieh.structs.FibonacciHeap;

import junit.framework.TestCase;

public class TestFibonacciHeap extends TestCase {

  void printNexts(DNode<String> root) {
    System.out.print("next: ");
    if (root.next == root) {
      System.out.println(" -> " + root.elem);
    } else {
      DNode<String> n = root;
      do {
        System.out.print(" -> " + n.elem);
        n = n.next;
      } while (n != root);
      System.out.println("");
    }
  }

  void printPrevs(DNode<String> root) {
    System.out.print("prev: ");
    if (root.prev == root) {
      System.out.println(" -> " + root.elem);
    } else {
      DNode<String> n = root;
      do {
        System.out.print(" -> " + n.elem);
        n = n.prev;
      } while (n != root);
      System.out.println("");
    }
  }

  public void testDLNode() {
    DNode<String> root = new DNode<String>("A");
    printNexts(root);
    printPrevs(root);

    root.append("C");
    printNexts(root);
    printPrevs(root);

    root.append("B");
    printNexts(root);
    printPrevs(root);

    root.append("D");
    printNexts(root);
    printPrevs(root);

  }

  void dumpHeap(FibonacciHeap<String>.Heap h) {
    if (h.children == null) {
      System.out.print(h.v);
      return;
    }

    DNode<FibonacciHeap<String>.Heap> n = h.children;
    do {
      System.out.print(" <=>  ( ");
      dumpHeap(n.elem);
      System.out.println(" ) ");
      n = n.next;
      // System.out.println();
    } while (n != h.children);
    System.out.println("");

  }

  void dumpFHeap(FibonacciHeap<String> fh) {
    if (fh.min == null) {
      System.out.println("Fib heap is empty!");
      return;
    }
    System.out.println("min : " + fh.min.elem.v);

    DNode<FibonacciHeap<String>.Heap> root = fh.roots;
    if (root.prev == root) {
      System.out.print(" <-> " + root.elem.v + " :: ");
      dumpHeap(root.elem);
    } else {
      DNode<FibonacciHeap<String>.Heap> n = root;
      do {
        System.out.print(" <-> " + n.elem.v + " :: ");
        dumpHeap(n.elem);
        n = n.prev;
      } while (n != root);
    }
    System.out.println("");
  }

  public void testFibHeap() {
    FibonacciHeap<String> fh = new FibonacciHeap<String>();

    fh.insert("3 Third");
    dumpFHeap(fh);
    fh.insert("2 Second");
    dumpFHeap(fh);
    fh.insert("4 Last");
    dumpFHeap(fh);
    fh.insert("1 First");
    dumpFHeap(fh);

    while (fh.peekMin() != null) {
      String i = fh.deleteMin();
      dumpFHeap(fh);
      System.out.println("Output: " + i);

    }
  }

  public void testFibHeapInterspersed() {
    FibonacciHeap<Integer> fh = new FibonacciHeap<Integer>();

    fh.insert(30);
    fh.insert(20);
    fh.insert(10);
    fh.insert(25);

    int i = fh.deleteMin();
    System.out.println("Output: " + i);
    assertEquals(10, i);

    fh.insert(5);
    i = fh.deleteMin();
    System.out.println("Output: " + i);
    assertEquals(5, i);

    fh.insert(27);
    i = fh.deleteMin();
    System.out.println("Output: " + i);
    assertEquals(20, i);

    fh.insert(28);
    fh.insert(29);
    i = fh.deleteMin();
    System.out.println("Output: " + i);
    assertEquals(25, i);
    i = fh.deleteMin();
    System.out.println("Output: " + i);
    assertEquals(27, i);
    i = fh.deleteMin();
    System.out.println("Output: " + i);
    assertEquals(28, i);
    i = fh.deleteMin();
    System.out.println("Output: " + i);
    assertEquals(29, i);

    i = fh.deleteMin();
    System.out.println("Output: " + i);
    assertEquals(30, i);

  }

  public void testFibHeapUnion() {
    FibonacciHeap<Integer> fh = new FibonacciHeap<Integer>();

    fh.insert(30);
    fh.insert(20);
    fh.insert(10);
    fh.insert(40);

    FibonacciHeap<Integer> fh2 = new FibonacciHeap<Integer>();
    fh2.insert(35);
    fh2.insert(5);
    fh2.insert(15);
    fh2.insert(25);

    fh.union(fh2);

    int[] ans = { 5, 10, 15, 20, 25, 30, 35, 40 };
    for (int j = 0; j < ans.length; j++) {
      int i = fh.deleteMin();
      System.out.println("Output: " + i);
      assertEquals(ans[j], i);
    }
  }
}
