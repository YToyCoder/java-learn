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

  /**
   * Base Class for SourceStringHandler
   * using ChainOfResponsibility Pattern
   */
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

  /**
   * <p>设置执行优先级</p>
   * <P>如果{@code Utils#gt(a, b) == true}, b将会在a后面执行, 反之也是</P>
   * <P>如果{@code Utils#eq(a, b) == true}, b,a执行顺序不定</P>
   */
  static abstract class OrderedHandler extends BaseHandler implements Ordering<OrderedHandler> {
    private int order;

    public OrderedHandler(int _order){
      order = _order;
    }

    @Override
    final public int compare(OrderedHandler other) {
      return order - other.order;
    }
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
      final String identifier_string = identifierBuilder.toString();
      tokens.add(new Token( Objects.equals(identifier_string, "=") ? Token.Assignment : Token.Identifier, identifier_string));
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
      final String maybeDeclaration;
//      = source.substring(start, start + 3);
      return source.length() - start >= 3 &&
          (
              Objects.equals(maybeDeclaration = source.substring(start, start + 3), Identifiers.ConstDeclaration) ||
              Objects.equals(maybeDeclaration, Identifiers.VarDeclaration)
          );
    }

    @Override
    protected int doHandle(List<Token> tokens, String source, int start) {
      // let | val
      tokens.add(new Token(Token.Declaration, source.substring(start, start + 3)));
      return start + 3;
    }
  }

  // get the identifier like :
  // a_name, b_name
  static final class IdentifierHandler extends OrderedHandler{
    public IdentifierHandler() {
      super(Utils.Order.Three.level());
    }

    @Override
    protected boolean canHandle(List<Token> tokens, String source, int start) {
      return
          Identifiers.identifiers.contains(source.charAt(start)) ||
          Objects.equals(source.charAt(start), Identifiers.Quote); // support string
    }

    @Override
    protected int doHandle(List<Token> tokens, String source, int start) {
      if(Utils.isQuote(source.charAt(start))){
        // handle string like : "string"
        final String string_literal = extractString(source, start);
        tokens.add(new Token(Token.Literal, string_literal));
        return start + string_literal.length() + 1;
      }
      final int init_pos = start;
      while(start < source.length() && Identifiers.identifiers.contains(source.charAt(start)))
        start++;
      tokens.add(new Token(Token.Identifier, source.substring(init_pos, start)));
      return start;
    }

    private String extractString(String source, int start){
      final int init_pos = ++start;
      while(start < source.length() && !Utils.isQuote(source.charAt(start)))
        start++;
      return source.substring(init_pos, start);
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
    .next(new IdentifierHandler())
    .next(new OperatorHandler())
    .next(new BlackHandler())
//        .next(new )
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
