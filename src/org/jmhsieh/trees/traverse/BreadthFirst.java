package org.jmhsieh.trees.traverse;

import java.util.Deque;
import java.util.LinkedList;

import org.jmhsieh.Observer;
import org.jmhsieh.trees.Tree;


public class BreadthFirst<T> {
  Deque<Tree<T>> q = new LinkedList<Tree<T>>();
  Observer<T, ?> obs;

  public BreadthFirst(Observer<T, ?> o) {
    this.obs = o;
  }

  public void preorder(Tree<T> t) {
    if (t == null)
      return;

    obs.observe(t.getItem());

    q.addAll(t.children());
    Tree<T> v = q.pollFirst();
    if (v == null) {
      return;
    }
    preorder(v);
  }

  public void postorder(Tree<T> t) {
    if (t == null)
      return;

    q.addAll(t.children());
    Tree<T> v = q.pollFirst();
    if (v == null) {
      obs.observe(t.getItem());
      return;
    }
    postorder(v);
    
    obs.observe(t.getItem());

  }

}
