package com.silence.DesignPattern.ChainOfResponsibility;

import java.util.Objects;

public class Handlers {
  private Handlers() {}

  private static class GetNameHandler extends BaseHandler {

    @Override
    protected void doHandle(Request request) {
      ((Requests.GetNameRequest)request).setName("settedName");
    }

    @Override
    public boolean canHandle(Request request) {
      return Objects.equals(request.type(), Requests.GET_NAME);
    }
  }

  private static class NothingHandler extends BaseHandler {

    @Override
    protected void doHandle(Request request) {
      throw new RuntimeException("can't handle");
    }

    @Override
    public boolean canHandle(Request request) {
      return false;
    }
    
  }

  private static class ThrowErrorHandler extends BaseHandler {

    @Override
    protected
    void doHandle(Request request) {
      throw new ThrowErrorException();
    }

    @Override
    public
    boolean canHandle(Request request) {
      return Requests.isRequest(request, Requests.THROW_ERROR);
    }
  }

  public static BaseHandler getHandlers(){
    var nothing = new NothingHandler();
    var named = new GetNameHandler();
    nothing.setNext(named);
    named.setNext(new ThrowErrorHandler());
    return nothing;
  }

}
