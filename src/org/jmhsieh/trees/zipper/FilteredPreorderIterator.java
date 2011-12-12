package org.jmhsieh.trees.zipper;

import java.util.Iterator;

import org.jmhsieh.trees.Tree2;


public class FilteredPreorderIterator<T extends Tree2<T>> implements
    Iterator<T> {

  // initialized at the element to return
  ZLoc<T> loc;
  ZLocFilter<T> f;

  FilteredPreorderIterator(ZLoc<T> loc, ZLocFilter<T> f) {
    this.loc = loc;
    this.f = f;
  }

  @Override
  public boolean hasNext() {
    if (loc == null)
      return false;
    if (f.downOk(loc.e) && loc.down() != null)
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
    if (f.downOk(ret) && d != null) {
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
