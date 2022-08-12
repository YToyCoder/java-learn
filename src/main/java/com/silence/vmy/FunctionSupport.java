package com.silence.vmy;

import java.util.List;

/**
 * all builtin call can get here
 */
public class FunctionSupport {
  private FunctionSupport(){}

  // call a function
  public static Object call(String name, List<Object> params){
    return "has not implement";
  }

  public static final int Builtin = 0;
  public static final int UserDefined = 1;

  public interface FunctionType extends VmyType{
    List<VmyType> types();
    VmyType param_type(int i);
    int tag();
  }

  public static interface FunctionRegister {
  }
}
