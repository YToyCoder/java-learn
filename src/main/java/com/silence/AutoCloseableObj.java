package com.silence;

public class AutoCloseableObj implements AutoCloseable{

  @Override
  public void close() throws Exception {
    System.out.println("auto closing ....");
  }
  
}
