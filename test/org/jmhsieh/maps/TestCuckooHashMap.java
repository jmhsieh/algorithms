package org.jmhsieh.maps;

import org.junit.Test;

public class TestCuckooHashMap {

  @Test
  public void testWikipediaExample() {
    int values[] = { 20, 50, 53, 75, 100, 67, 105, 3, 36, 39, 6};
    CuckooHashMap<Integer, Integer> map = new CuckooHashMap<Integer, Integer>(
        11);

    for (int i = 0; i < values.length; i++) {
      Integer j = values[i];
      try {
        map.put(j, j);
      } finally {
        debugDump(map);
        System.out.println("====");
      }
    }
  }

  public static <K, V> void debugDump(CuckooHashMap<K, V> chm) {
    for (int i = 0; i < chm.vals1.length; i++) {
      System.out.printf("%4d\t%s\t%s\n", i, chm.vals1[i], chm.vals2[i]);
    }

  }

}
