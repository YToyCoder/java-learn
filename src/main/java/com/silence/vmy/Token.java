package com.silence.vmy;

public class Token {
  final int tag;
  final String value;
  public Token(int _tag, String val){
    tag = _tag;
    value = val;
  }

  static final int INT_V = 0;
  static final int DOUBLE_V = 1;
  static final int Identifier = 2;
  static final int Assignment = 3;
  static final int NewLine = 4;
  static final int Declaration = 5;
  static final int Literal = 6;

  static final int BuiltinCall = 7;
}
