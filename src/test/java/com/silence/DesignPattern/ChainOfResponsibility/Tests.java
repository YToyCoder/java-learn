package com.silence.DesignPattern.ChainOfResponsibility;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

public class Tests {

  @Test
  public void test(){
    var handler = Handlers.getHandlers();
    Requests.GetNameRequest request = Requests.getNameRequest();
    handler.handle(request);
    assertEquals("", request.getName(), "settedName");
  }

  @Test
  public void test1() {
    var handler = Handlers.getHandlers();
    Requests.ThrowRequest request = Requests.getThrowRequest();
    assertThrows(ThrowErrorException.class, () -> handler.handle(request));
  }
}
