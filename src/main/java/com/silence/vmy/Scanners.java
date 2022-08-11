package com.silence.vmy;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class Scanners {
  
  static class VmyScanner implements Scanner{
    private final SourceStringHandler handler;

    public VmyScanner(final SourceStringHandler handler){
      this("", handler);
    }

    public VmyScanner(String _source, SourceStringHandler _handler){
      this(_source, _handler, new LinkedList<>());
    } 

    public VmyScanner(String _source, SourceStringHandler _handler, List<Token> _Tokens){
      source = _source;
      handler = _handler;
      tokens = _Tokens;
    }

    public VmyScanner(List<Token> _Tokens){
      this("", null, _Tokens);
    }

    @Override
    public List<Token> scan(final String source) {
      int walk = 0;
      while(walk < source.length()){
        walk = handler.handle(tokens, source, walk);
      }
      return tokens;
    }

    private final List<Token> tokens;
    private final String source;
    private int pos = 0;

    @Override
    public Token peek() {
      checkNotEmpty();
      return tokens.get(0);
    }

    @Override
    public Token next() {
      checkNotEmpty();
      return tokens.remove(0);
    }

    @Override
    public boolean hasNext() {
      return !tokens.isEmpty() || pos < source.length();
    }

    void checkNotEmpty(){
      while(tokens.isEmpty())
        doScan();
    }

    void doScan(){
      pos = handler.handle(tokens, source, pos);
    }

  }

  static Scanner scanner(String source){
    return new VmyScanner(source, getHandler());
  }

  static final VmyScanner SCANNER = new VmyScanner(getHandler());

  public static List<Token> scan(final String source){
    return scanner(source).scan(source);
  }

  static SourceStringHandler HANDLER;

  static interface SourceStringHandler {
    int handle(List<Token> tokens, final String source, final int start);
  }

  static abstract class BaseHandler implements SourceStringHandler {
    private BaseHandler next;
    public void setNext(BaseHandler next){
      this.next = next;
    }

    @Override
    public final int handle(List<Token> tokens, final String source, final int start){
      return canHandle(tokens, source, start) ? 
      doHandle(tokens, source, start) : Objects.nonNull(next) ? 
      next.handle(tokens, source, start) : 
      start;
    }

    protected abstract boolean canHandle(List<Token> tokens, final String source, final int start);
    protected abstract int doHandle(List<Token> tokens, final String source, final int start);

  }

  static final class OperatorHandler extends BaseHandler{

    @Override
    protected boolean canHandle(List<Token> tokens, String source, int start) {
//      return Identifiers.builtinIdentifiers.contains(Character.toString( source.charAt(start)));
      return Identifiers.operatorCharacters.contains(source.charAt(start)) || Identifiers.commonIdentifiers.contains(source.charAt(start));
    }

    @Override
    protected int doHandle(List<Token> tokens, String source, int start) {
      final StringBuilder identifierBuilder = new StringBuilder().append(source.charAt(start++));
      while(start < source.length() && Identifiers.operatorCharacters.contains(source.charAt(start)))
        identifierBuilder.append(source.charAt(start++));
      tokens.add(new Token(Token.Identifier, identifierBuilder.toString()));
      return start;
    }
  }

  static final class NumberHandler extends BaseHandler{

    @Override
    protected boolean canHandle(List<Token> tokens, String source, int start) {
      return Character.isDigit(source.charAt(start));
    }

    @Override
    protected int doHandle(List<Token> tokens, String source, int start) {
      final StringBuilder builder = new StringBuilder();
      int walk = start;
      boolean isfloat = false;
      while(
        walk < source.length() && 
        (Objects.equals('.', source.charAt(walk)) || Character.isDigit(source.charAt(walk)))
      ){
        builder.append(source.charAt(walk));
        if(Objects.equals('.', source.charAt(walk))) 
          isfloat = true;
        walk++;
      }
      tokens.add(
        new Token(
          isfloat ? Token.DOUBLE_V : Token.INT_V,
          builder.toString()
        )
      );
      return walk;
    }
  }

  static final class DefaultHandler extends BaseHandler {

    @Override
    protected boolean canHandle(List<Token> tokens, String source, int start) {
      return true;
    }

    @Override
    protected int doHandle(List<Token> tokens, String source, int start) {
      throw new LexcicalException("not support source");
    }
  }

  static final class BlackHandler extends BaseHandler {

    @Override
    protected boolean canHandle(List<Token> tokens, String source, int start) {
      return Objects.equals(' ', source.charAt(start));
    }

    @Override
    protected int doHandle(List<Token> tokens, String source, int start) {
      while(start < source.length() && Objects.equals(source.charAt(start), ' '))
        start++;
      return start;
    }
  }

  static final class DeclarationHandler extends  BaseHandler {
    @Override
    protected boolean canHandle(List<Token> tokens, String source, int start) {
      // let val
      final String maybeDeclaration = source.substring(start, start + 3);
      return Objects.equals(maybeDeclaration, Identifiers.ConstDeclaration) || Objects.equals(maybeDeclaration, Identifiers.VarDeclaration);
    }

    @Override
    protected int doHandle(List<Token> tokens, String source, int start) {
      // let val
      tokens.add(new Token(Token.Declaration, source.substring(start, start + 3)));
      return start + 3;
    }
  }


  static SourceStringHandler getHandler(){
    if(Objects.isNull(HANDLER)) 
      buildHandler();
    return HANDLER;
  }

  static void buildHandler(){
    HANDLER = new SimpleHandlerBuilder()
    .next(new NumberHandler())
    .next(new OperatorHandler())
    .next(new BlackHandler())
    .next(new DeclarationHandler())
    .next(new DefaultHandler())
    .build();
  }

  static class SimpleHandlerBuilder implements HandlerBuilder{
    private List<BaseHandler> handlers = new LinkedList<>();

    @Override
    public SourceStringHandler build() {
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

    @Override
    public HandlerBuilder next(BaseHandler handler) {
      handlers.add(handler);
      return this;
    }
  }

  static interface HandlerBuilder{
    SourceStringHandler build();
    HandlerBuilder next(BaseHandler handler);
  }

}
