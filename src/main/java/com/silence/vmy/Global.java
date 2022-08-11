package com.silence.vmy;

import java.util.Map;
import java.util.TreeMap;

public class Global {
  private Global(){}

  private static Global INSTANCE = new Global();

  public static Global getInstance() {
    return INSTANCE;
  }

  private Map<String, Object> primitives = new TreeMap<>();

  public void put(String _name, Object _value){
    if(_value instanceof Number || _value instanceof Boolean || _value instanceof Character){
      primitives.put(_name, _value);
    }
  }

  public boolean exists(String _name){
    return primitives.containsKey(_name);
  }

  public Object get(String _name){
    return primitives.get(_name);
  }
}