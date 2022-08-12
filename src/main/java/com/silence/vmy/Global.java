package com.silence.vmy;

import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

public class Global implements Frame {
  private Global(){}

  private static Global INSTANCE = new Global();

  public static Global getInstance() {
    return INSTANCE;
  }

  private Map<String, Object> primitives = new TreeMap<>();

  public void put(String _name, Object _value){
    if( Objects.isNull(_value) || _value instanceof Number || _value instanceof Boolean || _value instanceof Character){
      primitives.put(_name, _value);
    }
  }

  public boolean exists(String _name){
    return primitives.containsKey(_name);
  }

  public Object get(String _name){
    return primitives.get(_name);
  }

  @Override
  public Runtime.Variable local(String _name) {
    return null;
  }
}
