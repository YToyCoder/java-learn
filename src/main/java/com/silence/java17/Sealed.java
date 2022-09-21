package com.silence.java17;

/**
 * java密封类:
 * <p>1. 用于限制继承关系, 限制能够继承的子类</p>
 * <p>2. 用于支持将来的模式匹配</p>
 * 
 * <p>只有permits声明的类才能够继承</p>
 */
public class Sealed {

  public static abstract sealed class Shape permits Circle, Rectangle {
  }

  public static final class Circle extends Shape{
  }

  public static non-sealed class Rectangle extends Shape {
  }

}
