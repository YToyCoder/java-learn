package com.silence.DesignPattern.ChainOfResponsibility;

import java.util.Objects;

public class Requests {
  private Requests(){}
  public static String GET_NAME = "get-name";
  public static String THROW_ERROR = "throw-error";

  public static class GetNameRequest implements Request {
    private String name = "";
    @Override
    public String type() {
      return GET_NAME;
    }

    public void setName(String name) {
      this.name = name;
    }

    public String getName() {
      return name;
    }
  }

  public static class ThrowRequest implements Request {

    @Override
    public String type() {
      return THROW_ERROR;
    }

  }

  public static GetNameRequest getNameRequest(){
    return new GetNameRequest();
  }

  public static ThrowRequest getThrowRequest() {
    return new ThrowRequest();
  }


  public static boolean isRequest(Request request, String type) {
    return Objects.equals(request.type(), type);
  }
}
