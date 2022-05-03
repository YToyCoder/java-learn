package com.silence.reflect;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import org.junit.Test;

public class TestGetName {
	
	@Test
	public void testGetName() {
		function(List.of()::toString);
	}
	
	private String function(Supplier<String> supplier){
		Class<?> cls = supplier.getClass();
		Class<?> clz = cls.getDeclaringClass();
		Class<?> ecls = cls.getEnclosingClass();
		String cname = cls.getCanonicalName();
		System.out.println(cname);
		return "";
	}
}
