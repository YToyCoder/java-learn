package com.silence.reflect;

import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;
import java.util.List;

import org.junit.Test;

public class TestLambdaAndSerialization {

	@Test
	public void test(){
		SSupplier<String> supplier = List.of()::toString;
		Class<?> cls = supplier.getClass();
		try{
			Method method = cls.getDeclaredMethod("writeReplace");
			method.setAccessible(true);
			SerializedLambda l = (SerializedLambda) method.invoke(supplier);
			System.out.println(l);
		}catch(Throwable e){
			System.out.println(e);
			System.out.println("error");
		}
	}
	
	public static void main(String[] args) {
		new TestLambdaAndSerialization().test();
	}
}
