package org.jmhsieh.util;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * This is a simple LRU cache implementation. Eventually I should replace this
 * with a implementation that uses WeakReferences (so the GC can collect here if
 * it needs memory)
 * 
 * @author jmhsieh
 * 
 * @param <K>
 * @param <V>
 */
public class Cache<K, V> {

  final int maxsize;
  LinkedList<K> fifo;
  Map<K, SoftReference<V>> map;

  public Cache(int maxsize) {
    this.maxsize = maxsize;
    this.fifo = new LinkedList<K>();
    this.map = new HashMap<K, SoftReference<V>>();
  }

  public V lookup(K key) {
    SoftReference<V> val = map.get(key);
    if (val == null) {
      return null;
    }

    V v = val.get();
    if (v == null) {
      return null;
    }

    // update position (tail of list is most recently used)
    boolean ok = fifo.remove(key);
    assert (ok);
    fifo.addLast(key);
    return v;
  }

  /**
   * update lru cache, and return evicted or old value. Returns null otherwise.
   * 
   * @param key
   * @param val
   * @return
   */
  public V insert(K key, V val) {
    if (key == null || val == null) {
      return null;
    }

    V v0 = lookup(key); // updates position
    if (v0 != null) {
      // update map entry
      map.put(key, new SoftReference<V>(val));
      // (if same, nothing changes)
      return v0;
    }

    SoftReference<V> ret = null;
    // if too big, evict
    if (fifo.size() >= maxsize) {
      K kOut = fifo.removeFirst();
      ret = map.get(kOut);
      map.remove(kOut);
    }

    // install new pair
    fifo.addLast(key);
    map.put(key, new SoftReference<V>(val));
    return (ret == null) ? null : ret.get();
  }

  public void clear() {
    fifo.clear();
    map.clear();
  }
}
