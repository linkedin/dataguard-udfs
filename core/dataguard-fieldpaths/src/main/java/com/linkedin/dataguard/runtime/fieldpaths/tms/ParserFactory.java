package com.linkedin.dataguard.runtime.fieldpaths.tms;

import static com.linkedin.dataguard.runtime.fieldpaths.tms.ParserFactory.ParserType.*;


public final class ParserFactory {

  public enum ParserType {
    ANTLR
  }

  private ParserFactory() {
  }

  /**
   * Get a parser that understands TMS paths, and returns {@link ParsedTMSPath}
   * containing a list of path selectors.
   */
  public static TMSParser getParser() {
    // By default return ANTLR
    return getParser(ANTLR);
  }

  public static TMSParser getParser(ParserType type) {
    switch (type) {
      case ANTLR:
        return ANTLRBasedTMSParser.INSTANCE;
      default:
        throw new UnsupportedOperationException("Unknown parser type: " + type);
    }
  }
}
