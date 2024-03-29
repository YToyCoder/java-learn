package com.silence.vmy;

import java.lang.invoke.MethodHandle;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public enum BinaryOps {
  ADD{

    @Override
    public Object apply(Object obj1, Object obj2) {
      return shortStrategyApply(obj1, obj2, "add");
    }

  },
  SUB{

    @Override
    public Object apply(Object obj1, Object obj2) {
      return shortStrategyApply(obj1, obj2, "sub");
    }

  },
  MULTI {
    @Override
    public Object apply(Object obj1, Object obj2) {
      return shortStrategyApply(obj1, obj2, "multi");
    }
  },
  DIVIDE {

    @Override
    public Object apply(Object obj1, Object obj2) {
      return shortStrategyApply(obj1, obj2, "divide");
    }

  },
  Concat {
    @Override
    public Object apply(Object obj1, Object obj2) {
      return obj1.toString() + obj2.toString();
    }
  },
  GT{ /* > */
    @Override
    public Object apply(Object obj1, Object obj2) {
      return shortStrategyApply(obj1, obj2, "gt");
    }
  },
  LT{/* < */
    @Override
    public Object apply(Object obj1, Object obj2) {
      return shortStrategyApply(obj1, obj2, "lt");
    }
  },
  EQ{/* == */
    @Override
    public Object apply(Object obj1, Object obj2) {
      return Objects.equals(obj1, obj2);
    }
  },
  NEQ {
    @Override
    public Object apply(Object obj1, Object obj2) {
      return !Objects.equals(obj1, obj2);
    }
  }
  ;

  static final Map<String, BinaryOps> OpsMapper = new HashMap<>();

  static {
    OpsMapper.putAll(
      Map.of(
        Identifiers.ADD, BinaryOps.ADD,
        Identifiers.SUB, BinaryOps.SUB,
        Identifiers.MULTI, BinaryOps.MULTI,
        Identifiers.DIVIDE, BinaryOps.DIVIDE,
        Identifiers.Concat, BinaryOps.Concat,
          "<",BinaryOps.LT,
          "==",BinaryOps.EQ,
          ">", BinaryOps.GT,
          "!=",BinaryOps.NEQ
      )
    );
  }

  static Object shortStrategyApply(Object p1, Object p2, String name){

    if(p1 instanceof Number n1 && p2 instanceof Number n2){
      if(n1 instanceof Integer && n2 instanceof Integer ){
        return invoke(p1, p2, name, int.class);
      }else if(n1 instanceof Double  && n2 instanceof Double ){
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

  /**
   * call this class static function
   * @param p1
   * @param p2
   * @param name
   * @param type
   * @return
   */
  static Object invoke( Object p1, Object p2, String name, Class<?> type){
    
    MethodHandle method = switch (name){
      case "gt", "eq", "lt" -> Utils.getOpsStaticMethod(name, boolean.class, type, type);
      default -> Utils.getOpsStaticMethod(name, type, type, type);
    };

    try {
      return Objects.isNull(method) ? null : method.invoke(p1, p2);
    } catch (Throwable e) {
      e.printStackTrace();
      return null;
    }

  }
  
  public abstract Object apply(Object obj1, Object obj2);

  static boolean lt(int a, int b){
    return a < b;
  }

  static boolean lt(double a, double b){
    return a < b;
  }

  static boolean eq(int a, int b){
    return a == b;
  }

  static boolean eq(double a, double b){
    return a == b;
  }
  static boolean gt(int a, int b){
    return a > b;
  }

  static boolean gt(double a, double b){
    return a > b;
  }

  static int divide(int a, int b){
    return a / b;
  }

  static double divide(double a, double b){
    return a / b;
  }

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
