package com.silence.vmy;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class Scanners {
  
  static class VmyScanner implements Scanner{
    private final SourceStringHandler handler;

    public VmyScanner(final SourceStringHandler handler){
      this.handler = handler;
    }

    @Override
    public List<Token> scan(final String source) {
      int walk = 0;
      List<Token> tokens = new LinkedList<>();
      while(walk < source.length()){
        walk = handler.handle(tokens, source, walk);
      }
      return tokens;
    }

  }

  static final VmyScanner SCANNER = new VmyScanner(getHandler());

  public static List<Token> scan(final String source){
    return SCANNER.scan(source);
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
      return Operators.buildinIdentifiers.contains(source.charAt(start));
    }

    @Override
    protected int doHandle(List<Token> tokens, String source, int start) {
      tokens.add(new Token(Token.OP, String.valueOf(source.charAt(start))));
      return start + 1;
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
      throw new RuntimeException("not support source");
    }
  }

  static final class BlackHandler extends BaseHandler {

    @Override
    protected boolean canHandle(List<Token> tokens, String source, int start) {
      return Objects.equals(' ', source.charAt(start));
    }

    @Override
    protected int doHandle(List<Token> tokens, String source, int start) {
      return start + 1;
    }
  }

  static int handleSource(List<Token> tokens, final String source, final int start){
    return getHandler().handle(tokens, source, start);
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
