package com.silence;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.junit.Test;

public class AllVersion {

  @Test
  public void collectionShuffleTest(){
    // Stream<Integer>.generate(new Supplier<Integer>() {
    //   // Random random = new Random(System.currentTimeMillis());
    //   @Override
    //   public Integer get() {
    //     return 0;
    //   }
    // });
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
  }

  @Test
  public void intTest(){
    System.out.println(Integer.toBinaryString(0));
    int b = 1 +add();
  }

  static int add(){
    return 4;
  }

  @Test
  public void primitive_test(){
    int_param(1);
    // int_param(Integer.valueOf(1));
  }


  private void int_param(int i){
    System.out.println("int");
  }

  private void int_param(Integer i){
    System.out.println("Integer");
  }

  private void primitive_value(){
    byte a = 1 + 1;
    int b = 129;
    byte c = 127;
    byte k = (byte)(a + c);
  }

  @Test
  public void static_code_run_order(){
    new Exts("");
  }


  static class Base {
    static {
      System.out.println("base");
    }

    public Base(){
      System.out.println("base-constructor");
    }

    public Base(String _String){
      super();
      System.out.println("base-constructor");
    }
  }

  static class Exts extends Base {
    static {
      System.out.println("Exts");
    }

    public Exts() {
      System.out.println("Exts-constructor");
    }

    public Exts(String _String){
      super();
      System.out.println("Exts-constructor");
    }
  }

  @Test
  public void list_test(){
    List<Integer> lt = new ArrayList<>(
      IntStream
      .range(0, 10)
      .mapToObj(Integer::valueOf)
      .toList()
    );
    var iter = lt.iterator();
    while(iter.hasNext()){
      Integer el = iter.next();
      if(el % 3 == 0){
        iter.remove();
        // lt.remove(el) 将会报错,由于modCount
      }
    }
  }

  /**
   * 测试通过反射获取构造函数
   */
  @Test
  public void constructors() {
    Class<Constructors> constructors_class = Constructors.class;
    try {
      Constructor<Constructors> constructor =  constructors_class.getConstructor();
      constructor.newInstance();
    } catch (
        NoSuchMethodException |
        InvocationTargetException |
        InstantiationException |
        IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  private static class Constructors {
    public Constructors(){
      System.out.println("No args constructor");
    }

    public Constructors(String str){
      System.out.println("str arg constructor");
    }
  }
}
