package com.linkedin.dataguard.runtime.fieldpaths.tms;

import org.antlr.v4.runtime.atn.ATNDeserializer;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;


public class TestVersion {

  @Test
  public void testAntlrVersion() throws Exception {
    assertThat(ATNDeserializer.SERIALIZED_VERSION).isEqualTo(4);
    // Assert deserialization is successful
    assertThat(TMSPathLexer._ATN).isNotNull();
  }
}
