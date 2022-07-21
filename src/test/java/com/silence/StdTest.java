package com.silence;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
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

  @Test
  public void arrInit(){
    int[] a = {1, 2};
    assertArrayEquals(new int[]{1,2}, a);
    StdTest[] ss = { new StdTest(), new StdTest() };
    StdTest[] ss2 = new StdTest[]{new StdTest()};
  }

  @Test
  public void wrappedObjectAndPrimitive(){
    assertTrue(10 == Integer.valueOf(10));
    assertTrue(Integer.valueOf(10) == Integer.valueOf(10));
    assertFalse(new Integer(10) == new Integer(10));
    assertFalse(Integer.valueOf(130) == Integer.valueOf(130));
    assertTrue(130 == Integer.valueOf(130));
  }
}
