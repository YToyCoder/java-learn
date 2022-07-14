package com.silence.DesignPattern.Decorator;

public abstract class Decorator<T> implements Component<T> {
  private Component<T> wrappee;

  protected Decorator(Component<T> component) {
    this.wrappee = component;
  }

  protected Component<T> decorated(){
    return wrappee;
  }

  @Override
  public String execute() {
    return wrappee.execute();
  }
}
