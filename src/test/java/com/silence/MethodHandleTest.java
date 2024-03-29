package com.silence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

public class MethodHandleTest {

  public static Logger log = LogManager.getLogger(MethodHandleTest.class);

  @Test
  public void test(){
    final MethodHandles.Lookup lookup = MethodHandles.lookup();
    try {
      final MethodHandle func = lookup.findStatic(MethodHandleTest.class, "func", MethodType.methodType(void.class));
      func.invokeExact();
    } catch (NoSuchMethodException e) {
      log.error("不存在该方法", e);
    } catch (IllegalAccessException e) {
      log.error("没有访问权限", e);
    } catch (Throwable e) {
      log.error("方法抛出错误", e);
    }
  }

  @Test
  public void test2() {
    // operate field by {@code MethodHandle}
    final MethodHandles.Lookup lookup = MethodHandles.lookup();
    try {
      final MethodHandle fieldGetter = lookup.findGetter(InnerClass.class, "field", String.class);
      final MethodHandle fieldSetter = lookup.findGetter(InnerClass.class, "field", String.class);
      final InnerClass innerClass = new InnerClass();
      assertNull(fieldGetter.invoke(innerClass));
      fieldSetter.invoke(innerClass, "field");
      assertEquals("field", fieldGetter.invoke(innerClass));
    } catch (NoSuchFieldException e) {
      log.error("不存在该方法", e);
    } catch (IllegalAccessException e) {
      log.error("没有访问权限", e);
    } catch (Throwable e) {
      log.error("方法抛出错误", e);
    }
  }

  public static void func(){
    System.out.println(String.format("call %s#%s", MethodHandleTest.class.getName(), "func()V"));
  }


  /**
   * hack the jdk.internal.reflect.Reflection#getCallerClass()
   */
  @Test
  public void getReflection(){
    final String reflection = "jdk.internal.reflect.Reflection";
    try {
      final Class<?> cls = Class.forName(reflection);
      // final MethodHandles.Lookup lookup = MethodHandles.lookup();
      // final MethodHandle methodHandle = lookup.findStatic(cls, "getCallerClass", MethodType.methodType(Class.class));
      // final Object calledClass = methodHandle.invoke();
      // assertEquals(this.getClass(), calledClass);
      final Method method = cls.getMethod("getCallerClass");
      method.setAccessible(true);
      assertEquals(this.getClass(), method.invoke(null));
    } catch (ClassNotFoundException e) {
      log.error(String.format("could't get %s", reflection), e);
    } catch (NoSuchMethodException e) {
      log.error("不存在该方法", e);
    } catch (IllegalAccessException e) {
      log.error("没有访问权限", e);
    } catch (Throwable e) {
      log.error("方法抛出错误", e);
    }
  }

  public static class InnerClass {
    private String field;
  }
}
