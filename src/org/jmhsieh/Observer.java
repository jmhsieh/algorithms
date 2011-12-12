package org.jmhsieh;

public interface Observer<T, U> {
  public U observe(T t);

  public void error(T t);

  public void dispose();

}
