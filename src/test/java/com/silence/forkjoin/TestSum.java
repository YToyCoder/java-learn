package com.silence.forkjoin;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.RecursiveTask;

import org.junit.Test;

public class TestSum {
	@Test
	public void test() throws InterruptedException, ExecutionException {
		ForkJoinPool forkjoinPool = new ForkJoinPool();
		CountTask task = new CountTask(1, 4);
		Future<Integer> result = forkjoinPool.submit(task);
		assertEquals(Integer.valueOf(4) , result.get());
	}
	
	public static class CountTask extends RecursiveTask<Integer> {
		private static final int  THRESHOLD = 2; // 阈值
		
		private int start;
		private int end;
		
		public CountTask(int start, int end){
			this.start = start;
			this.end = end;
		}

		@Override
		protected Integer compute() {
			int sum = 0;
			boolean canCompute = (end - start) <= THRESHOLD;
			if(canCompute) {
				for(int i=start; i <= end; i++){
					sum += 1;
				}
			}else {
				int middle = (start + end) / 2;
				CountTask lefttask = new CountTask(start, middle);
				CountTask righttask = new CountTask(middle + 1, end);
				// 执行子任务
				lefttask.fork();
				righttask.fork();
				// 等待子任务执行完，并得到其结果
				int leftval = lefttask.join();
				int rightval = righttask.join();
				sum = leftval + rightval;
			}
			return sum;
		}
		
	}
}
