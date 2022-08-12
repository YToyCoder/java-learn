package com.silence.vmy;

public class Main {
  public static void main(String[] args) {
    Eval.repl();
  }
  /**
   * string -> tokenize() ->
   *
   * "int a = 3;" -> ["int", "a", "=", "3", ";"]
   *
   * -> lexical
   *
   * ["int", "a", "=", "3", ";"]
   *
     *                                            =
   *       /                                                \
   *    {type: "type", identifier : "a"}            expression () : {type: literal, value: 3}
   *                                                      {1 + 3  * ( 4 + 5 )}
   */
}
