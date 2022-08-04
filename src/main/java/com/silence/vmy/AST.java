package com.silence.vmy;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Stack;

public class AST {
  private AST(){}

  static interface ASTNode{}

  static class ValNode implements ASTNode {
    final Number value;
    public ValNode(final Number _val){
      value = _val;
    }
  }

  static class CommonNode implements ASTNode{
    final String OP;
    ASTNode left;
    ASTNode right;
    public CommonNode(final String _op, final ASTNode _left, final ASTNode _right){
      OP = _op;
      left = _left;
      right = _right;
    }
  }

  static class VmyAST{
    private ASTNode root;
  }

  static VmyAST build(List<Token> tokens){
    Stack<String> operatorStack = new Stack<>();
    Stack<ASTNode> nodesStack = new Stack<>();
    Iterator<Token> tokenIterator = tokens.iterator();
    TokenHandler handler = getTokenHandler();
    while(tokenIterator.hasNext()){
      handler.handle(tokenIterator.next(), tokenIterator, operatorStack, nodesStack);
    }
    VmyAST ast = new VmyAST();
    // todo
    if(!nodesStack.isEmpty()){
      ASTNode merge = nodesStack.pop();
      while(
        !operatorStack.isEmpty() && 
        !nodesStack.isEmpty()
      ){
          final String operator = operatorStack.pop();
          final ASTNode asLeft = nodesStack.pop();
          merge = new CommonNode(operator, asLeft, merge);
      }
      ast.root = merge;
    }
    return ast;
  }

  static interface TokenHandler{
    void handle(Token token, Iterator<Token> remains, Stack<String> operatorStack, Stack<ASTNode> nodesStack);
  }

  private static abstract class BaseHandler implements TokenHandler{
    private BaseHandler next;

    public void setNext(final BaseHandler _next){
      next = _next;
    }

    @Override
    final public void handle(Token token, Iterator<Token> remains, Stack<String> operatorStack, Stack<ASTNode> nodesStack){
      if(canHandle(token, operatorStack, nodesStack))
        doHandle(token, remains, operatorStack, nodesStack);
      else if(Objects.nonNull(next))
        next.handle(token, remains, operatorStack, nodesStack);
    }
    
    public abstract boolean canHandle(Token token, Stack<String> operatorStack, Stack<ASTNode> nodesStack); 
    public abstract void doHandle(Token token, Iterator<Token> remains, Stack<String> operatorStack, Stack<ASTNode> nodesStack); 

  }

  static class NumberHandler extends BaseHandler{

    @Override
    public boolean canHandle(Token token, Stack<String> operatorStack, Stack<ASTNode> nodesStack) {
      return token.tag == Token.INT_V || token.tag == Token.DOUBLE_V;
    }

    @Override
    public void doHandle(Token token, Iterator<Token> remains, Stack<String> operatorStack, Stack<ASTNode> nodesStack) {
      nodesStack.add(new ValNode(token.tag == Token.DOUBLE_V ? Double.valueOf(token.value) : Integer.valueOf(token.value)));
    }

  }

  static class OperatorHandler extends BaseHandler{

    @Override
    public boolean canHandle(Token token, Stack<String> operatorStack, Stack<ASTNode> nodesStack) {
      return token.tag == Token.OP;
    }

    @Override
    public void doHandle(Token token, Iterator<Token> remains, Stack<String> operatorStack, Stack<ASTNode> nodesStack) {
      if(operatorEquals(Operators.ADD, token) || operatorEquals(Operators.DIVIDE, token)){
        operatorStack.add(token.value);
      }else if(operatorEquals(Operators.MULTI, token)){
        if(!remains.hasNext()) 
          throw new ASTProcessingException("*(multiply) doesn't have right side");
        Token right = remains.next();
        if(right.tag == Token.OP)
          throw new ASTProcessingException("*(multiply) right side should be a value type, not operator");
        if(nodesStack.isEmpty())
          throw new ASTProcessingException("*(multiply) left side not exists");
        ASTNode left = nodesStack.pop();
        nodesStack.add(new CommonNode(Operators.MULTI, left, token2ValNode(token)));
      }else if(operatorEquals(Operators.OpenParenthesis, token)){
        operatorStack.add(token.value);
      }else if(operatorEquals(Operators.ClosingParenthesis, token)){
        if(nodesStack.isEmpty()) 
          throw new ASTProcessingException(") (closing parenthesis) has no content");
        ASTNode mergeNode = nodesStack.pop();
        while(
          !operatorStack.isEmpty() && 
          !nodesStack.isEmpty() && 
          !operatorStack.peek().equals(Operators.OpenParenthesis)
        ){
          final String operator = operatorStack.pop();
          final ASTNode asLeft = nodesStack.pop();
          mergeNode = new CommonNode(operator, asLeft, mergeNode);
        }
        if(!operatorStack.peek().equals(Operators.OpenParenthesis))
          throw new ASTProcessingException("error at processing parentthesise");
        operatorStack.pop();
        nodesStack.add(mergeNode);
      }else
        throw new ASTProcessingException("not support operator " + token.value);
    }

  }

  static ValNode token2ValNode(final Token token){
    return token.tag == Token.DOUBLE_V ? new ValNode(Double.valueOf(token.value)) : new ValNode(Integer.valueOf(token.value));
  }

  static boolean operatorEquals(final String operator, final Token token){
    return Objects.equals(operator, token.value);
  }

  // a static instance
  private static TokenHandler HANDLER;

  static TokenHandler getTokenHandler(){
    if(Objects.isNull(HANDLER))
      buildHandler();
    return HANDLER;
  }

  static void buildHandler(){
    HANDLER = new HandlerBuilder()
    .next(new NumberHandler())
    .next(new OperatorHandler())
    .build();
  }

  static class HandlerBuilder {
    private List<BaseHandler> handlers = new LinkedList<>();
    HandlerBuilder next(final BaseHandler next){
      return this;
    }

    TokenHandler build(){
      if(handlers.isEmpty()) return null;
      BaseHandler head = handlers.get(0);
      BaseHandler walk = head;
      for(int i=1; i<handlers.size(); i++){
        BaseHandler current = handlers.get(i);
        walk.setNext(current);
        walk = current;
      }
      return head;
    }
  }
  
}
