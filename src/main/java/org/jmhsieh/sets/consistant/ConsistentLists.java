package org.jmhsieh.sets.consistant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConsistentLists<K, V> {
  // consistent hash that assigns T's to K's
  final ConsistentHash<K> keyHash;

  // buckets the filters live in
  final Map<K, List<V>> dist;

  final List<MoveHandler<K, V>> listeners = new ArrayList<MoveHandler<K, V>>();

  public ConsistentLists(int replicationFactor) {
    keyHash = new ConsistentHash<K>(replicationFactor, new ArrayList<K>());
    dist = new HashMap<K, List<V>>();
  }

  public Map<K, List<V>> getBuckets() {
    return dist;
    // Map<K, List<V>> buckets = new HashMap<K,List<V>>();
    // return ) dist.clone();
  }

  public void addMoveListener(MoveHandler<K, V> l) {
    listeners.add(l);
  }

  public void removeMoveListenver(MoveHandler<K, V> l) {
    listeners.remove(l);
  }

  private void fireMoved(K from, K to, List<V> vals) {
    if (vals == null || vals.size() == 0)
      return;
    for (MoveHandler<K, V> l : listeners) {
      l.moved(from, to, vals);
    }
  }

  public void rebuild() {
    for (K from : dist.keySet()) {
      List<V> vals = dist.get(from);
      fireRebuild(from, vals);
    }
  }

  private void fireRebuild(K from, List<V> allVals) {
    for (MoveHandler<K, V> l : listeners) {
      l.rebuild(from, allVals);
    }
  }

  /**
   * figure out which node to assign the filter, then add it.
   */
  public void addValue(V exp) {
    K w = keyHash.get(exp);
    List<V> old = dist.get(w);
    if (old == null) {
      old = new ArrayList<V>();
    }

    List<V> newList = new ArrayList<V>();
    newList.addAll(old); // copies refs to list items
    newList.add(exp);
    dist.put(w, newList);

    // send alerts
    List<V> vals = new ArrayList<V>();
    vals.add(exp);
    fireMoved(null, w, vals);
  }

  /**
   * figure out which node is supposed to have the filter and remove it.
   */
  public void removeValue(V exp) {
    K w = keyHash.get(exp);
    List<V> old = dist.get(w);
    if (old == null) {
      throw new RuntimeException(
          "Weird! cannot remove item that doesn't exist: " + exp);
    }

    List<V> newList = new ArrayList<V>();
    newList.addAll(old);
    newList.remove(exp);
    dist.put(w, newList);

    List<V> vals = new ArrayList<V>();
    vals.add(exp);
    fireMoved(w, null, vals);

  }

  /**
   * Adding a new worker to the set. Need to make sure things are consistent.
   */
  public void addKey(K w) {
    keyHash.add(w);

    // now I need to march through all of the other buckets to figure out which
    // ones moved and move them.
    List<V> addDisj = new ArrayList<V>();
    for (K w2 : dist.keySet()) {
      List<V> de = dist.get(w2);

      List<V> newDisj = new ArrayList<V>();
      List<V> movingDisj = new ArrayList<V>();
      for (V e : de) {
        K w3 = keyHash.get(e);
        // didn't move, do nothing
        if (w3 == w) {
          movingDisj.add(e);
          // addDisj.add(e);
        } else if (w2 == w3) {
          newDisj.add(e);
        } else {
          // WTF? It moved to some unexpected node. BUG!
          throw new RuntimeException(
              "wtf;  expression moved to a random worker!");
        }
      }
      // replace previous bucket with new accurate bucket
      dist.put(w2, newDisj);
      addDisj.addAll(movingDisj);
      fireMoved(w2, w, movingDisj);
    }
    // and now add the buckets for the new worker
    dist.put(w, addDisj);
  }

  public void removeKey(K w) {
    List<V> moving = dist.get(w);
    keyHash.remove(w);

    // go through each remaining worker
    for (K w2 : dist.keySet()) {
      List<V> old = dist.get(w2);
      List<V> moved = new ArrayList<V>();
      List<V> newList = new ArrayList<V>();
      newList.addAll(old);
      // go through the list of expression from the one getting removed
      for (V e : moving) {
        K w3 = keyHash.get(e);
        if (w3 == w2) {
          // add it
          moved.add(e);
        }
      }
      newList.addAll(moved);
      dist.put(w2, newList);
      fireMoved(w, w2, moved);
    }
    // everyone should be assigned

    // remove the list
    dist.remove(w);
  }

  @Override
  public String toString() {
    StringBuffer buf = new StringBuffer();
    for (K k : dist.keySet()) {
      List<V> vs = dist.get(k);
      buf.append(k);
      buf.append(" => ");
      buf.append(vs);
      buf.append("\n");
    }
    return buf.toString();
  }

  public List<K> keys() {
    List<K> ks = new ArrayList<K>();
    ks.addAll(dist.keySet());
    return ks;
  }

}
