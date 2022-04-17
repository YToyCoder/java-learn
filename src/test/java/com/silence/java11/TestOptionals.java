package com.silence.java11;

import static org.junit.Assert.assertEquals;

import java.util.Optional;

import org.junit.Test;

public class TestOptionals {
	
	@Test(expected = NullPointerException.class)
	public void testOf(){
		var val = Optional.of(null);
	}

	@Test
	public void testOfNullable(){
		String test =
		Optional.<String>ofNullable(null)
			.or(() -> Optional.of("test"))
			.get();
		assertEquals("test", test);
	}

}
