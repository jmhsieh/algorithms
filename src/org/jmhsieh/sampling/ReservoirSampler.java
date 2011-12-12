package org.jmhsieh.sampling;

import java.util.ArrayList;
import java.util.List;

/**
 * Reservoir sampling is a method for getting a uniform random sampling of n
 * data elements from a stream of N data elements without a priori knowing the
 * total number of elements N.
 * 
 * This is implemented as a StreamObserver (as opposed to an iterator).
 */
public class ReservoirSampler<T> implements Observer<T> {

  int samples; // number of elements to sample.
  List<T> candidates;
  int count = 0;
  boolean done = false;

  ReservoirSampler(int samples) {
    this.samples = samples;
    this.candidates = new ArrayList<T>(samples);
  }

  @Override
  public void onCompleted(boolean b) {
    done = b;
  }

  @Override
  public void onError(Exception e) {
    done = true;
  }

  @Override
  public void onNext(T v) {
    if (done)
      return;

    if (candidates.size() < samples) {
      // for the first n elements.
      candidates.add(v);
      count++;
      return;
    }

    // do reservoir sampling.
    count++;
    int replace = (int) Math.floor((double) count * Math.random());
    if (replace < samples) {
      // probability says replace.
      candidates.set(replace, v);
    }
    // else keep the current sample reservoir

    return;

  }

  List<T> sample() {
    return new ArrayList<T>(candidates);
  }
}
