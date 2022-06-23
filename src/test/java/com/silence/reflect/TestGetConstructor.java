package com.silence.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.junit.Test;

public class TestGetConstructor {
  static class StaticClass {
    public StaticClass() {
    }

    public StaticClass(String param){
    }
  }
  
  @Test
  public void testDeclaredConstrutor() {
    final Class<StaticClass> clz = StaticClass.class;
    try {
      Constructor<StaticClass> constructor =  clz.getDeclaredConstructor();
      constructor.newInstance();
    }catch(NoSuchMethodException ex){
      System.out.println("不存在default构造函数");
    }catch(SecurityException ex){
      System.out.println("类加载器出错");
    } catch (InstantiationException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IllegalArgumentException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (InvocationTargetException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
}
