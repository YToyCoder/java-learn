package com.silence.java11;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

public class TestCollections {

	@Test
	public void testListOf(){
		List<Integer> list = List.of(1,4,2,-1);
		assertArrayEquals(new Integer[]{1 , 4, 2, -1}, list.toArray());
	}
	
	@Test
	public void testSetOf(){
		Set<Integer> set = Set.of(1,2,3);
		assertEquals(3, set.size());
		long len = set.stream().dropWhile(it -> it == 1 || it == 2 || it == 3).count();
		assertEquals(0, len);
	}

	@Test
	public void testMapOf(){
		Map<Integer, String> map = Map.of(1,"hello", 2, "name");
		//{2=name, 1=hello}
		// System.out.println(map.toString());
	}

}
