package org.jmhsieh.trees.traverse;

import org.jmhsieh.Observer;
import org.jmhsieh.trees.Tree;
import org.jmhsieh.trees.TreeImpl;
import org.jmhsieh.trees.traverse.BreadthFirst;
import org.jmhsieh.trees.traverse.DepthFirst;

import junit.framework.TestCase;


public class TestBFS extends TestCase {

  public void testBFS() {
    final StringBuilder b = new StringBuilder();

    Observer<Integer, Integer> print = new Observer<Integer, Integer>() {
      @Override
      public void dispose() {

      }

      @Override
      public void error(Integer t) {
        System.err.print(t);
      }

      @Override
      public Integer observe(Integer t) {
        System.out.print(t + " ");
        b.append(t + " ");
        return null;
      }
    };

    BreadthFirst<Integer> traverse = new BreadthFirst<Integer>(print);

    Tree<Integer> t7 = new TreeImpl<Integer>(7);
    Tree<Integer> t6 = new TreeImpl<Integer>(6);
    Tree<Integer> t5 = new TreeImpl<Integer>(5);
    Tree<Integer> t4 = new TreeImpl<Integer>(4);
    Tree<Integer> t3 = new TreeImpl<Integer>(3, t6, t7);
    Tree<Integer> t2 = new TreeImpl<Integer>(2, t4, t5);
    Tree<Integer> t1 = new TreeImpl<Integer>(1, t2, t3);

    System.out.println("BFS Pre order");
    traverse.preorder(t1);
    System.out.println();
    assertEquals("1 2 3 4 5 6 7 ", b.toString());
    b.setLength(0);

    System.out.println("BFS Post order");
    traverse.postorder(t1);
    System.out.println();
    assertEquals("7 6 5 4 3 2 1 ", b.toString());
    b.setLength(0);
  }

  public void testDFS() {
    final StringBuilder b = new StringBuilder();

    Observer<Integer, Integer> print = new Observer<Integer, Integer>() {

      @Override
      public void dispose() {
      }

      @Override
      public void error(Integer t) {
        System.err.print(t);
      }

      @Override
      public Integer observe(Integer t) {
        System.out.print(t + " ");
        b.append(t + " ");
        return null;
      }
    };

    DepthFirst<Integer> traverse = new DepthFirst<Integer>(print);

    Tree<Integer> t1 = new TreeImpl<Integer>(1);
    Tree<Integer> t2 = new TreeImpl<Integer>(2);
    Tree<Integer> t3 = new TreeImpl<Integer>(3, t1, t2);
    Tree<Integer> t4 = new TreeImpl<Integer>(4);
    Tree<Integer> t5 = new TreeImpl<Integer>(5);
    Tree<Integer> t6 = new TreeImpl<Integer>(6, t4, t5);
    Tree<Integer> t7 = new TreeImpl<Integer>(7, t3, t6);

    System.out.println("DFS Pre order");
    traverse.preorder(t7);
    System.out.println();
    assertEquals("7 6 5 4 3 2 1 ", b.toString());
    b.setLength(0);

    System.out.println("DFS Post order");
    traverse.postorder(t7);
    System.out.println();
    assertEquals("1 2 3 4 5 6 7 ", b.toString());
    b.setLength(0);

  }
}