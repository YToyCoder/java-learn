package com.silence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.silence.java16.Record;;

public class Java16Test {

  @Test
  public void testRecord(){
    var rcd = new Record(10, "name");
    assertEquals(10, rcd.length());
    assertEquals("name", rcd.name());
  }

  @Test
  public void testDeconstructingRecord(){
    Object rcd = new Record(10, "name");
    assertTrue(rcd instanceof Record r);
  }
}
