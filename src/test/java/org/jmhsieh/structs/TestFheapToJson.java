package org.jmhsieh.structs;

import org.jmhsieh.structs.FHeapToJson;
import org.jmhsieh.structs.FibonacciHeap;

import junit.framework.TestCase;

public class TestFheapToJson extends TestCase {

  public void testJson() {
    FibonacciHeap<Integer> fh = new FibonacciHeap<Integer>();

    fh.insert(30);
    fh.insert(20);
    fh.insert(10);
    fh.insert(40);

    System.out.println(FHeapToJson.toJson(fh));

    FibonacciHeap<Integer> fh2 = new FibonacciHeap<Integer>();
    fh2.insert(35);
    fh2.insert(5);
    fh2.insert(15);
    fh2.insert(25);
    System.out.println(FHeapToJson.toJson(fh2));

    fh.union(fh2);
    System.out.println(FHeapToJson.toJson(fh));

    int[] ans = { 5, 10, 15, 20, 25, 30, 35, 40 };
    for (int j = 0; j < ans.length; j++) {
      int i = fh.deleteMin();
      System.out.println("Output: " + i);
      System.out.println(FHeapToJson.toJson(fh));
      assertEquals(ans[j], i);
    }

  }
}
