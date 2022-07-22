package com.silence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThrows;

import java.time.LocalTime;

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
  public void testThreadState(){
    Thread t = new Thread(() -> {});
    assertEquals(Thread.State.NEW, t.getState());
  }

  @Test
  public void testThreadStart() {
    assertThrows(IllegalThreadStateException.class, () -> {
      Thread t = new Thread(() -> {});
      t.start();
      t.start();
    });
  }

  Object lock = new Object();

  @Test(timeout = 2000)
  public void testThreadWait(){
    /**
     * 线程状态转换 RUNNABLE -> WAITING
     */
    System.out.printf("start run at %d\n", System.currentTimeMillis());
    Thread mainthread = Thread.currentThread();
    System.out.printf("main thread state is %s\n", mainthread.getState());
    new Thread(() -> {
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }finally{
        synchronized(lock){
          System.out.printf("main thread state is %s(sub thread before notifyAll)\n", mainthread.getState());
          lock.notifyAll();
          System.out.printf("main thread state is %s(sub thread)\n", mainthread.getState());
          System.out.printf("stop wait at %d\n", System.currentTimeMillis());
        }
      }
    }).start();
    try {
      synchronized(lock){
        System.out.printf("wait lock at %d\n", System.currentTimeMillis());
        lock.wait();
        System.out.printf("main thread state is %s(in main after wait finish)\n", mainthread.getState());
      }
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    System.out.printf("finish run at %d\n", System.currentTimeMillis());
  }

  @Test
  public void testThreadWaiting(){
  }

  @Test
  public void testThreadSleep() {
    Thread main = Thread.currentThread();
    new Thread(() -> {
      try {
        Thread.sleep(10L);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      System.out.printf("main thread state is %s\n", main.getState());
    }).start();;
    try {
      Thread.sleep(1000L);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  Object timedLock = new Object();

  @Test
  public void testWaitLong(){
    System.out.printf("start time %d\n", System.currentTimeMillis());
    Thread main = Thread.currentThread();

    new Thread(() -> {
      System.out.printf("main state is %s (in sub thread %s before notifyAll) \n", main.getState(), Thread.currentThread().getState());
      System.out.println("finished sub thread");
    }).start();

    synchronized(timedLock){
      try {
        System.out.printf("before main wait\n");
        timedLock.wait(1000L);
        System.out.printf("after main wait\n");
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }

    System.out.printf("finish main %d \n", System.currentTimeMillis());
  }


  @Test
  public void testJoinLong(){
    System.out.printf("%d start time \n", System.currentTimeMillis());
    Thread main = Thread.currentThread();

    Thread sub = new Thread(() -> {
      long startT = System.currentTimeMillis();
      System.out.printf("%d start time - main thread state %s (sub before sleep) \n", System.currentTimeMillis(), main.getState());
      while(System.currentTimeMillis() - startT < 1000){
      }
      System.out.printf("%d time - main thread state %s (sub before sleep) \n", System.currentTimeMillis(), main.getState());
      System.out.printf("%d finish sub thread time (sub before sleep) \n", System.currentTimeMillis());
    });
    sub.start();

    try {
      sub.join(500L);
      System.out.printf("%d sub thread join finish \n", System.currentTimeMillis());
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    System.out.printf("%d finish main \n", System.currentTimeMillis());
  }

  public static void main(String[] args) {
    new ThreadTest().testJoinLong();
  }

}
