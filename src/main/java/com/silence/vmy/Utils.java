package com.silence.vmy;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.Arrays;

public class Utils {
  public static MethodHandle getStaticMethod(Class<?> refc, final String name, Class<?> ...types){
    MethodType type = MethodType.methodType(types[0]);
    type = type.appendParameterTypes(Arrays.copyOfRange(types, 1, types.length));
    MethodHandle lookupMethod = null;
    try {
      lookupMethod = lookup.findStatic(refc, name, type);
    } catch 
    (// NoSuchMethodException | IllegalAccessException | 
    Throwable e
    ) {
      return null;
    }
    return lookupMethod;
  }

  public static MethodHandle getOpsStaticMethod(final String name, Class<?> ...types){
    return getStaticMethod(Ops.class, name, types);
  }

  static final MethodHandles.Lookup lookup = MethodHandles.lookup();

  // a mark that represet can be called recursive
  // @see
  static interface Recursive{}

  static class RecursiveException extends RuntimeException{
    public RecursiveException(String msg){
      super(msg);
    }
  }

}
