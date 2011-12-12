package org.jmhsieh.trees.zipper;

import java.util.Iterator;

import org.jmhsieh.trees.Tree2;


/**
 * Iterates through a expression tree in prefix order. return, left, right
 * 
 * @author Administrator
 * 
 */
public class PreorderIterator<T extends Tree2<T>> implements Iterator<T> {

  // initialized at the element to return
  ZLoc<T> loc;

  PreorderIterator(ZLoc<T> loc) {
    this.loc = loc;
  }

  @Override
  public boolean hasNext() {
    if (loc == null)
      return false;
    if (loc.down() != null)
      return true;
    if (loc.right() != null)
      return true;
    ZLoc<T> u = loc.up();
    while (u != null) {
      if (u.right() != null)
        return true;
      u = u.up();
    }
    return true; // last element
  }

  @Override
  public T next() {
    // ret val is the current location
    T ret = loc.e;

    ZLoc<T> d = loc.down();
    if (d != null) {
      loc = d;
      return ret;
    }

    ZLoc<T> r = loc.right();
    if (r != null) {
      loc = r;
      return ret;
    }

    ZLoc<T> next = loc;
    while (true) {
      ZLoc<T> u = next.up();
      if (u == null) {
        loc = u;
        return ret;
      }
      ZLoc<T> ur = u.right();
      if (ur == null) {
        next = u;
        continue;
      }
      loc = ur;
      return ret;
    }
  }

  @Override
  public void remove() {
    throw new UnsupportedOperationException();
  }

}
