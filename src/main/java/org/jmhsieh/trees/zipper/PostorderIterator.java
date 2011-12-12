package org.jmhsieh.trees.zipper;

import java.util.Iterator;

import org.jmhsieh.structs.Stack;
import org.jmhsieh.trees.Tree2;


/**
 * Iterates through a expression tree in prefix order. return, left, right
 * 
 * @author Administrator
 * 
 */
public class PostorderIterator<T extends Tree2<T>> implements Iterator<T> {

  // initialized at the element to return
  ZLoc<T> loc;
  Stack<ZLoc<T>> stk;

  PostorderIterator(ZLoc<T> loc) {
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

  /**
   * 
   */
  @Override
  public T next() {
    if (loc.down() != null) {
      while (loc.down() != null) {
        loc = loc.down();
      }
      // I've left recursed all the way.
      // once an element is popped off it means not to visit its children.
      return loc.e;
    }

    if (loc.right() != null) {
      // move right and recurse
      loc = loc.right();
      return next();
    }

    // can't go down, and can't go right anymore, pop a level up
    if (stk.isEmpty()) {
      loc = null;
      throw new RuntimeException("asldfaslkjf");
      // return null;
    }

    // loc = stk.pop().right;
    loc = loc.up().right();

    // FIXME -- This is not done, and will not finish it until I need it.
    // FIXME
    // FIXME
    return null;
  }

  @Override
  public void remove() {
    throw new UnsupportedOperationException();
  }

}
