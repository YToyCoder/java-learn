package com.silence.vmy;

import java.util.List;

public interface Scanner {
  List<Token> scan(final String source);
}
