package com.silence;

import java.lang.ref.WeakReference;
import java.time.LocalDateTime;
import java.util.Objects;

public class ReferenceTest {
  private Integer strong;
  private WeakReference<Integer> weak;
  private static ReferenceTest selfSave;

  public ReferenceTest(){
  }

  public ReferenceTest(Integer strong, WeakReference<Integer> weak){
    this.strong = strong;
    this.weak = weak;
  }

  public void test(){
    System.out.println("before exit code block");
    System.out.println("before gc");
    if(Objects.nonNull(weak)) 
      System.out.println(
        weak.get()
      );
    System.out.println(strong);
    System.gc();
    System.out.println("after gc");
  }

  public static void main(String[] args) {
    var ref = new ReferenceTest();
    {
      Integer b = 10;
      Integer c = 20;
      WeakReference<Integer> a = new WeakReference<Integer>(c);
      c = null;
      ref.strong = b;
      ref.weak = a;
    }
    ref.test();
    System.out.println("before exit code block");
    System.out.println("before gc");
    System.gc();
    System.out.println("after gc");
    ref.test();
  }

  public static void run(int time,ReferenceTest run){
    var start = LocalDateTime.now();
    var end = start.plusMinutes(time);
    while(LocalDateTime.now().isBefore(end)){
      run.test();
    }
  }
}
