package com.silence.vmy;

import java.util.HashSet;
import java.util.Set;

final public class Operators {
  private Operators(){}
  public static final Set<Character> buildinIdentifiers = new HashSet<>();
  
  static{
    // set buildinOperators
    buildinIdentifiers.add('+');
    buildinIdentifiers.add('-');
    buildinIdentifiers.add('*');
    buildinIdentifiers.add('/');
    buildinIdentifiers.add('(');
    buildinIdentifiers.add(')');
  }


}
