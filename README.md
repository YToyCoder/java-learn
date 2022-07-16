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

*装饰器模式*

对于面向对象语言来说，当你想更改一个类型的对象的行为时可以通过继承来覆盖原有的方法。

但是继承可能引发几个严重的问题。

1. 

> reference: 

> https://refactoringguru.cn/design-patterns

> https://github.com/youlookwhat/DesignPattern

> [菜鸟教程](https://www.runoob.com/design-pattern/design-pattern-tutorial.html)

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

```java

@Test
public void test(){
  System.out.println("start creating reference ...");
  WeakReference<Object> weakReference = new WeakReference<Object>(new Object());
  Object strongReference = new Object();
  System.out.println("ending creating reference");
  System.out.println(String.format("weak-reference is null ? %b", Objects.isNull(weakReference.get())));
  System.out.println(String.format("strong-reference is null ? %b", Objects.isNull(strongReference)));
  System.out.println("starting call gc");
  System.gc();
  try {
    Thread.sleep(100);
  } catch (InterruptedException e) {
    log.error("sleep error", e);
  }
  System.out.println("gc finish");
  System.out.println(String.format("weak-reference is null ? %b", Objects.isNull(weakReference.get())));
  System.out.println(String.format("strong-reference is null ? %b", Objects.isNull(strongReference)));
}

```

*运行打印结果*:

> start creating reference ...

> ending creating reference

> weak-reference is null ? false

> strong-reference is null ? false

> starting call gc

> gc finish

> weak-reference is null ? true

> strong-reference is null ? false

**WeakHashMap**

基于哈希表的Map接口的实现，具有弱键 。 WeakHashMap的条目在其密钥不再正常使用时将自动删除。 更确切地说，给定密钥的映射的存在不会阻止密钥被垃圾收集器丢弃，即，可以最终化，最终化，然后回收。 当一个键被丢弃时，它的条目将被有效地从地图中删除，因此该类的行为与其他Map实现略有不同。

WeakHashMap的弱键特性利用了`WeakReference`的特性，`WeakHashMap`的`Entry`的实现是继承了`WeakReference`的。WeakHashMap的Entry的弱引用是`K`，也就是HashMap里面键值对的键。因此如果WeakHashMap里面的某一个`K`不存在任何强引用时对应的K就会被回收.

*那么WeakHashMap是如何实现除了key以外整个Entry对象从HashMap删除的呢?*

在WeakHashMap里有一个函数叫`expungeStaleEntries`

```java
// WeakHashMap#expungeStaleEntries
/**
 * Expunges stale entries from the table.
 */
private void expungeStaleEntries() {
  for (Object x; (x = queue.poll()) != null; ) {
    synchronized (queue) {
      @SuppressWarnings("unchecked")
        Entry<K,V> e = (Entry<K,V>) x;
      int i = indexFor(e.hash, table.length);

      Entry<K,V> prev = table[i];
      Entry<K,V> p = prev;
      while (p != null) {
        Entry<K,V> next = p.next;
        if (p == e) {
          if (prev == e)
            table[i] = next;
          else
            prev.next = next;
          // Must not null out e.next;
          // stale entries may be in use by a HashIterator
          e.value = null; // Help GC
          size--;
          break;
        }
        prev = p;
        p = next;
      }
    }
  }
}
```

该函数会去遍历一个`ReferenceQueue`检查所有被回收的`WeakReferece`也就是被回收Key对应的Entry，然后将该Entry回收。
而该函数会在调用`WeakHashMap`的总体信息的函数时被调用，如: `size`,`getTable`。

```java
/**
 * Returns the table after first expunging stale entries.
 */
private Entry<K,V>[] getTable() {
  expungeStaleEntries();
  return table;
}

/**
  * Returns the number of key-value mappings in this map.
  * This result is a snapshot, and may not reflect unprocessed
  * entries that will be removed before next attempted access
  * because they are no longer referenced.
  */
public int size() {
  if (size == 0)
      return 0;
  expungeStaleEntries();
  return size;
}
```


- 虚引用也称为"幽灵引用"或者"幻影引用"，它是一个最弱的一种引用关系。一个对象是否存在虚引用完全不会对其生存时间构成影响，也无法通过虚引用来获取一个对象实例。为一个对象设置虚引用关联的唯一目的只是为了能在这个对象被回收到时收到一个系统通知。


> reference :

> 《深入理解java虚拟机》周志明

### 5 AutoCloseable

java 1.7 之后实现了AutoCloseable的对象可以通过try - with - resources 语法实现自动关闭资源

例如:

```java
// AutoCloseableObj.java
public class AutoCloseableObj implements AutoCloseable{

  @Override
  public void close() throws Exception {
    System.out.println("auto closing ....");
  }
  
}

// AutoCloseTest
public class AutoCloseTest {

  @Test
  public void test() {
    try (var obj = new AutoCloseableObj()){
    }catch(Exception e){
    }
  }
}

/**
 * 最后会打印: auto closing ....
 */

```

### 6 ThreadLocal

ThreadLocal用于实现线程之间的变量隔离，ThreadLocal提供的线程局部变量与普通的线程局部变量之间的区别是，ThreadLocal提供的线程局部变量的作用域是整个线程，而普通的线程局部变量的作用域是方法。

ThreadLocal通过`ThreadLocal.ThreadLocalMap`来保存变量，对于每一个线程都会有一个成员变量叫`threadLocals`存储的就是`ThreadLocal.ThreadLocalMap`变量。

ThreadLocal的#get方法获取变量实际上就是获取`Thread.threadLocals`的变量。

### ThreadLocal.ThreadLocalMap

ThreadLocal.ThreadLocalMap实际上是通过hash表来存储局部线程局部变量的，哈希表的节点是一个继承自`WeakReference`的键值对，该键值对以ThreadLocal提供的hash值为做为键来做hash索引。
该hash值是一个`AtomicInteger`类型的不断递增的值，因此对于每一个ThreadLocal提供的hash值对于所有线程是唯一的。该特性保证了每当创建一个ThreadLocal当前线程只会有唯一的hash值与之对应。

ThreadLocal#get方法最终都会去查找当前线程的threadLocals的数据，因此，每个线程创建的Threadlocal的值都只会属于当前线程。

### 7 enum (枚举)

[reference](https://blog.csdn.net/jisuanji12306/article/details/79356558)

```java

public class EnumTest {

  @Test
  public void test() {
    final int a = 100,b = 13;
    for(IntOp op : IntOp.values()){
      System.out.println(String.format("op-%s-(%d,%d)-%d", op, a, b, op.apply(a, b)));
    }
    // op-ADD-(100,13)-113
    // op-DEVIDE-(100,13)-7
  }



  static enum IntOp {
    ADD {
      @Override
      public int apply(int a, int b) {
        return a + b;
      }
    },
    DEVIDE {

      @Override
      public int apply(int a, int b) {
        return a / b;
      }
    };
    public abstract int apply(int a, int b);
  }
}
```

### 8 for-in


for-in 循环语句是Java 1.5的新特征之一，在遍历数组、集合方面，for-in为开发者提供了极大的方便。for-in 循环语句是 for 语句的特殊简化版本，主要用于执行遍历功能的循环。

语法格式:

`for (type var : collection) {`

`  block;`

`}`

for-in适用于数组和任何**Collection**对象。(`for-in`语句适用于数组或者其它任何**Iterable**)

*参考: onJava*

### 9 Collections.shuffle

打乱列表元素

```java

List<Integer> list = Stream.generate(new Supplier<Integer>() {
  Random random = new Random();
  @Override
  public Integer get() {
    return random.nextInt();
  }
})
.limit(10)
.collect(Collectors.toList());
list.forEach(el -> System.out.printf("%d ", el));
System.out.println();
Collections.shuffle(list);
list.forEach(el -> System.out.printf("%d ", el));
System.out.println();
// 944377481 -720786430 702573091 1299273027 885662345 1287353883 382365354 -1409717507 -1455871407 -1702050819
// 702573091 1299273027 -720786430 -1409717507 382365354 885662345 1287353883 944377481 -1702050819 -1455871407

```

### 10 Java几种类

**内部类**定义在一个类的内部的类，内部类分为:

| 类型            | 中文名称 |
| --------------- | -------- |
| static class    | 静态类   |
| member  class   | 成员     |
| local class     | 局部类   |
| anonymous class | 匿名类   |

静态类(static class):
不用创建外部类的对象就可以直接创建static class的对象。静态类可以访问外部类的静态成员，但是无法访问外部类的非静态成员。
成员类(member class):
只有创建了外部类才可以创建成员类。
局部类(local class):
定义在函数内部的类，局部类的函数只能引用final修饰的或功能类似于final的变量。
匿名类(anonymous class):
