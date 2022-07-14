package com.silence.DesignPattern.Decorator;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class DTest {

  @Test
  public void test1() {
    Component<String> c = new Plus<>(new PrintHello<>());
    assertEquals("+Hello", c.execute());
  }
}
