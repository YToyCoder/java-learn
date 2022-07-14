package com.silence.DesignPattern.Decorator;

public class Plus<T> extends Decorator<T>{

  public Plus(Component<T> component) {
    super(component);
  }

  @Override
  public String execute(){
    return "+" + super.execute();
  }
}
