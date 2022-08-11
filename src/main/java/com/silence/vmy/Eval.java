package com.silence.vmy;

public class Eval {
  // eval an expression , like 1 + 2 * (3 + 4)
  public static Object eval(final String expression){
//    return EVALUATOR.eval( AST.build(Scanners.scan(expression)));
    return AST.defaultTreeEvaluator().eval(AST.build(Scanners.scanner(expression)));
  }

//  private static AST.VmyTreeEvaluator EVALUATOR = new AST.VmyTreeEvaluator();


}
