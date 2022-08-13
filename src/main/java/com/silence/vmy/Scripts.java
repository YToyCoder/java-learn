package com.silence.vmy;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

/**
 * vmy script support
 *
 * types of token need support :
 * 1. Literal : Int, Double, String, Char
 * 2. Comma : ,
 * 3. Black
 * 4. Declaration
 * 5. Identifier
 * 6. Operator
 */
public class Scripts {
  private Scripts() {}

  /**
   * run scripts
   * @param script_files files of script
   */
  public static void run(String[] script_files){
    for (String file_path : script_files)
      one_line_handle_support(file_path);
  }

  private static void one_line_handle_support(String file){
    AST.Evaluator evaluator = AST.evaluator(true);
    try {
      List<String> lines = Files.readAllLines(Path.of(file));
      for(String line : lines)
        Eval.eval(line, evaluator);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private static class FileInputScanner implements Scanner, AutoCloseable {

    public FileInputScanner(String file_path) throws FileNotFoundException {
      this.file_path = file_path;
      init(file_path);
    }

    /**
     * init resource
     * @param file_path
     * @throws FileNotFoundException
     */
    void init(String file_path) throws FileNotFoundException {
      origin = new RandomAccessFile(file_path, "rw");
      channel = origin.getChannel();
      buffer = ByteBuffer.wrap(new byte[1028]);
    }

    private ReadableByteChannel channel;
    private RandomAccessFile origin;
    private List<Token> tokens;
    private ByteBuffer buffer;
    private final String file_path;
    private int record;

    @Override
    public List<Token> scan(String source) {
      return null;
    }

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
      checkNotEmpty();
      return !tokens.isEmpty();
    }

    void checkNotEmpty(){
//      channel.read()
      if(hash_char()){
        switch (peek_char()){
          case '"': // string literal
            handle_string_literal();
            break;
          case Identifiers.SingleQuote:
            break;
          case '1':
          case '2':
          case '3':
          case '4':
          case '5':
          case '6':
          case '7':
          case '8':
          case '9':
          case '0':
            handle_digit_literal();
            break;
          case ':':
            tokens.add(new Token(Token.Comma, Identifiers.Comma, pos()));
            next_char();
            break;
          case ' ':
            handle_black();
          default:
            // identifier :
            if(Identifiers.identifiers.contains(peek_char()))
              handle_identifier_kind();
            if(
                Identifiers.operatorCharacters.contains(peek_char()) ||
                    Identifiers.commonIdentifiers.contains(peek_char())
            ) handle_operator();
        }
      }
    }

    int pos(){
      return 0;
    }

    /**
     * handle the string literal , like : "string literal"
     */
    void handle_string_literal(){
      record_position();
      char c = next_char();
      StringBuilder builder = new StringBuilder();
      while(
          hash_char() &&
              (
                  // not quote
                  !Utils.equal(peek_char(), Identifiers.Quote) &&
                      // not end of line
                  !is_end_of_line()
              )
      ) builder.append(next_char());
      if(!hash_char() || !Utils.equal(next_char(), Identifiers.Quote))
        throw new LexicalException(get_record(), file_path, "string literal has no closing parenthesis");
      tokens.add(new Token(Token.Literal, builder.toString(), get_record()));
    }

    /**
     * handle digit literal , like :
     *    1 , 2.0, 100
     */
    void handle_digit_literal(){
      final StringBuilder builder = new StringBuilder();
      record_position();
      while( hash_char() && Character.isDigit(peek_char()) )
        builder.append(next_char());
      if( hash_char() && Utils.equal(peek_char(), Identifiers.Dot)){
        // double
        builder.append(next_char());
        while( hash_char() && Character.isDigit(peek_char()) )
          builder.append(next_char());
      }
      tokens.add(new Token(Token.Literal, builder.toString(), get_record()));
    }

    /**
     * identifier things, variable name , function name or declaration
     */
    void handle_identifier_kind(){
      record_position();
      final StringBuilder builder = new StringBuilder();
      while(hash_char() && Identifiers.identifiers.contains(peek_char()))
        builder.append(next_char());
      final String identifier = builder.toString();
      tokens.add(
          new Token(
              get_identifier_kind(identifier),
              identifier,
              get_record()
          )
      );
    }

    void handle_operator(){
      record_position();
      final StringBuilder builder = new StringBuilder();
      while (hash_char() && Identifiers.operatorCharacters.contains(peek_char()))
        builder.append(next_char());
      final String operator = builder.toString();
      tokens.add(new Token(get_identifier_kind(operator), operator, get_record()));
    }

    int get_identifier_kind(String identifier){
      return switch (identifier){
        case /* let , val */Identifiers.ConstDeclaration, Identifiers.VarDeclaration -> Token.Declaration;
        case /* = */ Identifiers.Assignment -> Token.Assignment;
        default -> {
          if(Identifiers.builtinCall.contains(identifier)) yield Token.BuiltinCall;
          yield Token.Identifier;
        }
      };
    }

    /**
     * record current position
     */
    void record_position(){
      record = pos();
    }

    /**
     * get the recorded position
     * @see FileInputScanner#get_record()
     * @return recorded position
     */
    int get_record(){
      return record;
    }

    /**
     * remove the black between two token
     */
    void handle_black(){
      while(hash_char() && (Utils.equal(peek_char(), ' ')  || is_end_of_line() )){
        if (is_end_of_line())handle_end_of_line();
        else peek_char();
      }
    }

    boolean is_end_of_line(){
      return false;
    }

    void handle_end_of_line(){
    }

    boolean hash_char(){
      return false;
    }

    char peek_char(){
      return 'c';
    }

    char next_char(){
      return 'c';
    }

    @Override
    public void close() throws Exception {
      if(Objects.nonNull(origin))
        origin.close();
    }
  }
}
