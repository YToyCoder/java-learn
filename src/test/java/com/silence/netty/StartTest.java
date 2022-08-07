package com.silence.netty;

import org.junit.Test;

public class StartTest {
  
  @Test
  public void startTest(){
    final Start.HttpServerOne server = new Start.HttpServerOne(8080);
    try {
      server.start();
    } catch (InterruptedException e) {
      e.printStackTrace();
      System.out.println(e.getMessage());
      System.out.println("8".repeat(10));
    }
  }

  public static void main(String[] args) {
    new StartTest().startTest();
  }
}
