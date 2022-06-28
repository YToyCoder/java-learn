package com.silence.stream;

import static org.junit.Assert.assertTrue;

import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;

public class LimitAndSkipTest {

  @Test
  public void test(){
    var res = Stream.generate(new Supplier<Integer>() {

      private int seed = 0;

      @Override
      public Integer get() {
        return seed++;
      }
    }).skip(10).limit(20).collect(Collectors.toList());
    assertTrue("res size is 20", res.size() == 20);
    assertTrue(" 10 is in res index 0", res.get(0) == 10);
    assertTrue(" 29 is in res index 19", res.get(19) == 29);
  }
}
