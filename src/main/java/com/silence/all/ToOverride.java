package com.silence.all;

public class ToOverride {
  public static class Base {
    public String get(){
      return "";
    }
  }

  public static class Sub extends Base {
    @Override
    public String get(){
      return null;
    }
  }
}
