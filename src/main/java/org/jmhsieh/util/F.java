package org.jmhsieh.util;

import java.util.ArrayList;
import java.util.Collection;

import org.jmhsieh.structs.Pair;


/**
 * This provides functional coding style functions like 'map', and 'filter'
 * 
 * Fun<T,U> acts like a lambda and is instantiated as an inner class and used as
 * anonymous functions.
 * 
 * @author jmhsieh
 * 
 */
public class F {

  public interface Fun<T, U> {
    U call(T t);
  }

  @SuppressWarnings("unchecked")
  public static <T, U> Collection<U> map(Fun<T, U> c, Collection<T> src,
      Class<?> clz) throws InstantiationException, IllegalAccessException {

    Collection<U> lst = (Collection<U>) clz.newInstance();

    for (T e : src) {
      U e2 = c.call(e);
      lst.add(e2);
    }

    return lst;
  }

  public static <T, U> Collection<U> map(Fun<T, U> c, Collection<T> src,
      Collection<U> dst) {
    assert (src != dst);
    for (T e : src) {
      U e2 = c.call(e);
      dst.add(e2);
    }

    return dst;
  }

  public static <T, U> Collection<U> map(Fun<T, U> c, T[] src, Collection<U> dst) {
    for (T e : src) {
      U e2 = c.call(e);
      dst.add(e2);
    }
    return dst;
  }

  public static <T, U> Collection<U> map(Fun<T, U> c, Collection<T> src) {
    return map(c, src, new ArrayList<U>());
  }

  public static <T> Collection<T> filter(Fun<T, Boolean> c, Collection<T> src,
      Collection<T> dst) {
    for (T e : src) {
      if (c.call(e)) {
        dst.add(e);
      }
    }
    return dst;

  }

  @SuppressWarnings("unchecked")
  public static <T> Collection<T> filter(Fun<T, Boolean> c, Collection<T> src) {
    try {
      Collection<T> l2 = (Collection<T>) src.getClass().newInstance();
      for (T e : src) {
        if (c.call(e)) {
          l2.add(e);
        }
      }
      return l2;
    } catch (InstantiationException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return null;
  }

  // implements foldr with type signature: (T->U->T) -> T -> Collection(U) -> T
  public static <T, U> T foldl(Fun<Pair<T, U>, T> f, T acc, Collection<U> lst) {
    if (lst.isEmpty())
      return acc;

    for (U u : lst) {
      acc = f.call(new Pair<T, U>(acc, u));
    }
    return acc;
  }

  public static <T> boolean forall(Fun<T, Boolean> f, Collection<T> src) {
    for (T e : src) {
      if (!f.call(e))
        return false;
    }
    return true;
  }

  public static <T> boolean exists(Fun<T, Boolean> f, Collection<T> src) {
    for (T e : src) {
      if (f.call(e))
        return true;
    }
    return false;
  }

}
