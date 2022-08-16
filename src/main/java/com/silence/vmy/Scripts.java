package com.silence.vmy;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
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

  public static FileInputScanner file_scanner(String file) throws FileNotFoundException {
    return new FileInputScanner(file);
  }

  /**
   * a new version handler to handle the file by NIO
   */
  public static class FileInputScanner implements Scanner, AutoCloseable {

    public FileInputScanner(String file_path) throws FileNotFoundException {
      this.file_path = file_path;
      init(file_path);
    }

    /**
     * init resource
     * @param file_path
     * @throws FileNotFoundException
     */
    private void init(String file_path) throws FileNotFoundException {
      origin = new RandomAccessFile(file_path, "rw");
      channel = origin.getChannel();
      buffer = ByteBuffer.wrap(new byte[1028]);
      buffer.flip();
      pos = 0;
      cs = new LinkedList<>();
      tokens = new LinkedList<>();
    }

    private ReadableByteChannel channel;
    private RandomAccessFile origin;
    private List<Token> tokens;
    private ByteBuffer buffer;
    private final String file_path;
    private int pos;
    private int record;
    private LinkedList<Character> cs;
    private boolean end_of_file;

    @Override
    public List<Token> scan(String source) {
      List<Token> tos = new LinkedList<>();
      while(hasNext())
        tos.add(next());
      return tos;
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


    /**
     * check if the token list is empty, if empty and has char , then add new token to token list
     */
    private void checkNotEmpty() {
      while(has_char() && tokens.isEmpty())
        do_fill_tokens();
    }

    /**
     * fill the tokens
     */
    private void do_fill_tokens(){
//      channel.read()
      if(has_char()){
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
          case ' ':
            handle_black();
            break;
          case Identifiers.OpenBraceChar:
          case Identifiers.ClosingBraceChar:
          case ',': // Comma
          case '(':
//          case ':':
            handle_single_char_identifier();
            break;
          default:
            // identifier :
            if(Identifiers.identifiers.contains(peek_char()))
              handle_identifier_kind();
            else if(is_end_of_line())
              handle_end_of_line();
            else if(
                Identifiers.operatorCharacters.contains(peek_char()) ||
                    Identifiers.commonIdentifiers.contains(peek_char())
            ) handle_operator();
            else
              throw new LexicalException(pos(), file_path, "can't handle char : " + peek_char() + " at position " + pos);
        }
      }
    }

    /**
     * current offset from first word
     * @return
     */
    private int pos(){
      return pos;
    }

    /**
     * handle the string literal , like : "string literal"
     */
    private void handle_string_literal() {
      record_position();
      next_char(); // remove "
      StringBuilder builder = new StringBuilder();
      while(
          has_char() &&
                  // not quote
                  !Utils.equal(peek_char(), Identifiers.Quote) &&
                      // not end of line
                  !is_end_of_line()
      ) builder.append(next_char());
      if(!has_char() || !Utils.equal(next_char(), Identifiers.Quote)) /* try to remove " */
        throw new LexicalException(get_record(), file_path, "string literal has no closing parenthesis");
      tokens.add(new Token(Token.Literal, builder.toString(), get_record()));
    }

    /**
     * handle digit literal , like :
     *    1 , 2.0, 100
     */
    private void handle_digit_literal() {
      final StringBuilder builder = new StringBuilder();
      record_position();
      while( has_char() && Character.isDigit(peek_char()) )
        builder.append(next_char());
      if( has_char() && Utils.equal(peek_char(), Identifiers.Dot)){
        // double
        builder.append(next_char());
        while( has_char() && Character.isDigit(peek_char()) )
          builder.append(next_char());
      }
      tokens.add(new Token(Token.Literal, builder.toString(), get_record()));
    }

    /**
     * identifier things, variable name , function name or declaration
     */
    private void handle_identifier_kind() {
      record_position();
      final StringBuilder builder = new StringBuilder();
      while(has_char() && Identifiers.identifiers.contains(peek_char()))
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

    private void handle_operator() {
      record_position();
      final StringBuilder builder = new StringBuilder();
      while (
          has_char() &&
              (
                  Identifiers.operatorCharacters.contains(peek_char()) ||
                      Identifiers.commonIdentifiers.contains(peek_char())
              )
      ) builder.append(next_char());
      final String operator = builder.toString();
      tokens.add(new Token(get_identifier_kind(operator), operator, get_record()));
    }

    private int get_identifier_kind(String identifier){
      return switch (identifier){
        case /* let , val */Identifiers.ConstDeclaration, Identifiers.VarDeclaration -> Token.Declaration;
        case /* = */ Identifiers.Assignment -> Token.Assignment;
        case /* while */ Identifiers.While -> Token.Builtin;
        case /* true false */ Identifiers.True, Identifiers.False -> Token.Literal;
        default -> {
          if(Identifiers.builtinCall.contains(identifier)) yield Token.BuiltinCall;
          yield Token.Identifier;
        }
      };
    }

    /**
     * handle builtin character which is single character
     */
    private void handle_single_char_identifier(){
      tokens.add(new Token(Token.Identifier, Character.toString(next_char()), pos()));
    }

    /**
     * record current position
     */
    private void record_position(){
      record = pos();
    }

    /**
     * get the recorded position
     * @see FileInputScanner#get_record()
     * @return recorded position
     */
    private int get_record(){
      return record;
    }

    /**
     * remove the black between two token
     */
    private void handle_black() {
      while(has_char() && Utils.equal(peek_char(), ' ')  ){
        next_char();
      }
    }

    private boolean is_end_of_line(){
      if(has_char() && Utils.equal( cs.peek() , '\n'))
        return true;
      // check if next two char is "\r\n"
      char next_char = next_char(); // move out
      boolean end_of_line = (Utils.equal(next_char, '\r') && Utils.equal(peek_char(), '\n'));
      cs.addFirst(next_char); // set back
      return end_of_line;
    }

    private void handle_end_of_line(){
      record_position();
      StringBuilder builder = new StringBuilder();
      if(!Utils.equal(peek_char() /* may be '\n' or '\r\n'*/, '\n')){
        builder.append(next_char()).append(next_char());
      }else builder.append(next_char());
      tokens.add(new Token(Token.NewLine, builder.toString(), pos()));
    }

    private boolean has_char() {
      if(!cs.isEmpty() || buffer.hasRemaining())
        return true;
      if(end_of_file) /* already at end_of_file */
        return false;
      //
      buffer.clear();

      try {
        end_of_file = channel.read(buffer) == 0;
      }catch (IOException e){
        e.printStackTrace();
        throw new LexicalException(pos(), file_path, e.getMessage());
      }
      buffer.flip();
      return buffer.hasRemaining();
    }

    private char peek_char(){
      check_and_set_char();
      return cs.peek();
    }

    private char next_char(){
      check_and_set_char();
      pos++;
      return cs.pop();
    }

    /**
     * call this after @see hash_char
     */
    private void check_and_set_char(){
      while(cs.isEmpty())
        cs.add((char) buffer.get());
    }

    @Override
    public void close() throws Exception {
      if(Objects.nonNull(origin))
        origin.close();
    }
  }
}
