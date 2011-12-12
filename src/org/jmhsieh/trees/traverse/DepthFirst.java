package org.jmhsieh.trees.traverse;

import java.util.ArrayDeque;
import java.util.Deque;

import org.jmhsieh.Observer;
import org.jmhsieh.trees.Tree;


public class DepthFirst<T> {
  Deque<Tree<T>> dq = new ArrayDeque<Tree<T>>();
  Observer<T, ?> obs;

  public DepthFirst(Observer<T, ?> o) {
    this.obs = o;
  }

  public void preorder(Tree<T> t) {
    if (t == null)
      return;

    obs.observe(t.getItem());

    for (Tree<T> u : t.children()) {
      dq.addFirst(u);
    }

    Tree<T> v = dq.pollFirst();
    if (v == null) {
      return;
    }

    preorder(v);
  }

  public void postorder(Tree<T> t) {
    if (t == null)
      return;

    for (Tree<T> u : t.children()) {
      dq.addFirst(u);
    }

    Tree<T> v = dq.pollFirst();
    if (v == null) {
      obs.observe(t.getItem());
      return;
    }
    postorder(v);

    obs.observe(t.getItem());

  }
}
