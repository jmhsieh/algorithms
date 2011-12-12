package org.jmhsieh.trees;

import java.util.List;

public interface Tree2<T> extends Tree<T> {
  List<T> getChildren(); 
}
