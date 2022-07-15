package com.silence;

import static org.junit.Assert.assertThrows;

import java.util.Objects;

import org.junit.Test;

public class TernaryIfElseOperator {

  @Test
  public void test() {
    todo2(null, null);
  }

  static class A{
    public A(Object i, Object k){
    }
  }
  A todo(Object a, Object b){
    return Objects.isNull(a) ? new A(a, null) : new A(b.getClass(), b.toString());
  }

  A todo2(Object a, Object b){
    if(Objects.isNull(a)) return new A(a,null);
    return new A(b.getClass(), b.toString());
  }
}
