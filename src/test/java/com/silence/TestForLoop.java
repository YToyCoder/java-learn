package com.silence;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import org.junit.Test;

public class TestForLoop {
	
	@Test
	public void test() {
		// for(Object i : new AIterable(10)){
		// 	System.out.println(i);
		// }
		List<Integer> ls = new LinkedList<>(List.of(1, 2, 3));
		ListIterator<Integer> iter = ls.listIterator();
		while(iter.hasNext()) {
			iter.next();
			if(!iter.hasNext()){
				iter.add(100);
				iter.previous();
				break;
			} 
		}
		// System.out.println(String.format("iter hasNext %s : val is %d", iter.hasNext(), iter.next()));
		// for(var v : ls) System.out.println(v);
	}
	
	public static class AIterable implements Iterable<Integer> {
		
		private final int max;
		
		public AIterable(final int val){
			max = val;
		}

		@Override
		public Iterator<Integer> iterator() {
			return new Iter(max);
		}
		
		static class Iter implements Iterator<Integer> {
			
			private int val;

			public Iter(int v){
				val = v;
			}

			@Override
			public boolean hasNext() {
				return val > 0;
			}

			@Override
			public Integer next() {
				val--;
				return val;
			}
		}
	}
}
