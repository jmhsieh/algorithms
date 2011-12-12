package org.jmhsieh.sets.consistant;

import java.util.List;

public interface MoveHandler<K, V> {
  public void moved(K from, K to, List<V> values);

  public void rebuild(K key, List<V> allVals);
}
