package com.silence.DesignPattern.ChainOfResponsibility;

/**
 * 责任链模式里的Hanlder接口
 */
public interface Handler {
  void setNext(Handler h);
  void handle(Request request);
}
