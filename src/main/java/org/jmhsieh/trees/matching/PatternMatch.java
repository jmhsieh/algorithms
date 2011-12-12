package org.jmhsieh.trees.matching;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jmhsieh.structs.Pair;
import org.jmhsieh.trees.Tree;


/**
 * Pattern matching for my pipe language.
 * 
 * @author jmhsieh
 */
public class PatternMatch<T> {

  public PatternMatch(PType p, Object... args) throws PatternMatchException {
    check(p, args);
    this.pt = p;
    this.opts = args;
  }

  PType pt;
  Object[] opts;
  T instance;

  /**
   * PWild - wild card
   * 
   * PVar String - binding pattern
   * 
   * PContains - Composed (like mutable pipe, compound pipe or thread pipe)
   * 
   * PType Class - matched pipe class/interface type
   * 
   * 
   * @author Administrator
   */
  public enum PType {
    P_WILD, P_CONTAINS, P_CONTAINS_RECURSIVE, P_VAR, P_TYPE, P_SUBSCRIBER_RECURSIVE, P_SUBSCRIBER, P_RECURSIVE, P_EXACT
  };

  @SuppressWarnings("serial")
  public static class PatternMatchException extends Exception {
  };

  /**
   * Checks to make sure pattern instantiation is ok.
   * 
   * 
   * 
   * @param p
   * @param opts
   * @throws PatternMatchException
   */
  @SuppressWarnings("unchecked")
  void check(PType p, Object[] opts) throws PatternMatchException {
    // return if constraints met, else, throw exception
    switch (p) {
    case P_VAR:
      if (opts.length == 1 && opts[0] instanceof String)
        return;

      for (int i = 1; i < opts.length; i++) {
        if (!(opts[i] instanceof PatternMatch)) {
          throw new PatternMatchException();
        }
      }
      return;
      // throw new PatternMatchException();
    case P_WILD:
      if (opts.length == 0)
        return;
      throw new PatternMatchException();
    case P_TYPE:
      if (opts.length == 1 && opts[0] instanceof Class) {
        Class<Tree<T>> pclz = (Class<Tree<T>>) instance.getClass();
        Class<?> clz = (Class<?>) opts[0];
        if (isSubType(pclz, clz))
          return;
      }
      throw new PatternMatchException();
    case P_EXACT:
      // if (opts.length == 1 && opts[0] instanceof T) {
      if (opts.length == 1
          && opts[0].getClass().isInstance(instance.getClass())) {
        return;
      }
    case P_SUBSCRIBER:
      // recursive type
      if (opts.length == 2 && opts[0] instanceof PatternMatch
          && opts[1] instanceof PatternMatch) {
        return;
      }
      throw new PatternMatchException();
    case P_CONTAINS:
    case P_SUBSCRIBER_RECURSIVE:
    case P_CONTAINS_RECURSIVE:
    case P_RECURSIVE:
      // recursive type
      if (opts.length == 1 && opts[0] instanceof PatternMatch) {
        return;
      }
      throw new PatternMatchException();
    default:
      throw new PatternMatchException();
    }
  }

  boolean isSubType(Class<?> superClz, Class<?> subClz) {
    if (subClz == null)
      return false;

    // same type
    if (superClz == subClz)
      return true;

    // implements interface
    Class<?>[] ifc = subClz.getInterfaces();
    for (int i = 0; i < ifc.length; i++) {
      if (ifc[i] == superClz) {
        return true;
      }
    }

    // super class
    Class<?> superSub = subClz.getSuperclass();
    return isSubType(superClz, superSub);

  }

  @SuppressWarnings("unchecked")
  public List<Pair<String, Tree<T>>> match(Tree<T> v) {
    if (v == null) {
      return null;
    }

    switch (pt) {
    case P_VAR:
      // bind a pattern variable to the
      String x = (String) opts[0];

      List<Pair<String, Tree<T>>> l = new ArrayList<Pair<String, Tree<T>>>();
      for (int i = 1; i < opts.length; i++) {
        PatternMatch<T> pp = (PatternMatch<T>) opts[i];
        List<Pair<String, Tree<T>>> mc = pp.match(v);
        if (mc == null) {
          // all constraints must be met
          return null;
        }
        l.addAll(mc);
      }

      l.add(new Pair<String, Tree<T>>(x, v));
      return l;
    case P_WILD:
      // accept pipe
      return new ArrayList<Pair<String, Tree<T>>>();
    case P_TYPE:
      Class<?> clz = (Class<?>) opts[0];
      Class<?> vclz = v.getClass();
      if (isSubType(clz, vclz))
        return new ArrayList<Pair<String, Tree<T>>>();
      return null;
    case P_EXACT:
      Tree<T> p = (Tree<T>) opts[0];
      if (v == p) {
        return new ArrayList<Pair<String, Tree<T>>>(); // We matched!
      }
      return null; // we didn't match

    case P_SUBSCRIBER: {
      // this was checked on construction.
      PatternMatch<T> producer = (PatternMatch<T>) opts[0];
      PatternMatch<T> consumer = (PatternMatch<T>) opts[1];
      List<Pair<String, Tree<T>>> prodMatch = producer.match(v);
      if (prodMatch == null) {
        return null;
      }

      // Iterator<Tree<T>> iter = v.children();
      // while (iter.hasNext()) {
      // Tree<T> vi = (Tree<T>) iter.next();
      // this will just find the first match

      for (Tree<T> vi : v.children()) {
        List<Pair<String, Tree<T>>> consMatch = consumer.match(vi);
        if (consMatch != null) {
          // success!
          prodMatch.addAll(consMatch);
          return prodMatch;
        }
      }
      return null;
    }
    case P_SUBSCRIBER_RECURSIVE: {
      PatternMatch<T> pat = (PatternMatch<T>) opts[0];

      // base case
      List<Pair<String, Tree<T>>> m1 = pat.match(v);
      if (m1 != null) {
        return m1;
      }

      // recursive case
      // Iterator<Tree<T>> iter = v.children();
      // // this will just find the first match
      // while (iter.hasNext()) {
      // Tree<T> vi = (Tree<T>) iter.next();
      //        
      for (Tree<T> vi : v.children()) {
        List<Pair<String, Tree<T>>> match = this.match(vi);
        if (match != null) {
          // success!
          return match;
        }
      }
      return null;
    }

      // case P_CONTAINS: {
      // PatternMatch pat = (PatternMatch) opts[0];
      //
      // // recursive case
      // Iterator<T> iter = v.contained(); // subscribers();
      // if (iter == null) {
      // return null;
      // }
      // // this will just find the first match
      // while (iter.hasNext()) {
      // T vi = iter.next();
      // List<Pair<String, Tree<T>>> match = pat.match(vi);
      // if (match != null) {
      // // success!
      // return match;
      // }
      // }
      // return null;
      // }
      // case P_CONTAINS_RECURSIVE: {
      // PatternMatch pat = (PatternMatch) opts[0];
      //
      // // base case
      // List<Pair<String, Tree<T>>> m1 = pat.match(v);
      // if (m1 != null) {
      // return m1;
      // }
      //
      // // recursive case
      // Iterator<? extends T> iter = v.contained();
      // // this will just find the first match
      // while (iter.hasNext()) {
      // T vi = iter.next();
      // List<Pair<String, Tree<T>>> match = this.match(vi);
      // if (match != null) {
      // // success!
      // return match;
      // }
      // }
      //
      // return null;
      //
      // }

    case P_RECURSIVE: {
      PatternMatch<T> pat = (PatternMatch<T>) opts[0];

      // base case
      List<Pair<String, Tree<T>>> m1 = pat.match(v);
      if (m1 != null) {
        return m1;
      }

      // recursive contain case
      Iterator<Tree<T>> iter = v.contained();
      if (iter != null) {
        // this will just find the first match
        while (iter.hasNext()) {
          Tree<T> vi = (Tree<T>) iter.next();
          List<Pair<String, Tree<T>>> match = this.match(vi);
          if (match != null) {
            // success!
            return match;
          }
        }
      }

      // recursive case
//      Iterator<Tree<T>> iter2 = v.children();
//      // this will just find the first match
//      while (iter2.hasNext()) {
//        Tree<T> vi = iter2.next();
      for (Tree<T> vi : v.children()) {
        List<Pair<String, Tree<T>>> match = this.match(vi);
        if (match != null) {
          // success!
          return match;
        }
      }

      return null;
    }
    default:
      break;
    }

    return null;
  }

  public static <T> PatternMatch<T> contains(PatternMatch<T> pp)
      throws PatternMatchException {
    return new PatternMatch<T>(PType.P_CONTAINS, pp);
  }

}
