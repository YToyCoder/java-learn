package com.silence.vmy;

import org.junit.Test;

public class EvalTest {
  

  @Test
  public void evalTest(){
    System.out.println( Eval.eval("1 + 2 * ( 3 + 4 )"));
    System.out.println( Eval.eval("1 + 2 * ( 3 + 4 ) * ( 5 )  - 1"));
  }
  @Test
  public void evalTest1(){
    System.out.println( Eval.eval("1 + 2 * 3 * 4"));
  }
}
