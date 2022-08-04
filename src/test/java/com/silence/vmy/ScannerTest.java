package com.silence.vmy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

import org.junit.Test;

public class ScannerTest {

  static Map<Integer, String> oneCharSource = new HashMap<>();

  static {
    oneCharSource.put(Token.OP, "*");
    oneCharSource.put(Token.OP, "/");
    oneCharSource.put(Token.OP, "-");
    oneCharSource.put(Token.OP, "+");
    oneCharSource.put(Token.INT_V, "12");
    oneCharSource.put(Token.INT_V, " 12 ");
    oneCharSource.put(Token.INT_V, "140");
    oneCharSource.put(Token.DOUBLE_V, "1.2");
  }

  @Test
  public void testHandler(){
    oneCharSource.forEach((k, v) -> {
      List<Token> tokens = Scanners.scan(v);
      assertTrue(v + "length should be 1", tokens.size() == 1);
      assertEquals(v, (long)tokens.get(0).tag, (long)k);
      assertEquals(v, tokens.get(0).value, v.trim());
    });

    assertThrows("? should not be handled" ,RuntimeException.class, () -> {
      Scanners.scan("?");
    });

    List<Token> tokens = Scanners.scan("1 2");
    assertTrue("1 2", tokens.size() == 2);
    tokens.forEach(el -> {
      assertEquals(Token.INT_V, el.tag);
    });

  }

  @Test
  public void testHandler2() {
    final String source = "1*2";
    List<Token> tokens2 = Scanners.scan(source);
    assertTrue(source, tokens2.size() == 3);
    assertTrue(source + " should be 1", tokens2.get(0).tag == Token.INT_V);
    assertEquals(source + " should be 1", tokens2.get(0).value, "1");

    assertTrue(source + " should be *", tokens2.get(1).tag == Token.OP);
    assertEquals(source + " should be *", tokens2.get(1).value, "*");

    assertTrue(source + " should be 1", tokens2.get(2).tag == Token.INT_V);
    assertEquals(source + " should be 1", tokens2.get(2).value, "2");
  }

}
