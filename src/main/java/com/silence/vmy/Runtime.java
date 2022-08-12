package com.silence.vmy;

import java.util.Map;
import java.util.WeakHashMap;

public class Runtime {
  private Runtime(){}

  public static interface Variable {
    VmyType getType();
    Object getValue();
    void setValue(Object value);
    boolean mutable();
  }

  private static class DefaultOPool implements ObjPool {
    private Map<Long, Object> objectMapper = new WeakHashMap<>();

    @Override
    public void put(Long identity, Object obj) {
      if(objectMapper.containsKey(identity))
        throw new VmyRuntimeException("existing identity");
      objectMapper.put(identity, obj);
    }

    @Override
    public Object get(Long identity) {
      return objectMapper.get(identity);
    }

    @Override
    public boolean exists(Long identity) {
      return objectMapper.containsKey(identity);
    }
  }

  // create a pool to store objects
  public static ObjPool create_pool(){
    return new DefaultOPool();
  }

  private static ObjPool OBJPool = new DefaultOPool();

  /**
   *  put new Obj to pool
   * @param identity
   * @param obj
   */
  public static void put(Long identity, Object obj) {
    OBJPool.put(identity, obj);
  }

  /**
   * get obj from pool
   * @param identity
   * @return
   */
  public static Object get(Long identity) {
    return OBJPool.get(identity);
  }

  private static class DefaultVariableImpl implements Variable{

    private final VmyType type;
    private Object value;

    public DefaultVariableImpl(VmyType _Type){
      type = _Type;
    }

    @Override
    public VmyType getType() {
      return type;
    }

    @Override
    public Object getValue() {
      return value;
    }

    @Override
    public void setValue(Object _value) {
      value = _value;
    }

    @Override
    public boolean mutable() {
      return true;
    }
  }

  private static class ImmutableVariable extends DefaultVariableImpl {

    public ImmutableVariable(VmyType _Type) {
      super(_Type);
    }

    @Override
    public boolean mutable() {
      return false;
    }
  }

  public static interface WithName {
    String name();
  }

  public static interface VariableWithName extends Variable , WithName{}

  // create a new variable
  private static Variable create_variable(VmyType type, boolean mutable){
    return mutable ? new DefaultVariableImpl(type) : new ImmutableVariable(type);
  }

  public static Variable declare_variable(Frame frame, String name, VmyType type){
    return declare_variable(frame, name, type, true);
  }

  public static Variable declare_variable(Frame frame, String name, VmyType type, boolean mutable){
    Variable variable = create_variable(type, mutable);
    frame.put(name, variable, null);
    return variable;
  }

  public static Object get_value(String name, Frame frame){
    Variable variable = frame.local(name);
    if(Utils.isType(variable, VmyTypes.BuiltinType.Table)){
      return frame.get_obj((Long)variable.getValue());
    }else return variable.getValue();
  }

}
