package org.jmhsieh.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.TreeSet;

import org.jmhsieh.util.F;


import junit.framework.TestCase;

/**
 * Tests of functional method -- map and filter
 * 
 * @author Administrator
 * 
 */
public class TestF extends TestCase {

  public void testMap() {
    ArrayList<Integer> l = new ArrayList<Integer>();
    int[] list = { 1, 2, 3, 4, 54, };
    for (int i = 0; i < list.length; i++) {
      l.add(list[i]);
    }

    F.Fun<Integer, Integer> fun = new F.Fun<Integer, Integer>() {
      @Override
      public Integer call(Integer t) {
        return t + 1000;
      }
    };
    Collection<Integer> l2 = F.map(fun, l);

    System.out.println(l);
    System.out.println(l2);

    int[] ans = { 1001, 1002, 1003, 1004, 1054 };
    Iterator<Integer> j = l2.iterator();
    for (int i = 0; i < ans.length; i++) {
      assertEquals(ans[i], j.next().intValue());
    }
  }

  public void testFilter() {
    ArrayList<Integer> l = new ArrayList<Integer>();
    int[] list = { 1, 2, 3, 4, 54, 6, 7, 8, 9, 5, 32, 6, 2, 563, 64 };
    for (int i = 0; i < list.length; i++) {
      l.add(list[i]);
    }

    F.Fun<Integer, Boolean> fun = new F.Fun<Integer, Boolean>() {
      @Override
      public Boolean call(Integer t) {
        return t < 5;
      }
    };
    Collection<Integer> l2 = F.filter(fun, l);

    System.out.println(l);
    System.out.println(l2);

    int[] ans = { 1, 2, 3, 4, 2 };
    Iterator<Integer> j = l2.iterator();
    for (int i = 0; i < ans.length; i++) {
      assertEquals(ans[i], j.next().intValue());
    }
  }

  public void testFilter2() {
    ArrayList<Integer> l = new ArrayList<Integer>();
    int[] list = { 1, 2, 3, 4, 54, 6, 7, 8, 9, 5, 32, 6, 2, 563, 64 };
    for (int i = 0; i < list.length; i++) {
      l.add(list[i]);
    }

    F.Fun<Integer, Boolean> fun = new F.Fun<Integer, Boolean>() {
      @Override
      public Boolean call(Integer t) {
        return t < 7;
      }
    };

    F.Fun<Integer, Integer> fun2 = new F.Fun<Integer, Integer>() {
      @Override
      public Integer call(Integer t) {
        return t + 1000;
      }
    };

    try {
      Collection<Integer> l2 = F.filter(fun, l);
      Collection<Integer> l3;

      l3 = F.map(fun2, l2, TreeSet.class);

      System.out.println(l);
      System.out.println(l2);
      System.out.println(l3);

      int[] ans = { 1001, 1002, 1003, 1004, 1005, 1006 };
      Iterator<Integer> j = l3.iterator();
      for (int i = 0; i < ans.length; i++) {
        assertEquals(ans[i], j.next().intValue());
      }
    } catch (InstantiationException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

  }
}
