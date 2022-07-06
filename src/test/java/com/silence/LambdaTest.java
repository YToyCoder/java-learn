package com.silence;

import static org.junit.Assert.assertEquals;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.function.Function;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

public class LambdaTest {
  private static final String GET_SL_METHOD = "writeReplace";

  public static Logger log = LogManager.getLogger(LambdaTest.class);

  private static <I,O> SerializedLambda getSerializedLambda(SerializeFunction<I,O> func){
    try {
      final Method method = func.getClass().getDeclaredMethod(GET_SL_METHOD);
      return (SerializedLambda) method.invoke(func);
    } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
      log.error(e.getMessage(), e);
    }
    return null;
  }
  
  public static <I,O> String get(SerializeFunction<I,O> func){
    final SerializedLambda sLambda = getSerializedLambda(func);
    return Objects.nonNull(sLambda) ? sLambda.getImplMethodName() : "";
  }

  @Test
  public void test(){
    final String methodName = get(LambdaTest::getClass);
    assertEquals("getClass", methodName);
  }

}

interface SerializeFunction<I,O> extends Function<I,O> , Serializable{
}
