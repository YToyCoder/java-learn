package com.silence.vmy;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class EvalTest {


  @Test
  public void evalTest(){
    assertEquals( 1 + 2 * (3 + 4), Eval.eval("1 + 2 * ( 3 + 4 )"));
    assertEquals( 1 + 2 * (3 + 4.0), Eval.eval("1 + 2 * ( 3 + 4.0 )"));
    assertEquals( 1 + 2 * (3 + 4) * (5) - 1, Eval.eval("1 + 2 * ( 3 + 4 ) * ( 5 )  - 1"));
  }
  @Test
  public void evalTest1(){
    assertEquals(1 + 2 * 3 * 4, Eval.eval("1 + 2 * 3 * 4"));
    assertEquals(1 + 2 / 3 * 4 * 7, Eval.eval("1 + 2 / 3 * 4 * 7"));
    assertEquals(1 + 14 / (3 + 4), Eval.eval("1 + 14 / (3 + 4)"));
  }

  @Test
  public void evalTest2(){
    assertEquals(1 + 2 * 3 * 4, Eval.eval("let a : Int = 1 + 2 * 3 * 4", AST.variableStoreTreeEvaluator()));
    assertEquals(1 + 2 / 3 * 4 * 7, Eval.eval("let b : Int = 1 + 2 / 3 * 4 * 7", AST.variableStoreTreeEvaluator()));
    assertEquals(1 + 14 / (3 + 4), Eval.eval("let c : Int = 1 + 14 / (3 + 4)", AST.variableStoreTreeEvaluator()));
  }
}
