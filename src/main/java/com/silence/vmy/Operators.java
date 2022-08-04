package com.silence.vmy;

import java.util.HashSet;
import java.util.Set;

final public class Operators {
  private Operators(){}
  public static final Set<String> buildinIdentifiers = new HashSet<>();

  public static final String ADD = "+";
  public static final String SUB = "-";
  public static final String MULTI = "*";
  public static final String DIVIDE = "/";
  public static final String OpenParenthesis = "(";
  public static final String ClosingParenthesis   = ")";
  
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
