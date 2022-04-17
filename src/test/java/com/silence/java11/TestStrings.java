package com.silence.java11;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TestStrings {
	
	@Test
	public void testIsBlank(){
		assertTrue("\" \"", " ".isBlank());
	}

	@Test
	public void testStrip(){
		assertEquals("test", " test ".strip());
		assertEquals(" test", " test  ".stripTrailing());
		assertEquals("test  ", "   test  ".stripLeading());
	}

	@Test
	public void testRepeat(){
		assertEquals("testtest", "test".repeat(2));
	}
	
}
