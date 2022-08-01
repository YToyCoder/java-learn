package com.silence;

import static org.junit.Assert.assertEquals;

import java.util.TreeMap;

import org.junit.Test;

public class Java17Test {

  @Test
  public void testTreeMap(){
    TreeMap<String, String> map = new TreeMap<>();
    map.put("key", null);
    // jdk 17 之前computeIfAbsent: 如果value值是null, 将会直接返回null
    // jdk 17 改为: 执行计算
    // 其他版本的结果也是执行计算
    var value = map.computeIfAbsent("key", (key) -> "null");
    assertEquals("null", value);
  }

  @Test
  public void textBlock(){
    var block = """ 
        zero
        one
        """;
    System.out.println(block);
  }

  @Test
  public void switchReturn (){
    var str = switch (0) {
      case 0 -> "e";
      default -> "";
    };
    System.out.println(str);
    final var ans = switch (1) {
      case 1 -> {
        System.out.println("before return");
        yield "1";
      }
        
      default -> "-".repeat(10);
    };
    System.out.println(ans);
  }
}
