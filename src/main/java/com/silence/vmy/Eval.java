package com.silence.vmy;

import java.util.Objects;
import java.util.Scanner;

public class Eval {
  // eval an expression , like 1 + 2 * (3 + 4)
  public static Object eval(final String expression){
//    return EVALUATOR.eval( AST.build(Scanners.scan(expression)));
    return AST.defaultTreeEvaluator().eval(AST.build(Scanners.scanner(expression)));
  }

  private static final String notice = """
      Hell , welcome to vmy!
      version 0.1
      """;
  public static void repl(){
    System.out.println(notice);
    String input = "";
    Scanner scanner = new Scanner(System.in);
    while(!Objects.equals(input, "#")){
      System.out.print("> ");
      input = scanner.nextLine();
      if(input.trim().length() == 0 ) continue;
      if(Objects.equals(input, "#")) System.exit(0);
      try{
        System.out.println( eval(input) );
      }catch (Exception e){
        e.printStackTrace();
      }
    }
  }

//  private static AST.VmyTreeEvaluator EVALUATOR = new AST.VmyTreeEvaluator();


}
