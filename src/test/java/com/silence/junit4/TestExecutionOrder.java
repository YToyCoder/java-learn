package com.silence.junit4;

import org.junit.Test;
import org.junit.runner.OrderWith;
import org.junit.runner.manipulation.Alphanumeric;
// OrderWith的参数是Ordering.Factory的实例
import org.junit.runner.manipulation.Ordering;
@OrderWith(Alphanumeric.class)
public class TestExecutionOrder {

	@Test
	public void testA(){
		System.out.println("first");
	}

	@Test
	public void testB(){
		System.out.println("second");
	}

	@Test
	public void testC(){
		System.out.println("third");
	}
}
// Alphanumeric 测试函数名称进行排序
/**
import java.util.Comparator;

import org.junit.runner.Description;
public final class Alphanumeric extends Sorter implements Ordering.Factory {

	public Alphanumeric() {
			super(COMPARATOR);
	}

	public Ordering create(Context context) {
			return this;
	}

	private static final Comparator<Description> COMPARATOR = new Comparator<Description>() {
			public int compare(Description o1, Description o2) {
					return o1.getDisplayName().compareTo(o2.getDisplayName());
			}
	};
}
 */

