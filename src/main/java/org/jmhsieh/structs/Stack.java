package org.jmhsieh.structs;

import java.util.LinkedList;

public class Stack<T> {
  LinkedList<T> stk = new LinkedList<T>();
  
  public T peek() {        
    return stk.get(0);
  }
  
  public void push(T item) {
    stk.add(0, item);
  }
  
  public T pop() {    
    return stk.remove();
  }
  
  public boolean isEmpty() {
    return stk.isEmpty();
  }
  
}
