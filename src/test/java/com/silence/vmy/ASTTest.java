package com.silence.vmy;

import static org.junit.Assert.assertThrows;

import org.junit.Test;

import com.silence.vmy.AST.VmyAST;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.function.Consumer;

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

  static Set<String> testCases = Set.of(
      "1 + 2 * ( 3 + 4 )",
      "1 + 2 / (3 + 4 )",
      "(1 + 2) / ( 3 + 4 ) * 4",
      "( 2 + 4 ) * 2 + 3 - 4 * 5"
  );
  @Test
  public  void testBuildWithScanner(){
    cases4(
        testCases,
        el -> AST.build(Scanners.scanner(el))
    );
  }

  @Test
  public void testDeclaration(){
    cases4(
        Set.of(
            "let a",
            "val b",
            "val c : Int"
        ),
        el -> AST.build(Scanners.scanner(el))
    );
  }

  @Test
  public void testAssignment(){
    cases4(
        Set.of(
            "let a = b",
            "let a : Int = 1",
            "let a : Int = 1 + 2",
            "let a : Int = 1 + 2 * ( 10 + 9) - 1"
        ),
        el -> AST.build(Scanners.scanner(el))
    );
  }

  void cases4(Set<String> cases, Consumer<String> test){
    cases.forEach(test);
  }
}
