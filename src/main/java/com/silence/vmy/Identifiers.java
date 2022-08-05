package com.silence.vmy;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

final public class Identifiers {
  private Identifiers(){}
  public static final Set<String> builtinIdentifiers = new HashSet<>();
  public static  final Set<Character> operatorCharacters = new HashSet<>();
  public static  final Set<Character> commonIdentifiers = new TreeSet<>();

  public static final String ADD = "+";
  public static final String SUB = "-";
  public static final String MULTI = "*";
  public static final String DIVIDE = "/";
  public static final String OpenParenthesis = "(";
  public static final String ClosingParenthesis   = ")";
  
  static{
    // set builtinOperators
    builtinIdentifiers.add(ADD);
    builtinIdentifiers.add(SUB);
    builtinIdentifiers.add(MULTI);
    builtinIdentifiers.add(DIVIDE);
    builtinIdentifiers.add(OpenParenthesis);
    builtinIdentifiers.add(ClosingParenthesis);
    operatorCharacters.addAll(
        Set.of('+','-', '*','/',':','?','%','>','<','|','^','&','~')
    );
    commonIdentifiers.addAll(
        Set.of('(',')')
    );
  }


}
