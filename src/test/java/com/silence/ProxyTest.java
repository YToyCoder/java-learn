package com.silence;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Objects;

import org.junit.Test;

public class ProxyTest {
  private static Object getProxy(Object target, InvocationHandler handler) {
    return Proxy.newProxyInstance(
      target.getClass().getClassLoader(), 
      target.getClass().getInterfaces(), 
      Objects.requireNonNull(handler)
    );
  }


  public interface AInterface {
    String get();
  }

  public class AInterfaceIH implements InvocationHandler {
    private final Object target;

    public AInterfaceIH(Object _obj){
      target = _obj;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
      return Prefix + method.invoke(target, args);
    }

  }

  public static final String OriginS = "origin";
  public static final String Prefix = "pre-";

  @Test
  public void t1(){
    AInterface ai = () -> OriginS;
    AInterface proxy = (AInterface) getProxy(ai, new AInterfaceIH(ai));
    assertEquals(Prefix + OriginS, proxy.get());
  }

}
