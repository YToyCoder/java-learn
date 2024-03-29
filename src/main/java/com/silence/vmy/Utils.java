package com.silence.vmy;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.BiPredicate;

public class Utils {
  public static MethodHandle getStaticMethod(
    Class<?> refc, 
    final String name, 
    Class<?> ...types
  ){

    MethodType type = MethodType.methodType(types[0]);
    type = type.appendParameterTypes(Arrays.copyOfRange(types, 1, types.length));
    MethodHandle lookupMethod = null;
    try {
      lookupMethod = lookup.findStatic(refc, name, type);
    } catch 
    (// NoSuchMethodException | IllegalAccessException | 
    Throwable e
    ) {
      return null;
    }
    return lookupMethod;

  }

  public static MethodHandle getOpsStaticMethod(final String name, Class<?> ...types){
    return getStaticMethod(BinaryOps.class, name, types);
  }

  static final MethodHandles.Lookup lookup = MethodHandles.lookup();

  // a mark that represent can be called recursive
  // @see
  static interface Recursive{}

  static class RecursiveException extends RuntimeException{
    public RecursiveException(String msg){
      super(msg);
    }
  }

  // if two obj is equal
  // a == b
  public static <T extends Ordering<T>> boolean eq(T a , T b){
    return a.compare(b) == 0;
  }

  // like : a > b
  public static <T extends Ordering<T>> boolean gt(T a , T b){
    return a.compare(b) > 0;
  }

  // like : a < b
  public static <T extends Ordering<T>> boolean lt(T a , T b){
    return a.compare(b) < 0;
  }

  static enum Order {
    // 5 level : 1 -> 5
    One(1),
    Two(2),
    Three(3),
    Four(4),
    Five(5);

    Order(int _level){
      level = _level;
    }

    private final int level;

    public int level(){
      return level;
    }
  }

  public static  boolean isQuote(char c){
    return equal(c, Identifiers.Quote);
  }
  public static boolean equal(Object a, Object b){
    return Objects.equals(a, b);
  }

  // if from start is EOL return the start of next line
  // else return -1
  public static int EOL(String source, int start){
    return start + 1 < source.length() && equal(source.charAt(start), '\r') && equal(source.charAt(start + 1), '\n') ?
        start + 2 : equal(source.charAt(start), '\n') ?
        start + 1 :
        -1;
  }

  public static boolean isEOL(String source, int start){
    return EOL(source, start) != -1;
  }
  public static boolean isEOL(Token token) {
    return
        Objects.nonNull(token) &&
        (Objects.equals(token.value, "\n") || Objects.equals(token.value, "\r\n"));
  }

  public static boolean isType(Runtime.Variable head, VmyType type){
    return head.getType().equals(type);
  }

  public static VmyType to_type(String type_name){
    return VmyTypes.BuiltinType.valueOf(type_name);
  }

  public static VmyType get_obj_type(Object obj){

    if(obj instanceof Runtime.VariableWithName obj_variable)
      return obj_variable.getType();
    else if(obj instanceof String)
      return VmyTypes.BuiltinType.String;
    else if(obj instanceof Character)
      return VmyTypes.BuiltinType.Char;
    else if(obj instanceof Integer)
      return VmyTypes.BuiltinType.Int;
    else if(obj instanceof Boolean)
      return VmyTypes.BuiltinType.Boolean;
    else if(obj instanceof Double)
      return VmyTypes.BuiltinType.Double;
    else
      throw new VmyRuntimeException("current version not support this type");

  }

  public static boolean is_mutable(String string){

    return switch (string) {
      case Identifiers.ConstDeclaration -> false;
      case Identifiers.VarDeclaration -> true;
      default -> throw new RuntimeException(string + " is not valid declaration identifier!");
    };

  }

  public static 
  Runtime.VariableWithName variable_with_name(
    String name, 
    Runtime.Variable variable
  ){
    return new Runtime.VariableWithName() {

      @Override
      public String name() {
        return name;
      }

      @Override
      public VmyType getType() {
        return variable.getType();
      }

      @Override
      public Object getValue() {
        return variable.getValue();
      }

      @Override
      public void setValue(Object value) {
        variable.setValue(value);
      }

      @Override
      public boolean mutable() {
        return variable.mutable();
      }
    };

  }

  // compare two function type
  public static int function_type_compare(
    FunctionSupport.FunctionType a, 
    FunctionSupport.FunctionType b
  ){

    return concat_type_to_string(a.types()).compareTo(concat_type_to_string(b.types()));

  }

  private static String concat_type_to_string(List<VmyType> types){

    return types.stream()
        .map(VmyType::toString)
        .reduce(String::concat)
        .orElse("");
  }

  public static String function_to_string(String name, FunctionSupport.FunctionType type){

    String params = "";
    List<VmyType> types = type.types();
    for(int i=1; i<types.size(); i++){
      params += type.param_type(i).toString();
    }

    return String.format(
      "%s(%s) : %s",
      name, 
      params, 
      type.types().size() > 0 ? type.param_type(0) : "any"
    );

  }

  public static void log(String msg){
    System.out.println("[vmy-info] " + msg);
  }

  public static void warning(String msg){
    System.out.println("[vmy-warning] " + msg);
  }

  public static void error(String msg) {
    System.err.println("[vmy-error]" + msg);
  }

  /**
   * convert \n and \r\n to \\n , \\r\\n
   * @param string
   * @return
   */
  public static String display_newline(String string) {
    return string
      .replace("\n", "\\n")
      .replace("\r", "\\r");
  }

  public static 
  BiPredicate<Token, Token> next_two_token_should_not_be_empty_parenthesis_for_token(
      Token token,
      String error_msg
  ){

    return (n, nn) -> {
      if(/* check it is not "while()"*/
          Objects.equals(Identifiers.OpenParenthesis, n.value) &&
          !Objects.equals(Identifiers.ClosingParenthesis, nn.value)
      ) return true;
      throw new ASTProcessingException(String.format( 
        error_msg + " (token start position %d)", 
        token.pos
      ));
    };

  }

}
