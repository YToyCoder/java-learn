package com.silence.DesignPattern.Decorator;

public class PrintHello<T> implements Component<T>{

  @Override
  public String execute() {
    return "Hello";
  }
  
}
