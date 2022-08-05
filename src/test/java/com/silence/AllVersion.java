package com.silence;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.Collectors;
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


}
