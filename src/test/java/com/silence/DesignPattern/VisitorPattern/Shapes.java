package com.silence.DesignPattern.VisitorPattern;

import java.util.Objects;

public abstract class Shapes {

  public static void walk(Shape shape, Visitor visitor){
    if(Objects.isNull(shape) || Objects.isNull(visitor)) return ;
    shape.accept(visitor);
    final var children = shape.getChildren();
    if(Objects.nonNull(children) && !children.isEmpty()){
      children.forEach(each -> walk(each, visitor));
    }
  }
}
