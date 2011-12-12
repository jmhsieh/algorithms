package org.jmhsieh.trees;

import java.util.Collection;
import java.util.Iterator;

public interface Tree<T> {
  Collection<Tree<T>> children();

  Iterator<Tree<T>> contained();

  T getItem();
}
