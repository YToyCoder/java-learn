package com.silence.DesignPattern.Singleton;

import java.util.Objects;

public class Singletons {
  
  // the most simple implementation of Singleton pattern
  public static class MSSingleton {
    private MSSingleton(){}
    private static MSSingleton singleton = new MSSingleton();
    public static MSSingleton getInstance() {
      return singleton;
    }
  }

  // lazy initialize way
  // not thread safe
  public static class LazySingleton {
    private LazySingleton(){}
    private static LazySingleton singleton;

    public static LazySingleton instance() {
      if(Objects.isNull(singleton)){
        singleton = new LazySingleton();
      }
      return singleton;
    }
  }


  // lazy && thread safe
  public static class LazyThreadSafeSingleton {
    private LazyThreadSafeSingleton() {}

    private static LazyThreadSafeSingleton singleton;

    public static synchronized LazyThreadSafeSingleton instance() {
      if(Objects.isNull(singleton)) {
        singleton = new LazyThreadSafeSingleton();
      }
      return singleton;
    }
  }

  // double-checking locking

  public static class DoubleCheckingLockSingleton {
    private DoubleCheckingLockSingleton () {}
    private static volatile DoubleCheckingLockSingleton singleton;

    /**
     * {@code 
     * thread1 -> t1
     * thread2 -> t2
     *        t1      t2
     *        ||      ||
     * 
     *        volatile(可见性， 保证一个线程修改以后其他线程能够看见修改内容)
     *   第一次判读singleton是否是空
     *      singleton is null
     * 
     *        ||      ||
     *        ||      
     *        ||  singleton not null -------------------++----++ 
     *        ||                                        ||    ||
     *        ||      ||                                \/    \/
     *        \/      \/                           return singleton
     *       synchronized
     *            ||
     * 
     *        t1(or t2)
     * 
     *            ||
     * 
     *         new Singleton
     * 
     *            ||
     *            ||
     * 
     *    t1(or t2) finished & return
     * 
     *            ||
     *            \/
     * 
     *        t2(or t1)
     * 
     *            ||
     * 
     *      do nothing & return 
     * 
     *            ||
     *            \/
     * }
     * 
     * @return
     */
    public static DoubleCheckingLockSingleton instance() {
      if(Objects.isNull(singleton)) {
        // 可见性 轻度锁
        synchronized (DoubleCheckingLockSingleton.class) {
          // 初始化时加锁 
          if(Objects.isNull(null)){
            singleton = new DoubleCheckingLockSingleton();
          }
        }
      }
      return singleton;
    }
  }

  public static class InnerClassRegisteSingleton {
    private InnerClassRegisteSingleton(){}
    static final class SingletonHolder{
      public static InnerClassRegisteSingleton singleton = new InnerClassRegisteSingleton();
    }

    public static InnerClassRegisteSingleton instance() {
      return SingletonHolder.singleton;
    }

  }

  public static enum EnumSingleton {
    Instance;
  }
}
