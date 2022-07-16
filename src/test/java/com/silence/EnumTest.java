package com.silence;

import org.junit.Test;

public class EnumTest {

  @Test
  public void test() {
    final int a = 100,b = 13;
    for(IntOp op : IntOp.values()){
      System.out.println(String.format("op-%s-(%d,%d)-%d", op, a, b, op.apply(a, b)));
    }

    for(bool el : bool.values()){
      System.out.println(String.format("%s-%d", el, el.ordinal()));
    }
  }



  static enum IntOp {
    ADD {
      @Override
      public int apply(int a, int b) {
        return a + b;
      }
    },
    DEVIDE {

      @Override
      public int apply(int a, int b) {
        return a / b;
      }
    };
    public abstract int apply(int a, int b);
  }

  static enum bool{
    False(0),
    True(2) 
    ;

    private int value;
    private bool(int value){
      this.value = value;
    }
  }
}
