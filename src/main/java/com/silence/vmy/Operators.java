package com.silence.vmy;

import java.util.HashSet;
import java.util.Set;

final public class Operators {
  private Operators(){}
  public static final Set<Character> buildinIdentifiers = new HashSet<>();

  public static final Character ADD = '+';
  public static final Character SUB = '-';
  public static final Character MULTI = '*';
  public static final Character DIVIDE = '/';
  public static final Character OpenParenthesis = '(';
  public static final Character ClosingParenthesis   = ')';
  
  static{
    // set buildinOperators
    buildinIdentifiers.add(ADD);
    buildinIdentifiers.add(SUB);
    buildinIdentifiers.add(MULTI);
    buildinIdentifiers.add(DIVIDE);
    buildinIdentifiers.add(OpenParenthesis);
    buildinIdentifiers.add(ClosingParenthesis);
  }


}
