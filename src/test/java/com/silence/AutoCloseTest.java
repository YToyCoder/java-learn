package com.silence;

import java.util.function.Supplier;

import org.junit.Test;

public class AutoCloseTest {

  @Test
  public void test() {
    try (var obj = new AutoCloseableObj()){
    }catch(Exception e){
    }
  }

  @Test
  public void try_with_resources(){
    try(TryClose close = new TryClose()){
      close.throwEception();
    }catch(Exception e){
      System.out.println("exception in catch : " + e.getClass().getSimpleName());
    }
  }


  @Test
  public void try_with_resources_and_return(){
    Supplier<String> s = () -> {
      try(TryClose tryClose = new TryClose()){
        tryClose.throwEception();
        return "hello";
      }catch(Exception e){
        System.out.println("exception in catch : " + e.getClass().getSimpleName());
        throw new RuntimeException(e);
      }
    };
    try{
      System.out.println(s.get()); 
    }catch(Exception e){
      e.printStackTrace();
    }
  }

  static class TryClose implements AutoCloseable{

    public void throwEception(){
      throw new RuntimeException();
    }

    @Override
    public void close() throws Exception {
      System.out.println("closed");
    }
  }
}
