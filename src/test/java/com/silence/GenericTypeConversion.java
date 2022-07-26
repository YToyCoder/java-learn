package com.silence;

import org.junit.Test;

public class GenericTypeConversion {
  static class Parent<T>{
  }

  static class Child<T> extends Parent<T>{
  }

  @Test
  public void conversionTest(){
    // Parent<Integer> p = new Child<T>();
    Parent<Integer> p;
    Child<Integer> c = new Child<>();
    p = new Child<>();
  }
}
