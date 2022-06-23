package com.silence;

import org.junit.Test;

public class VarargsTest {
  public void testVarargs(String ...args) {
    if(args.length == 0) {
      System.out.println("args is empty");
    }else {
      System.out.printf("args length is %d\n", args.length);
    }
  }
  
  @Test
  public void test() {
    testVarargs();
    testVarargs("1", "2");
  }
}
