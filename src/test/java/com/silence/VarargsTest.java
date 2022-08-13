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

  public void var_args_test_2(Object ...params){
    System.out.println(params.length);
  }
  
  @Test
  public void test() {
    testVarargs();
    testVarargs("1", "2");
    testVarargs(new String[]{"123", "4567"});
    var_args_test_2(new Object[]{1, 2});
  }
}
