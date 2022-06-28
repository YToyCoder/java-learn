package com.silence.DesignPattern.VisitorPattern;

import java.util.ArrayList;
import java.util.List;

public interface Shape {

  void accept(Visitor visitor);
  void addChild(Shape shape);
  List<Shape> getChildren();

  public abstract class AbstractShape implements Shape {

    private final List<Shape> children;

    public AbstractShape(List<Shape> shape){
      this.children = shape;
    }

    public AbstractShape() {
      this(new ArrayList<>());
    }

    @Override
    public void addChild(Shape shape){
      children.add(shape);
    }

    @Override
    public List<Shape> getChildren(){
      return children;
    }
  }

  public static class Circle extends AbstractShape {

    @Override
    public void accept(Visitor visitor) {
      visitor.visitCircle(this);
    }}

  public static class Rectangle extends AbstractShape {

    @Override
    public void accept(Visitor visitor) {
      visitor.visitRect(this);
    }}

}
