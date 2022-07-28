package com.silence.stream;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.junit.Test;

public class MethodTest {

  @Test
  public void reduce() {

    int sumOfZero2nine = IntStream.range(0, 10)
    .reduce((a , b) -> a + b)
    .getAsInt();
    IntStream.range(0, 10)
    .forEach(System.out::println);

    List<Integer> zero2nine = Stream.generate(() -> 1)
    .limit(10)
    .reduce(
      new ArrayList<Integer>(), 
      (arr, el) -> {
        arr.add(arr.size());
        return arr;
      }, 
      (arr1, arr2) -> { 
      arr1.addAll(arr2);
      return arr1;
      }
    );

  }

}
