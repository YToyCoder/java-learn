package com.silence.vmy;

import static org.junit.Assert.assertThrows;

import org.junit.Test;

import com.silence.vmy.AST.VmyAST;

public class ASTTest {

  @Test
  public void test1(){
    VmyAST ast = AST.build(Scanners.scan("1 + 2"));
  }

  @Test
  public void testBuildException(){
    assertThrows(ASTProcessingException.class, () -> {
      AST.build(Scanners.scan("1 + 2)"));
    });

    assertThrows(ASTProcessingException.class, () -> {
      AST.build(Scanners.scan("1 ++ 2"));
    });

    assertThrows(ASTProcessingException.class, () -> {
      AST.build(Scanners.scan(" * 2"));
    });

    assertThrows(ASTProcessingException.class, () -> {
      AST.build(Scanners.scan("1 * "));
    });
    assertThrows(ASTProcessingException.class, () -> {
      AST.build(Scanners.scan(" + 2"));
    });
  }

  @Test
  public void testBuild(){
    AST.build(Scanners.scan("1 + 2 * (3 + 4)"));
    AST.build(Scanners.scan("1 + 2 / (3 + 4)"));
    AST.build(Scanners.scan("(1 + 2) / (3 + 4)"));
    AST.build(Scanners.scan("(1 + 2) / (3 + 4 * 5)"));
    AST.build(Scanners.scan("(1 + 2) / (3 + 4 * (5 + 1))"));
  }
}
