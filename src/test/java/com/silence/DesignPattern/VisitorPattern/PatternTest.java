package com.silence.DesignPattern.VisitorPattern;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class PatternTest {

  @Test
  public void test() {
    var c = new Shape.Circle();
    var r = new Shape.Rectangle();
    r.addChild(c);
    final var visitor = new VisitorImpl();
    Shapes.walk(r, visitor);
    assertTrue("map should be 2 length", visitor.map.size() == 2);
    assertTrue(visitor.map.containsKey(Shape.Circle.class));
    assertTrue(visitor.map.containsKey(Shape.Rectangle.class));
  }
}
