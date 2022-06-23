package com.silence.genetic;

public class MethodReturnGeneticType {
  
  public static <T> Holder<T> getHolder(String type) {
    Object ans = null;
    switch(type) {
      case "string":
        ans = "1";
      return new SimpleHolder<>((T)ans);
      case "number":
        ans = Integer.valueOf(0);
      return new SimpleHolder((T)ans);
      default:
        return new SimpleHolder((T)ans);
    }
  }

  interface Holder<T> {
    T get();
  }
  
  static class SimpleHolder<T> implements Holder<T> {
    private T keeper;
    public SimpleHolder(T val){
      this.keeper = val;
    }
    
    public T get() {
      return keeper;
    }
  }
  
  public static void main(String[] args) { 
    Holder<String> a = getHolder("string");
    System.out.println(a.get());
  }
}
