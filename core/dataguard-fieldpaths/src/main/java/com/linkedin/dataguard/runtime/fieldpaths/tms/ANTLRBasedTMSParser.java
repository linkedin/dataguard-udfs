package com.linkedin.dataguard.runtime.fieldpaths.tms;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

import static com.linkedin.dataguard.runtime.fieldpaths.tms.ANTLRBasedTMSParser.ExceptionThrowingErrorListener.*;
import static java.lang.String.*;
import static java.util.Objects.*;


/**
 * Uses grammar rules defined in TMSPath.g4 to parse the TMS field paths described at
 * http://go/tms/schema.
 */
public final class ANTLRBasedTMSParser implements TMSParser {

  public static final ANTLRBasedTMSParser INSTANCE = new ANTLRBasedTMSParser();

  private ANTLRBasedTMSParser() {
  }

  @Override
  public ParsedTMSPath parsePath(String tmsPath) throws TMSParsingException {
    requireNonNull(tmsPath);
    TMSPathLexer lexer = new TMSPathLexer(CharStreams.fromString(tmsPath));
    TMSPathParser parser = new TMSPathParser(new CommonTokenStream(lexer));

    lexer.removeErrorListeners();
    lexer.addErrorListener(ERROR_HANDLER);
    parser.removeErrorListeners();
    parser.addErrorListener(ERROR_HANDLER);
    try {
        ParserRuleContext fieldPath = parser.tmsPath();
        return ParsedTMSPathBuilder.buildTMSPath(fieldPath);
    } catch (RuntimeException e) {
      throw new TMSParsingException(
          format("Failed to parse %s: %s", tmsPath, e.getMessage()),
          e.getCause());
    }
  }

  /**
   * ANTLR's default error handling only prints the exception, but does not throw an exception (e.g. bad tokens
   * like ',' will simply get ignored). We implement this error handler to make the errors more explicit.
   */
  static class ExceptionThrowingErrorListener extends BaseErrorListener {

    static final ExceptionThrowingErrorListener ERROR_HANDLER = new ExceptionThrowingErrorListener();

    @Override
    public void syntaxError(
        Recognizer<?, ?> recognizer,
        Object offendingSymbol,
        int line,
        int charPositionInLine,
        String msg,
        RecognitionException e) {
      // Copied from ANTLR's ConsoleErrorListener - but make it throw an exception instead
      throw new RuntimeException("line " + line + ":" + charPositionInLine + " " + msg, e);
    }
  }
}
