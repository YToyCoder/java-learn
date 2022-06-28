package com.silence.DesignPattern.VisitorPattern;

import java.util.HashMap;
import java.util.Map;

import com.silence.DesignPattern.VisitorPattern.Shape.Circle;
import com.silence.DesignPattern.VisitorPattern.Shape.Rectangle;

public class VisitorImpl implements Visitor{

  public Map<Class<?>, Shape> map = new HashMap<>();

  @Override
  public void visitCircle(Circle circle) {
    map.put(circle.getClass(), circle);
  }

  @Override
  public void visitRect(Rectangle rect) {
    map.put(rect.getClass(), rect);
  }
  
}
