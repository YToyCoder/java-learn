package com.silence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

public class ThreadTest {

  @Test
  public void testThreadGroup() {
    new Thread(new ThreadGroupNameCompare(Thread.currentThread().getThreadGroup().getName())).start();
    new Thread(() -> {
      assertNotEquals("rangeName", Thread.currentThread().getThreadGroup().getName());
    }).start();
    new Thread(() -> {
      assertEquals("main", Thread.currentThread().getThreadGroup().getName());
    }).start();
  }

  static class ThreadGroupNameCompare implements Runnable{
    private final String tgName;
    public ThreadGroupNameCompare(String tgName){
      this.tgName = tgName;
    }

    @Override
    public void run() {
      assertEquals(tgName, Thread.currentThread().getThreadGroup().getName());
    }
  }

  @Test
  public void testThreadGroupExceptionHandle(){
    ThreadGroup threadGroup = new ThreadGroup("thread-group-test"){
      @Override
      public void uncaughtException(Thread t, Throwable e){
        System.out.println(String.format("thread %s , exception %s", t.getName(), e.getMessage()));
      }
    };

    new Thread( threadGroup, () -> {
      throw new RuntimeException("thread group handle error");
    }).start();
  }

}
