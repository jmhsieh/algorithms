package org.jmhsieh.trees.zipper;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.jmhsieh.trees.Tree2;


public class ZLoc<T extends Tree2<T>> {
  final ZPath<T> z;
  final T e;

  protected ZLoc(T e, ZPath<T> z) {
    this.z = z;
    this.e = e;
  }

  public ZLoc(T e) {
    this.z = new ZPath<T>();
    this.e = e;
  }

  public ZLoc<T> left() {
    if (z.isTop())
      return null;
    if (z.prevSibs.isEmpty())
      return null;

    // main case
    T psib = z.prevSibs.get(0);
    List<T> prev = new LinkedList<T>(z.prevSibs);
    prev.remove(0);
    List<T> next = new LinkedList<T>(z.nextSibs);
    next.add(0, e);
    ZPath<T> z2 = new ZPath<T>(prev, z.par, z.parent, next);
    return new ZLoc<T>(psib, z2);
  }

  public ZLoc<T> right() {
    if (z.isTop())
      return null;
    if (z.nextSibs.isEmpty())
      return null;

    // main case
    T psib = z.nextSibs.get(0);
    List<T> prev = new LinkedList<T>(z.prevSibs);
    prev.add(0, e);
    List<T> next = new LinkedList<T>(z.nextSibs);
    next.remove(0);
    ZPath<T> z2 = new ZPath<T>(prev, z.par, z.parent, next);
    return new ZLoc<T>(psib, z2);
  }

  public ZLoc<T> up() {
    if (z.isTop())
      return null;

    return new ZLoc<T>(z.par, z.parent); // TODO null? functional version
  }

  public ZLoc<T> down() {
    List<T> children = e.getChildren();
    if (children.size() == 0)
      return null;

    List<T> next = new LinkedList<T>(children);
    T cur = next.remove(0);
    return new ZLoc<T>(cur, new ZPath<T>(new LinkedList<T>(), e, z, next));
  }

  public T value() {
    return e;
  }

  public Iterable<T> preorderIterable() {
    return new Iterable<T>() {
      @Override
      public Iterator<T> iterator() {
        return new PreorderIterator<T>(ZLoc.this);
      }
    };

  }

  public Iterable<T> preorderIterable(final ZLocFilter<T> f) {
    return new Iterable<T>() {
      @Override
      public Iterator<T> iterator() {
        return new FilteredPreorderIterator<T>(ZLoc.this, f);
      }
    };
  }
}
