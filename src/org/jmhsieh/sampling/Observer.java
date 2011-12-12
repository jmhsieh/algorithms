package org.jmhsieh.sampling;

public interface Observer<T> {
  public void onNext(T v);

  public void onError(Exception e);

  public void onCompleted(boolean done);
}
