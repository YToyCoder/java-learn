package com.silence.junit4;

import static org.hamcrest.CoreMatchers.both;
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.hamcrest.core.Is.is;

import java.util.ArrayList;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class TestExceptionTesting {

	// 4.13 可以使用assertThrows来测试特定函数抛出特定地错误
	@Test
	public void testExceptionAndState() {
		// The method assertThrows has been added to the Assert class in version 4.13.
		List<Object> list = new ArrayList<>();
		IndexOutOfBoundsException thrown = assertThrows(
				IndexOutOfBoundsException.class,
				() -> list.add(1, new Object()));
		assertEquals("Index: 1, Size: 0", thrown.getMessage());
		assertTrue( list.isEmpty() );
	}

	@Test
	public void testExceptionMessage() {
		// If you project is not yet using JUnit 4.13 or your code base does not support lambdas, 
		// you can use the try/catch idiom which prevailed in JUnit 3.x:
		
		List<Object> list = new ArrayList<>();
    
		try {
			list.get(0);
			fail("Expected an IndexOutOfBoundsException to be thrown");
		} catch (IndexOutOfBoundsException anIndexOutOfBoundsException) {
			assertThat(anIndexOutOfBoundsException.getMessage(), containsString("Index 0"));
		}
	}

	// The @Test annotation has an optional parameter "expected" that takes as values subclasses of Throwable
	@Test(expected = IndexOutOfBoundsException.class) 
	public void empty() { 
		new ArrayList<Object>().get(0); 
	}

	// ExpectedException Rule
	@Rule
	public ExpectedException thrown = ExpectedException.none();

	// this approach has been deprecated in JUnit 4.13
	@Test
	public void shouldTestExceptionMessage() throws IndexOutOfBoundsException {
		List<Object> list = new ArrayList<Object>();

		thrown.expect(IndexOutOfBoundsException.class);
		thrown.expectMessage(containsString("Index 0"));
		list.get(0); // execution will never get past this line
	}


}
