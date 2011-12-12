package org.jmhsieh.structs;

import java.util.AbstractQueue;
import java.util.Iterator;

/**
 * Note this implementation is not synchronized.
 */
public class FibPriorityQueue<E> extends AbstractQueue<E> {

  final FibonacciHeap<E> fh = new FibonacciHeap<E>();

  @Override
  public Iterator<E> iterator() {
    return new Iterator<E>() {
      final FibonacciHeap<E> fheap = fh;
      DNode<FibonacciHeap<E>.Heap> n = null;
      // Stack<FibonacciHeap<E>.Heap> h = null;
      FibonacciHeap<E>.Heap hc = null;

      @Override
      public boolean hasNext() {
        return (fheap.sz != 0);
      }

      @Override
      public E next() {
        // TODO
        throw new UnsupportedOperationException();
      }

      @Override
      public void remove() {
        throw new UnsupportedOperationException();
      }

    };

  }

  @Override
  public int size() {
    return fh.sz;
  }

  @Override
  public boolean offer(E e) {
    fh.insert(e);
    return true;
  }

  @Override
  public E peek() {
    return fh.peekMin();
  }

  @Override
  public E poll() {
    return fh.deleteMin();
  }

}
