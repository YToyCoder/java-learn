package com.silence.reflect;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Method;
import java.util.function.Function;

import org.junit.Test;

public class ClassFunctionTest {

  @Test
  public void typeCheck(){
    assertTrue("int is primitive", int.class.isPrimitive());
    assertFalse("Integer is not primitive", Integer.class.isPrimitive());
    assertFalse("int is not Array ", int.class.isArray());
    assertTrue("int[] is array ", int[].class.isArray());
    assertEquals("int[] ComponentType is ", int.class, int[].class.getComponentType());
    assertTrue("Function is interface", Function.class.isInterface());
  }

  static class AC {
    public void fn1(){}

    private void fn2(){}

    public String fn3(String n){
      return null;
    }

  }

  static class ACS extends AC{
    public void ACSFn(){}
  }

  @Test
  public void getMethods(){
    Class<?> cls = ACS.class;
    Method[] all =  cls.getMethods();
    Method[] selfDefined = cls.getDeclaredMethods();
    System.out.println("all methods");
    for(Method method : all) System.out.println(method.getName());
    System.out.println("self defined methods");
    for(Method method : selfDefined) System.out.println(method.getName());
  }

  public static void main(String[] args) {
    new ClassFunctionTest().getMethods();
  }
}
