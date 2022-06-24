package com.silence.DesignPattern.ChainOfResponsibility;

import java.util.Objects;

public abstract class BaseHandler implements Handler {
  private Handler handler;
  
  @Override
  public void setNext(Handler h){
    handler = h;
  }

  @Override
  public
  void handle(Request request) {
    if(canHandle(request)){
      doHandle(request);
    }else if(Objects.nonNull(handler))
      handler.handle(request);
  }

  protected
  abstract
  void doHandle(Request request);

  public
  abstract
  boolean canHandle(Request request);

}
