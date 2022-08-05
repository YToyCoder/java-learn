package com.silence.vmy;

import java.lang.invoke.MethodHandle;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public enum Ops {
  ADD{

    @Override
    public Object apply(Object obj1, Object obj2) {
      return shortStrategeApply(obj1, obj2, "add");
    }

  },
  SUB{

    @Override
    public Object apply(Object obj1, Object obj2) {
      return shortStrategeApply(obj1, obj2, "sub");
    }

  },
  MULTI {
    @Override
    public Object apply(Object obj1, Object obj2) {
      return shortStrategeApply(obj1, obj2, "multi");
    }
  }
  ;

  static final Map<String, Ops> OpsMapper = new HashMap<>();

  static {
    OpsMapper.putAll(
      Map.of(
        Identifiers.ADD, Ops.ADD,
        Identifiers.SUB, Ops.SUB,
        Identifiers.MULTI, Ops.MULTI
      )
    );
  }

  static Object shortStrategeApply(Object p1, Object p2, String name){
    if(p1 instanceof Number n1 && p2 instanceof Number n2){
      if(n1 instanceof Integer i1 && n2 instanceof Integer i2){
        return invoke(p1, p2, name, int.class);
      }else if(n1 instanceof Double i1 && n2 instanceof Double i2){
        return invoke(p1, p2, name, double.class);
      }else{
        return invoke(asDouble(p1),asDouble(p2), name, double.class);
      }
    }else
      throw new OpsException("ops not support not Number type");
  }

  static double asDouble(Object obj){
    if(obj instanceof Integer p1int){
      return (double) p1int;
    }else if(obj instanceof Double p1double)
      return p1double;
    else 
      throw new OpsException("can't convert" + obj.getClass().getName() +" to double");
  }

  static Object invoke( Object p1, Object p2, String name, Class<?> type){
    MethodHandle method = Utils.getOpsStaticMethod(name, type, type, type);
    try {
      return Objects.isNull(method) ? null : method.invoke(p1, p2);
    } catch (Throwable e) {
      e.printStackTrace();
      return null;
    }
  }
  
  public abstract Object apply(Object obj1, Object obj2);

  static  int multi(int a, int b) {
    return a * b;
  }
  static  double multi(double a, double b){
    return a * b;
  }
  static int add(int a, int b){
    return a + b;
  }

  static int sub(int a, int b){
    return a - b;
  }

  static double add(double a, double b) {
    return a + b;
  }

  static double sub(double a, double b){
    return a - b;
  }
}
