package org.jmhsieh.stress;

import junit.framework.TestCase;

public class TestThreadLimit extends TestCase {

  class RingThread extends Thread {
    int max;
    int count;
    RingThread next;

    RingThread(int max, RingThread next) {
      this.max = max;
      this.count = 0;
      this.next = next;
    }

    public void run() {

    }
  }

  public void testThreadLimit() {
    // goal here is to do a token right with n threads, where n is 10, 100, 1000

  }

}
