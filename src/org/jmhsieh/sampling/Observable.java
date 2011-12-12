package org.jmhsieh.sampling;

public interface Observable<T> {
  Disposable subscribe(Observer<T> o);
}
