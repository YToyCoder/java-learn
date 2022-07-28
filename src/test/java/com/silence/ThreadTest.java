package com.silence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThrows;

import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;
import java.util.stream.Stream;

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

  static volatile int signal = 0;

  @Test
  public void implementsSemaphoreByVolatile(){
    final int MAX = 20;
    new Thread(() -> {
      while(signal < MAX){
        if(signal % 2 == 0){
          System.out.printf("%s - %d\n", Thread.currentThread().getName(), signal);
          synchronized(this){
            signal++;
          }
        }
      }
    }).start();
    try{
      Thread.sleep(100L);
    }catch(InterruptedException e){
      e.printStackTrace();
    }
    new Thread(() -> {
      while(signal < MAX){
        if(signal % 2 == 1){
          System.out.printf("%s - %d\n", Thread.currentThread().getName(), signal);
          synchronized(this){
            signal++;
          }
        }
      }
    }).start();
    try{
      Thread.sleep(1000l);
    }catch(InterruptedException e){
      e.printStackTrace();
    }
  }

  @Test
  public void interruptTest(){
    Thread thread = new Thread(() -> {
      System.out.printf("%d sub thread run\n", System.currentTimeMillis());
      while(true){
      }
    });
    try{
      Thread.sleep(1000L);
    }catch(InterruptedException e){
      e.printStackTrace();
    }
    System.out.printf("%d before call interrupt\n", System.currentTimeMillis());
    thread.interrupt();
    System.out.printf("%d after call interrupt\n", System.currentTimeMillis());
    System.out.printf("%d sub thread is interrupted %b\n", System.currentTimeMillis(), thread.isInterrupted());
    while(!thread.isInterrupted()){
    }
    System.out.printf("%d sub thread is interrupted \n", System.currentTimeMillis());
  }

  static int count = 0;
  public static void noLockMethod(){
    System.out.printf("count1 %d \n", count);
    System.out.printf("count2 %d \n", count);
    System.out.printf("count3 %d \n", count);
    count++;
  }

  public static synchronized void syncMethod(){
    noLockMethod();
  }

  public void method(){
    synchronized(this.getClass()){
      noLockMethod();
    }
  }

  @Test
  public void syncMethodDiver(){
    new Thread(() -> {
      syncMethod();
    }).start();
    new Thread(() -> {
      this.method();
    }).start();
    try{
      Thread.sleep(1000L);
    }catch(InterruptedException e){
      e.printStackTrace();
    }
  }

  @Test
  public void lockUsingTest(){
    ReentrantLock lock = new ReentrantLock();
    new Thread(() -> {
      lock.lock();
      
      Stream.generate(new Supplier<Integer>() {
        int seed = 0;

        @Override
        public Integer get() {
          return ++seed;
        }
      })
      .limit(5)
      .forEach(el -> {
        try{
          System.out.printf("%s %d\n", Thread.currentThread().getName(), el);
          Thread.sleep(100L);
        }catch(InterruptedException e){
          e.printStackTrace();
        }
      });

      lock.unlock();
    }).start();
    new Thread(() -> {
      lock.lock();
      
      Stream.generate(new Supplier<Integer>() {
        int seed = 0;

        @Override
        public Integer get() {
          return ++seed;
        }
      })
      .limit(5)
      .forEach(el -> {
        try{
          System.out.printf("%s %d\n", Thread.currentThread().getName(), el);
          Thread.sleep(100L);
        }catch(InterruptedException e){
          e.printStackTrace();
        }
      });

      lock.unlock();
    }).start();
    try{
      Thread.sleep(1500);
    }catch(InterruptedException e){
      e.printStackTrace();
    }
  }

}
