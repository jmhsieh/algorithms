package org.jmhsieh.sampling;

import java.util.List;

import org.jmhsieh.sampling.ReservoirSampler;
import org.jmhsieh.structs.Histogram;

import junit.framework.TestCase;


public class TestReservoirSampler extends TestCase {

  public void testReserviorSampler() {
    Histogram<Integer> h = new Histogram<Integer>();

    for (int i = 0; i < 10000; i++) {
      ReservoirSampler<Integer> rs = new ReservoirSampler<Integer>(10);

      for (int j = 0; j < 100; j++) {
        rs.onNext(j);
      }

      rs.onCompleted(true);
      List<Integer> l = rs.sample();

      for (Integer k : l) {
        h.increment(k);
      }

    }

    System.out.println(h);

  }

}
