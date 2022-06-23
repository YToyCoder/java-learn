package com.silence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.TreeMap;

import org.junit.Test;

/**
 * standard lib test
 */
public class StdTest {

  @Test
  public void treeMap(){
    TreeMap<Integer, Integer> treeMap = new TreeMap<>();
    for(int i=0; i<10; i++) {
      if(i % 2 == 0)
        treeMap.put(i, i);
    }
    var ceil = treeMap.ceilingEntry(5);
    assertEquals("tree Map ceil", (int)ceil.getValue(), 6);

    var floor = treeMap.floorEntry(5);
    assertEquals("tree Map floor", 4, (int)floor.getValue());


    var tailOf5 = treeMap.tailMap(5);
    tailOf5.forEach((value, key) -> {
      assertTrue(String.format("value is %d", value), value >= 5);
    });
  }
}
