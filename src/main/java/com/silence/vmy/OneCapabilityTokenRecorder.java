package com.silence.vmy;

import java.util.Objects;

public class OneCapabilityTokenRecorder implements TokenHistoryRecorder{
  private Token the_one;

  @Override
  public TokenHistoryRecorder record_to_history(Token token) {
    the_one = Objects.requireNonNull(token);
    return this;
  }

  @Override
  public Token last() {
    if(!has_history())
      throw new RecordHistoryEmptyException("");
    return the_one;
  }

  @Override
  public boolean has_history() {
    return Objects.nonNull(the_one);
  }
  
}
