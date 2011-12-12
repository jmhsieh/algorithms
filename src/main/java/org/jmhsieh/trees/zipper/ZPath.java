package org.jmhsieh.trees.zipper;

import java.util.LinkedList;
import java.util.List;

/**
 * From huet's paper: The ExprPath is the "path". The Expression the "tree". The
 * ExprLoc is the "location.
 * 
 * @author jmhsieh
 */
public class ZPath<T> {
  final T par;
  final ZPath<T> parent; // the "spine", if this is null, it is the top.
  final List<T> prevSibs;
  final List<T> nextSibs;

  // create the TOP path.
  ZPath() {
    this.par = null;
    this.parent = null;
    this.prevSibs = new LinkedList<T>();
    this.nextSibs = new LinkedList<T>();
  }

  // non top path
  ZPath(List<T> prev, T par, ZPath<T> parent,
      List<T> next) {
    this.par = par;
    this.parent = parent;
    this.nextSibs = next;
    this.prevSibs = prev;
  }

  boolean isTop() {
    return parent == null;
  }
}
