package com.silence;

import org.junit.Test;

public class TestInerClass {
  
  private static int aval = 0;
  private static MemberClass a = null;
  public static class InerTest {
    public void use(){
      aval = 2;
    }
  }

  public class MemberClass {
  }
  
  @Test
  public void test() {
    int a = 2;
    class LocalClass{
      public void method() {
        int b = a;
        System.out.println(String.format("in LocalClass varible b is %d", b));
      }
    }
    new LocalClass().method();
  }
}
