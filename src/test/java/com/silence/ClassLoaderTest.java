package com.silence;

import java.util.HashMap;
import java.util.Objects;

import org.junit.Test;

public class ClassLoaderTest {

  @Test
  public void test(){
    final var hashMapClassLoader = HashMap.class.getClassLoader();
    if(hashMapClassLoader == null)
      // will be Bootstrap class loader 
      System.out.println("it's bootstrap class loader");
    else if(Objects.equals(hashMapClassLoader, ClassLoaderTest.class.getClassLoader())){
      System.out.println("it's Application class loader");
    }
  }
}
