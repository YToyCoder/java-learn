package com.silence.vmy;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TokenRecorder {

  @Test
  public void one_capability_token_recorder_test(){
    OneCapabilityTokenRecorder token_recorder = new OneCapabilityTokenRecorder();
    assertFalse("init recorder", token_recorder.has_history());
    assertThrows(RecordHistoryEmptyException.class, () -> {
      token_recorder.last();
    });
    token_recorder.record_to_history(new Token(0, ""));
    assertTrue("add one token", token_recorder.has_history());
  }
}
