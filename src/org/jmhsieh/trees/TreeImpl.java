package org.jmhsieh.trees;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * A simple implementation of an n-ary tree
 */
public class TreeImpl<T> implements Tree<T> {
  T item;
  List<Tree<T>> children;

  public TreeImpl(T item, Tree<T>... children) {
    this.item = item;
    this.children = Arrays.asList(children);
  }

  @Override
  public Collection<Tree<T>> children() {
    return Collections.unmodifiableList(children);
  }

  @Override
  public Iterator<Tree<T>> contained() {
    return children.iterator();
  }

  @Override
  public T getItem() {
    return item;
  }

}
