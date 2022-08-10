package com.silence.vmy;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Stack;

public class AST {
  private AST(){}

  static interface ASTNode{}
  static interface Tree{}
  static interface Evaluator{
    Object eval(Tree tree);
  }

  static class ValNode implements ASTNode {
    final Number value;
    public ValNode(Number _val){
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

  // node for assignment
  // like : 
  //      let a : Type = 1
  //      a = 2
  static class AssignNode implements ASTNode {
    ASTNode variable;
    ASTNode expression;

    public AssignNode(ASTNode _variable, ASTNode expr){
      variable = _variable;
      expression = expr;
    }
  }

  // node for Identifier , like variable-name/function-name ...
  static class IdentifierNode implements ASTNode {
    final String value;
    public IdentifierNode(String _val){
      value = _val;
    }
  }

  // node for Declaration, like let a : Type , val a : Type
  static class DeclareNode implements ASTNode {
    final String declare;
    final String type;
    final IdentifierNode identifier;
    public DeclareNode(String _declare, IdentifierNode _identifier, String _type){
      declare = _declare;
      identifier = _identifier;
      type =_type;
    }

    public DeclareNode(String _declare, IdentifierNode _identifier){
      this(_declare, _identifier, null);
    }
  }

  static class VmyAST implements Tree{
    private ASTNode root;
  }

  public static VmyAST build(List<Token> tokens){
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
      if(!nodesStack.isEmpty() || !operatorStack.isEmpty())
        throw new ASTProcessingException("expression wrong");
      ast.root = merge;
    }
    return ast;
  }

  static interface TokenHandler{
    void handle(Token token, Iterator<Token> remains, Stack<String> operatorStack, Stack<ASTNode> nodesStack);
  }


  private static abstract class BaseHandler implements TokenHandler, Utils.Recursive{
    private BaseHandler next;

    private BaseHandler head;

    public void setNext(final BaseHandler _next){
      next = _next;
    }

    public void setHead(BaseHandler _head){
      head = _head;
    }

    final protected void recall(Token token, Iterator<Token> remains, Stack<String> operatorStack, Stack<ASTNode> nodesStack){
      if(Objects.isNull(head))
        throw new Utils.RecursiveException("can't do recall head is not exists!");
      head.handle(token, remains, operatorStack, nodesStack);
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
//      nodesStack.add(new ValNode(token.tag == Token.DOUBLE_V ? Double.parseDouble(token.value) : Integer.parseInt(token.value)));
      nodesStack.add(token.tag == Token.DOUBLE_V ? new ValNode( Double.parseDouble(token.value) ) : new ValNode(Integer.parseInt(token.value)));
    }

  }

  static class OperatorHandler extends BaseHandler{

    @Override
    public boolean canHandle(Token token, Stack<String> operatorStack, Stack<ASTNode> nodesStack) {
      return token.tag == Token.Identifier;
    }

    @Override
    public void doHandle(Token token, Iterator<Token> remains, Stack<String> operatorStack, Stack<ASTNode> nodesStack) {
      if(operatorEquals(Identifiers.ADD, token) || operatorEquals(Identifiers.SUB, token)){
        operatorStack.add(token.value);
      }else if(operatorEquals(Identifiers.MULTI, token) || operatorEquals(Identifiers.DIVIDE, token)){
        if(!remains.hasNext()) 
          throw new ASTProcessingException("*(multiply) doesn't have right side");
        if(nodesStack.isEmpty())
          throw new ASTProcessingException("*(multiply) left side not exists");
        ASTNode left = nodesStack.pop();
        // getTokenHandler().handle(remains.next(), remains, operatorStack, nodesStack);
        recall(remains.next(), remains, operatorStack, nodesStack);
        nodesStack.add(new CommonNode(token.value, left, nodesStack.pop()));
      }else if(operatorEquals(Identifiers.OpenParenthesis, token)){
        operatorStack.add(token.value);
        Token nextToken;
        while(remains.hasNext() && !operatorEquals(Identifiers.ClosingParenthesis, nextToken = remains.next())){
          recall(nextToken, remains, operatorStack, nodesStack);
        }
      // }else if(operatorEquals(Identifiers.ClosingParenthesis, token)){
        if(nodesStack.isEmpty()) 
          throw new ASTProcessingException(") (closing parenthesis) has no content");
        ASTNode mergeNode = nodesStack.pop();
        while(
          !operatorStack.isEmpty() && 
          !nodesStack.isEmpty() && 
          !operatorStack.peek().equals(Identifiers.OpenParenthesis)
        ){
          final String operator = operatorStack.pop();
          final ASTNode asLeft = nodesStack.pop();
          mergeNode = mergeTwoNodes(asLeft, mergeNode, operator);
        }
        if(operatorStack.isEmpty() || !operatorStack.peek().equals(Identifiers.OpenParenthesis))
          throw new ASTProcessingException("error at processing parenthesise");
        operatorStack.pop();
        nodesStack.add(mergeNode);
      }else
        throw new ASTProcessingException("not support operator " + token.value);
    }

  }

  // handle the expression like
  //    let a : Int = 2 or
  //    b = 1
  static class AssignmentHandler extends BaseHandler {
    @Override
    public boolean canHandle(Token token, Stack<String> operatorStack, Stack<ASTNode> nodesStack) {
      return token.tag == Token.Assignment;
    }

    @Override
    public void doHandle(Token token, Iterator<Token> remains, Stack<String> operatorStack, Stack<ASTNode> nodesStack) {
      // =
      // 1 get the variable name or a declaration
      ASTNode variable;
      if(
          nodesStack.isEmpty() ||
          !((variable = nodesStack.pop()) instanceof DeclareNode) ||
          !(variable instanceof IdentifierNode)
      )
        throw new ASTProcessingException("assignment has no variable or declare expression");
      operatorStack.add(token.value);
      Token nextToken;
      while(remains.hasNext() && (nextToken = remains.next()).tag != Token.NewLine){
        recall(nextToken, remains, operatorStack, nodesStack);
      }
      // build it
      if(nodesStack.isEmpty())
        throw  new ASTProcessingException("assignment has no value expression");
      ASTNode the_value = nodesStack.pop();
      while(!operatorStack.isEmpty() && !Objects.equals( operatorStack.peek(), Identifiers.Assignment)){
        the_value = mergeTwoNodes(nodesStack.pop(), the_value, operatorStack.pop());
      }
      nodesStack.add(new AssignNode(variable, the_value));
      operatorStack.pop();
    }
  }

  // handle declaration expression, like
  //      let a , or
  //      let a : Int , or
  //      val a , or
  //      val a : Int
  static class DeclarationHandle extends BaseHandler {
    @Override
    public boolean canHandle(Token token, Stack<String> operatorStack, Stack<ASTNode> nodesStack) {
      return token.tag == Token.Declaration;
    }

    @Override
    public void doHandle(Token token, Iterator<Token> remains, Stack<String> operatorStack, Stack<ASTNode> nodesStack) {
      Token identifier;
      if((identifier = remains.next()).tag != Token.Identifier)
        throw new ASTProcessingException("declaration has no right identifier " + identifier.value);
    }
  }

  static ASTNode mergeTwoNodes(ASTNode left, ASTNode right, String _op){
    return new CommonNode(_op, left, right);
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
    .next(new AssignmentHandler())
    .build();
  }

  static class HandlerBuilder {
    private List<BaseHandler> handlers = new LinkedList<>();
    HandlerBuilder next(final BaseHandler next){
      handlers.add(next);
      return this;
    }

    TokenHandler build(){
      if(handlers.isEmpty()) return null;
      BaseHandler head = handlers.get(0);
      head.setHead(head);
      BaseHandler walk = head;
      for(int i=1; i<handlers.size(); i++){
        BaseHandler current = handlers.get(i);
        current.setHead(head);
        walk.setNext(current);
        walk = current;
      }
      return head;
    }
  }

  static class VmyTreeEvaluator implements Evaluator{

    @Override
    public Object eval(Tree tree) {
      if(tree instanceof VmyAST ast){
        return evalsub(ast.root);
      }else
        throw new EvaluatException("unrecognized AST");
    }

    Object evalsub(ASTNode node){
      if(node instanceof ValNode val){
        return val.value;
      }else if(node instanceof CommonNode common){
        Object left = evalsub(common.left);
        Object right  = evalsub(common.right);
        if(Objects.isNull(right) || Objects.isNull(left))
          throw new EvaluatException(common.OP + " can't handle null object");
        Ops op = Ops.OpsMapper.get(common.OP);
        if(Objects.isNull(op))
          throw new EvaluatException("op(" + common.OP + ") not support!");
        return Objects.nonNull(op) ? op.apply(left, right) : null;
      }else 
        throw new EvaluatException("unrecognizable AST node");
    }

    
  }
  
}
