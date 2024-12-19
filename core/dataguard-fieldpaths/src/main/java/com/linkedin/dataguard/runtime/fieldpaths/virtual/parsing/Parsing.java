package com.linkedin.dataguard.runtime.fieldpaths.virtual.parsing;

import com.linkedin.dataguard.runtime.fieldpaths.virtual.syntaxtree.FieldPathNode;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.syntaxtree.FieldPathSyntaxTreeBuilder;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.syntaxtree.FieldReferenceExpression;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.syntaxtree.RowSelectorAwareFieldPath;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

import static java.lang.String.*;
import static java.util.Objects.*;


/**
 * A helper class that contains utility methods around parsing a virtual field path.
 */
public final class Parsing {

  private Parsing() {
  }

  public static RowSelectorAwareFieldPath parseMountedPath(String input) throws ParsingException {
     return (RowSelectorAwareFieldPath) parsePath(input, GrammarElement.MOUNTED_PATH);
  }

  public static RowSelectorAwareFieldPath parseRowSelectorAwareFieldPath(String input) throws ParsingException {
    return (RowSelectorAwareFieldPath) parsePath(input, GrammarElement.ROW_SELECTOR_AWARE_FIELD_PATH);
  }

  public static FieldReferenceExpression parseFieldPath(String input) throws ParsingException {
    return (FieldReferenceExpression) parsePath(input, GrammarElement.FIELD_PATH);
  }

  public enum GrammarElement {

    MOUNTED_PATH("fullPathWithMountPoint", VirtualFieldPathParser::mountedPath),
    ROW_SELECTOR_AWARE_FIELD_PATH("rowSelectorAwareFieldPath", VirtualFieldPathParser::rowSelectorAwareFieldPath),
    FIELD_PATH("virtualFieldPath", VirtualFieldPathParser::virtualFieldPath);

    private final String name;
    private final GrammarElementFunction grammarElementParsingFunction;

    GrammarElement(String name, GrammarElementFunction grammarElementParsingFunction) {
      this.name = name;
      this.grammarElementParsingFunction = grammarElementParsingFunction;
    }

    public String getName() {
      return name;
    }

    public ParserRuleContext extract(VirtualFieldPathParser parser) {
      return grammarElementParsingFunction.apply(parser);
    }
  }

  interface GrammarElementFunction {
    ParserRuleContext apply(VirtualFieldPathParser parser);
  }

  public static FieldPathNode parsePath(String input, GrammarElement grammarElement) {
    requireNonNull(input);
    VirtualFieldPathLexer lexer = new VirtualFieldPathLexer(CharStreams.fromString(input));
    VirtualFieldPathParser parser = new VirtualFieldPathParser(new CommonTokenStream(lexer));

    parser.removeErrorListeners();
    parser.addErrorListener(new ParserErrorListener());
    try {
      ParserRuleContext grammarElementContext = grammarElement.extract(parser);
      return grammarElementContext.accept(new FieldPathSyntaxTreeBuilder());
    } catch (RuntimeException e) {
      throw new ParsingException(
          format("Failed to parse %s to extract %s: %s", input, grammarElement.getName(), e.getMessage()),
          e.getCause());
    }
  }

  public static class ParserErrorListener extends BaseErrorListener {

    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine,
        String msg, RecognitionException e) {
      // Copied from ANTLR's ConsoleErrorListener - but make it throw RTE
      throw new RuntimeException("line " + line + ":" + charPositionInLine + " " + msg, e);
    }
  }
}
