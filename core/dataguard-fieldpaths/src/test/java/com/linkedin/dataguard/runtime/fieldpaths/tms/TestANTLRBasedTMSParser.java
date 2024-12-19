package com.linkedin.dataguard.runtime.fieldpaths.tms;

public class TestANTLRBasedTMSParser extends TestAbstractTMSParser {

  @Override
  protected TMSParser getParser() {
    return ANTLRBasedTMSParser.INSTANCE;
  }
}
