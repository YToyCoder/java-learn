package com.silence.java8;

import static org.junit.Assert.assertNotNull;

import java.util.Collections;

import org.junit.Test;

public class TestFunctionalInterface {
	
	@Test
	public void aConstructorReferance(){
		FunctionalInterface.DefaultFactory<FunctionalInterface> creator = FunctionalInterface::new;
		FunctionalInterface instance = creator.create();
		assertNotNull(instance);
		FunctionalInterface.OneParamFactory<FunctionalInterface,String> creator2 = FunctionalInterface::new;
		FunctionalInterface instance2 = creator2.create("");
		assertNotNull(instance2);
		Collections.emptyList().stream();
	}
}
