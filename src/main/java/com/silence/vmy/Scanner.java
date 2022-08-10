package com.silence.vmy;

import java.util.List;

public interface Scanner {
  @Deprecated
  List<Token> scan(final String source);

  /**
   * preview the next token
   * @return
   */
  Token peek();

  /**
   * get the next token
   * @return
   */
  Token next();

  /**
   * check if there is next token
   * @return
   */
  boolean hasNext();

}
