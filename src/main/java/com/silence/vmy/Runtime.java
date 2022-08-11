package com.silence.vmy;

import java.util.Map;
import java.util.WeakHashMap;

public class Runtime {
  private Runtime(){}

  public static interface Variable {
    VmyType getType();
    Object getValue();
  }

  static class DefaultOPool implements ObjPool {
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
}
