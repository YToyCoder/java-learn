package com.silence.reflect;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class TestCreateParalizedArray {
  @Test
  public void test1() {
    List<String>[] sl = (List<String>[]) Array.newInstance(List.class, 10);
    sl[0] = new ArrayList<>();
    sl[0].add("first");
  }
}
