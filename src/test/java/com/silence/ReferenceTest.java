package com.silence;

import java.lang.ref.WeakReference;
import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

public class ReferenceTest {

  static Logger log = LogManager.getLogger(ReferenceTest.class);

  @Test
  public void test(){
    System.out.println("start creating reference ...");
    WeakReference<Object> weakReference = new WeakReference<Object>(new Object());
    Object strongReference = new Object();
    System.out.println("ending creating reference");
    System.out.println(String.format("weak-reference is null ? %b", Objects.isNull(weakReference.get())));
    System.out.println(String.format("strong-reference is null ? %b", Objects.isNull(strongReference)));
    System.out.println("starting call gc");
    System.gc();
    try {
      Thread.sleep(100);
    } catch (InterruptedException e) {
      log.error("sleep error", e);
    }
    System.out.println("gc finish");
    System.out.println(String.format("weak-reference is null ? %b", Objects.isNull(weakReference.get())));
    System.out.println(String.format("strong-reference is null ? %b", Objects.isNull(strongReference)));
  }

}
