package com.silence.vmy;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.IntStream;

final public class Identifiers {
  private Identifiers(){}
  public static final Set<String> builtinIdentifiers = new HashSet<>();
  // support binary operation
  public static  final Set<Character> operatorCharacters = new HashSet<>();
  // parenthesis : ( and )
  public static  final Set<Character> commonIdentifiers = new TreeSet<>();

  // variable name / function name
  public static  final Set<Character> identifiers = new TreeSet<>();
  public static final Set<String> builtinCall = new TreeSet<>();

  public static final String ADD = "+";
  public static final String SUB = "-";
  public static final String MULTI = "*";
  public static final String DIVIDE = "/";
  public static final String OpenParenthesis = "(";
  public static final String ClosingParenthesis   = ")";
  public static final String NewLine = "\r\n";
  public static final String Assignment = "=";
  public static final String Equals = "==";
  public static final String ConstDeclaration = "val";
  public static final String VarDeclaration = "let";
  public static final String Colon = ":";
  public static final String Print = "print";
  // to combine two string
  public static final String Concat = "++";

  public static final char Quote = '"';
  static{
    // set builtinOperators
    builtinIdentifiers.add(ADD);
    builtinIdentifiers.add(SUB);
    builtinIdentifiers.add(MULTI);
    builtinIdentifiers.add(DIVIDE);
    builtinIdentifiers.add(OpenParenthesis);
    builtinIdentifiers.add(ClosingParenthesis);
    builtinIdentifiers.add(Equals);
    builtinIdentifiers.add(Print);
    operatorCharacters.addAll(
        Set.of('+','-', '*','/',':','?','%','>','<','|','^','&','~')
    );
    commonIdentifiers.addAll(
        Set.of('(',')', '=', Quote)
    );
    // Alphabetic 字母 a -> z & A -> Z
    identifiers.addAll(
        //
        IntStream.range(0, 26)
            .mapToObj(el -> List.of(Character.valueOf((char) (el + 'a')), Character.valueOf((char)(el + 'A'))))
            .flatMap(ls -> ls.stream())
            .toList()
    );
    // _ 下划线
    identifiers.add('_');

    builtinCall.add(Print);
  }


}
