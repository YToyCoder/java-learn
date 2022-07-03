package com.silence;

import org.eclipse.collections.impl.list.primitive.IntInterval;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

import java.util.TreeMap;

public class TreeMapTest {

  @Test
  public void test(){
    TreeMap<Integer,Integer> tree = new TreeMap<>();
    for(var i : IntInterval.fromTo(1, 10).toArray()){
      tree.put(i, i);
    }

    final var twoToTwenty = tree.subMap(2, true, 10, true);
    assertEquals(2, (int)twoToTwenty.firstKey());
    assertEquals(10, (int)twoToTwenty.lastKey());
  }
}
