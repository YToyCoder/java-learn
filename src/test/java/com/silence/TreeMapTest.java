package com.silence;

import org.eclipse.collections.impl.list.primitive.IntInterval;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

import java.util.TreeMap;

public class TreeMapTest {

  @Test
  public void test(){
    TreeMap<Integer,Integer> tree = new TreeMap<>();
    for(var i : IntInterval.fromToBy(1, 9, 2).toArray()){
      tree.put(i, i);
    }

    final var twoToTwenty = tree.subMap(2, true, 10, true);
    assertEquals(3, (int)twoToTwenty.firstKey());
    assertEquals(9, (int)twoToTwenty.lastEntry().getKey());

    final var arr = twoToTwenty.keySet().toArray(new Integer[0]);
    assertEquals(twoToTwenty.size(), arr.length);
    final var floor = tree.floorEntry(2);
    assertEquals(1, (int)floor.getKey());
  }

}


// ["RangeModule1","queryRange2","addRange3","removeRange4","queryRange5","addRange6","addRange7","removeRange8","removeRange9","addRange10","addRange11","removeRange12","queryRange13","addRange14","addRange15","addRange16","removeRange17","addRange18","addRange19",
// ["RangeModule","queryRange","addRange","removeRange","queryRange","addRange","addRange","removeRange","removeRange","addRange","addRange","removeRange","queryRange","addRange","addRange","addRange","removeRange","addRange","addRange","queryRange","removeRange","addRange",
// "queryRange","addRange","queryRange","queryRange","removeRange","queryRange","removeRange","addRange","queryRange","removeRange","addRange","removeRange","removeRange","addRange","removeRange","queryRange","removeRange","removeRange","removeRange","addRange","queryRange","addRange",
// "addRange","addRange","queryRange","removeRange","addRange","addRange","removeRange","removeRange","queryRange","removeRange","queryRange","queryRange","queryRange","removeRange","queryRange","addRange","queryRange","queryRange","addRange","queryRange","removeRange","removeRange",
// "addRange","addRange","addRange","addRange","queryRange","removeRange","addRange","removeRange","queryRange","queryRange","removeRange","removeRange","removeRange","addRange","removeRange","queryRange","queryRange","queryRange","removeRange","queryRange","removeRange","queryRange",
// "addRange","queryRange","queryRange"]
// [null,false,null,null,false,null,null,null,null,null,null,null,false,null,null,null,null,null,null,false,null,null,true,null,true,true,null,false,null,null,false,null,null,null,null,null,null,false,null,null,null,null,false,null,null,null,false,null,null,null,null,null,false,null,false,false,false,null,true,null,false,true,null,false,null,null,null,null,null,null,true,null,null,null,true,false,null,null,null,null,null,false,false,false,null,false,null,false,null,false,false]
// [null,false,null,null,false,null,null,null,null,null,null,null,false,null,null,null,null,null,null,false,null,null,true,null,true,true,null,false,null,null,false,null,null,null,null,null,null,false,null,null,null,null,false,null,null,null,false,null,null,null,null,null,false,null,false,false,false,null,false,null,false,true,null,false,null,null,null,null,null,null,true,null,null,null,true,false,null,null,null,null,null,false,false,false,null,false,null,false,null,false,false]
// [ 1  , 2   , 3   , 4 , 5   , 6 , 7   , 8  ,  9 , 10  , 11, 12  ,13   , 14,15  ,16  ,17  ,18  ,19  ,  20 ,  21, 22 , 23 , 24 ,  25, 26 , 27 , 28  , 29 ,30  , 31  , 32 , 33 , 34 , 35, 36  , 37 , 38  , 39 , 40 , 41 , 42 , 43  , 44 , 45 , 46 , 47  , 48 , 49 , 50 , 51 , 52 , 53  , 54 , 55  , 56  , 57  , 58 , 59  , ]