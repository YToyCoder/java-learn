package com.silence.java11;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;

public class TestStream {

	@Test
	public void testNewConstructor() {
		long length = Stream.ofNullable(null).count();
		assertEquals(0, length);
	}

	@Test
	public void DropWhile() {
		List<Integer> list = Stream.of(1, 2, 3)
				.dropWhile(n -> n > 2)
				.collect(Collectors.toList());
		System.out.println(list);
	}
}
