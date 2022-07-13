# java-lang-test

#### 介绍
java语言测试
使用junit测试


### 1 設計模式(design pattern)

三類設計模式:

- 创建型模式: 工厂模式、单例模式、抽象工厂模式、建造者模式、原型模式

- 结果型模式: 装饰器模式、适配器模式、代理模式、桥接模式、组合模式、外观模式、享元模式

- 行为型模式: 责任链模式、观察者模式、访问者模式、模板方法模式、命令模式、迭代器模式、中介者模式、备忘录模式、解释器模式、状态模式、策略模式


*singleton 6种实现*

```java

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
```

> reference: 

> https://refactoringguru.cn/design-patterns

> https://github.com/youlookwhat/DesignPattern

> [菜鸟模式](https://www.runoob.com/design-pattern/design-pattern-tutorial.html)

### 2 MethodHandle

java.lang.invoke 包提供一种动态调用的方法, 称为方法句柄(MethodHandle)。可以通过`java.lang.invoke.MethodHandles`的`lookup`创建一个`MethodHandles.Lookup`, 该类可以获取对应类的方法句柄。

例如：

```java
public class MethodHandleTest {
  public static void main(String[] args) {
    final MethodHandles.Lookup lookup = MethodHandles.lookup();
    final MethodHandle method = lookup.findStatic(MethodHandleTest.class, "function", MethodType.methodType(void.class));
    method.invoke();
  }

  public static void function(){
  }
}
```

`findStatic`用于生成静态方法的方法句柄. 该函数需要传入3个参数，第一个是要查找的类，第二个是要查找的方法的名称，第三个是要查找的方法的类型，该类型通过方法的返回值和参数确定。方法类型通过`java.lang.invoke.MethodType`类型确定,可以通过工厂方法`MethodType#methodType`创建，该方法第一个入参是函数的返回值，后面的可选参数是函数的入参。

与`findStatic`类似的方法还有:`findSpecial`,`findGetter`,`findSetter`,`findStaticGetter`,`findStaticSetter`和`findVirtual`等。

类似`**Getter`和`**Setter`是生成操作field的方法句柄，主要是获取对应field的进行设置或获取的操作，有点类似于JavaBean的Getter,Setter方法。

`findSpecial`是获取`final`或`private`修饰的方法.

`findVirtual`是获取虚方法的句柄，就是那些具有多态性质的函数。

*参考*:
《深入理解java虚拟机》周志明

### 3 record

Java中Record类型java14预览，java16开始正式支持.

> reference :

> [Data Oriented Programming in Java](https://www.infoq.com/articles/data-oriented-programming-java/#:~:text=Data%20oriented%20programming%20in%20Java%20Records%2C%20sealed%20classes%2C,and%20type-safe%20way%20of%20acting%20on%20polymorphic%20data.)

> [实战 Java 16 值类型](https://cloud.tencent.com/developer/article/1814757)

> [openjdk record](https://openjdk.org/jeps/395)


### 4 Java 引用

Java将引用分为`强引用`、`软引用`、`弱引用`、`虚引用`.

- 强引用是传统的引用的定义，是指传统的引用赋值方式, 如`A a = new A()`。无论任何情况下，只要强引用关系存在，垃圾收集器就永远不会回收掉被引用的对象.

- 软引用是用来描述一些还有用，但非必须的对象。只被软引用关联(软引用可达)的对象，当内存不足时垃圾回收器才会回收这些对象。
由于软引用可到达的对象比弱引用可达到的对象滞留内存时间会长一些，可以利用这个特性来做缓存。这样的话，就可以节省很多事情，垃圾回收器会关心当前哪种可到达类型以及内存的消耗程度来进行处理。

- 弱引用是用来描述那些非必要的对象，它的强度比软引用更弱一些。如果垃圾收集器在某个时间点上确定一个对象是若可达(只存在弱引用)，垃圾回收器就会回收该对象。

- 虚引用也称为"幽灵引用"或者"幻影引用"，它是一个最弱的一种引用关系。一个对象是否存在虚引用完全不会对其生存时间构成影响，也无法通过虚引用来获取一个对象实例。为一个对象设置虚引用关联的唯一目的只是为了能在这个对象被回收到时收到一个系统通知。

> reference :

> 《深入理解java虚拟机》周志明