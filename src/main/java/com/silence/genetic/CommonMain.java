package com.silence.genetic;

public class CommonMain {
  static NumberServe numberServe = new NumberServe();
  static StringServe stringServe = new StringServe();
  public static void main(String[] args) {
  }
  
  public static void doServe(Common<?, ?> common) {
  }

  public static class StringServe implements CommonServer<String, String> {
    public void serve(Common<String, String> common) {
      System.out.println("string");
    }
  }
  
  public static class NumberServe implements CommonServer<Number, Number> {
    public void serve(Common<Number, Number> common) {
      System.out.println("number");
    }
  }
}
