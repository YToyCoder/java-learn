package com.silence.vmy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

import org.eclipse.collections.impl.factory.Strings;
import org.junit.Test;

public class ScannerTest {

  static Map<Integer, String> oneCharSource = new HashMap<>();

  static {
    oneCharSource.put(Token.Identifier, "*");
    oneCharSource.put(Token.Identifier, "/");
    oneCharSource.put(Token.Identifier, "-");
    oneCharSource.put(Token.Identifier, "+");
    oneCharSource.put(Token.INT_V, "12");
    oneCharSource.put(Token.INT_V, " 12 ");
    oneCharSource.put(Token.INT_V, "140");
    oneCharSource.put(Token.DOUBLE_V, "1.2");
  }

  @Test
  public void testHandler(){
    var identifiers = Identifiers.operatorCharacters;
    oneCharSource.forEach((k, v) -> {
      List<Token> tokens = Scanners.scan(v);
      assertTrue(v + "length should be 1, but is " + tokens.size(), tokens.size() == 1);
      assertEquals(v, (long)tokens.get(0).tag, (long)k);
      assertEquals(v, tokens.get(0).value, v.trim());
    });

    assertThrows("? should not be handled" ,RuntimeException.class, () -> {
      Scanners.scan(",");
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

    assertTrue(source + " should be *", tokens2.get(1).tag == Token.Identifier);
    assertEquals(source + " should be *", tokens2.get(1).value, "*");

    assertTrue(source + " should be 1", tokens2.get(2).tag == Token.INT_V);
    assertEquals(source + " should be 1", tokens2.get(2).value, "2");
  }

  @Test
  public void testHandler3(){
    final String source = "1*2 + ( 3 - 1 ) ";
    List<Token> tokens = Scanners.scan(source);
    assertEqualTo(
      new Token[]{ 
        new Token(Token.INT_V, "1"), 
        new Token(Token.Identifier, "*"),
        new Token(Token.INT_V, "2"),
        new Token(Token.Identifier, "+"),
        new Token(Token.Identifier, "("),
        new Token(Token.INT_V, "3"),
        new Token(Token.Identifier, "-"),
        new Token(Token.INT_V, "1"),
        new Token(Token.Identifier, ")")
      }, 
      tokens.toArray(new Token[0]));
  }

  void assertEqualTo(Token[] expects, Token[] real){
    assertTrue("length should equal", expects.length == real.length);
    for(int i=0; i<expects.length; i++){
      assertTrue("token " + expects[i].value, expects[i].tag == real[i].tag);
      assertEquals("token " + expects[i].value, expects[i].value , real[i].value);
    }
  }

  // test token like : a = b
  @Test
  public  void assignmentTest(){
    assertEqualTo(
        new Token[]{
            new Token(Token.Assignment,"=")
        },
        Scanners.scan(" = ").toArray(new Token[0])
    );
  }

  @Test
  public void declarationTest(){
    assertEqualTo(
        new Token[]{
            new Token(Token.Declaration, "let"),
            new Token(Token.Declaration, "val")
        },
        Scanners.scan("let val").toArray(new Token[0])
    );
  }

  @Test
  public void string_literal_test(){
    assertEqualTo(
        new Token[]{
            new Token(Token.Literal, "string literal"),
            new Token(Token.Literal, " has black ")
        },
        Scanners.scan("\"string literal\" \" has black \"").toArray(new Token[0])
    );
    assertThrows(
        LexicalException.class,
        () -> {
          Scanners.scan("\"");
        }
    );
    assertThrows(
        LexicalException.class,
        () -> {
          Scanners.scan("\" lexical\r\n \"");
        }
    );

  }

  @Test
  public void black_test(){
    assertEqualTo(
        new Token[]{
            new Token(Token.Declaration, Identifiers.VarDeclaration)
        },
        Scanners.scan("let     ").toArray(new Token[0])
    );
    Scanner scanner = Scanners.scanner("let  let ");
    while(scanner.hasNext()){
      scanner.next();
    }
  }

  @Test
  public void print_call() {
    assertEqualTo(
        new Token[]{
            new Token(Token.BuiltinCall, "print")
        },
        Scanners.scan("print").toArray(new Token[0])
    );

    assertEqualTo(
        new Token[]{
            new Token(Token.BuiltinCall, "print"),
            new Token(Token.INT_V, "1")
        },
        Scanners.scan("print 1").toArray(new Token[0])
    );
  }


}
