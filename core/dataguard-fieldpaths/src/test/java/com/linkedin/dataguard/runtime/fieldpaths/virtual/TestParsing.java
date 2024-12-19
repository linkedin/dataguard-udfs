package com.linkedin.dataguard.runtime.fieldpaths.virtual;

import com.google.common.collect.ImmutableList;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.parsing.ParsingException;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.syntaxtree.BinaryArithmeticExpression;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.syntaxtree.BinaryArithmeticOperator;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.syntaxtree.BinaryLogicalOperator;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.syntaxtree.BinaryLogicalOperatorExpression;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.syntaxtree.BooleanLiteral;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.syntaxtree.CollectionLookupReferenceExpression;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.syntaxtree.CollectionPredicateReferenceExpression;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.syntaxtree.ComparisonExpression;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.syntaxtree.ComparisonOperator;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.syntaxtree.DecimalLiteral;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.syntaxtree.FieldPathPrinter;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.syntaxtree.FieldReferenceExpression;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.syntaxtree.FunctionCall;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.syntaxtree.Identifier;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.syntaxtree.IntegerLiteral;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.syntaxtree.MembershipOperatorExpression;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.syntaxtree.NotExpression;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.syntaxtree.PredicateCurrentVariable;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.syntaxtree.RowSelectorAwareFieldPath;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.syntaxtree.StringLiteral;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static com.linkedin.dataguard.runtime.fieldpaths.virtual.ExpressionBuilderUtil.*;
import static com.linkedin.dataguard.runtime.fieldpaths.virtual.parsing.Parsing.*;
import static com.linkedin.dataguard.runtime.fieldpaths.virtual.syntaxtree.BinaryArithmeticOperator.*;
import static com.linkedin.dataguard.runtime.fieldpaths.virtual.syntaxtree.ComparisonOperator.*;
import static com.linkedin.dataguard.runtime.fieldpaths.virtual.syntaxtree.ContextVariable.*;
import static com.linkedin.dataguard.runtime.fieldpaths.virtual.syntaxtree.MembershipOperator.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;


public class TestParsing {

  private static final PredicateCurrentVariable PREDICATE_CURRENT_VARIABLE = new PredicateCurrentVariable();

  @Test
  public void testFieldPathParsing() {

    Assertions.assertEquals(parseFieldPath("$"), CONTEXT_VARIABLE);
    Assertions.assertEquals(parseFieldPath("@"), PREDICATE_CURRENT_VARIABLE);
    Assertions.assertEquals(parseFieldPath("$.p"), structMember(CONTEXT_VARIABLE, "p"));

    // Assume p is of type array(row(x varchar,...))
    Assertions.assertEquals(parseFieldPath("$.p"), structMember(CONTEXT_VARIABLE, "p"));
    Assertions.assertEquals(parseFieldPath("$.p[?(@.x == 'a')]"),
        applyFilter(
            structMember(CONTEXT_VARIABLE, "p"),
            new ComparisonExpression(
                structMember(PREDICATE_CURRENT_VARIABLE, "x"),
                new StringLiteral("a"),
                EQUALS)));

    // Test utf-8 strings. '测试' stands for 'test' in Chinese
    Assertions.assertEquals(
        parseFieldPath("$.p[?(@.x == '测试')]"),
        new CollectionPredicateReferenceExpression(
            structMember(CONTEXT_VARIABLE, "p"),
            new ComparisonExpression(
                structMember(PREDICATE_CURRENT_VARIABLE, "x"),
                new StringLiteral("测试"),
                EQUALS)));

    // Assume y has ARRAY[VARCHAR] type
    Assertions.assertEquals(parseFieldPath("$.y[5]"),
        collectionLookup(structMember(CONTEXT_VARIABLE, "y"), 5));

    Assertions.assertEquals(parseFieldPath("$.y[?(@ != 'k' || 5 < 6)][5]"),
        collectionLookup(
            applyFilter(structMember(CONTEXT_VARIABLE, "y"),
            new BinaryLogicalOperatorExpression(
                new ComparisonExpression(PREDICATE_CURRENT_VARIABLE, new StringLiteral("k"), NOT_EQUALS),
                new ComparisonExpression(new IntegerLiteral("5"), new IntegerLiteral("6"), LT),
                BinaryLogicalOperator.OR)),
            5));

    // z has ROW(f1 VARCHAR, f2 VARCHAR) type

    Assertions.assertEquals(parseFieldPath("$.z.f1"),
        structMember(structMember(CONTEXT_VARIABLE, "z"), "f1"));

    // w is Array(row(f1 varchar, f2 varchar))

    Assertions.assertEquals(parseFieldPath("$.w[:].f1"),
        structMember(collectionStream(structMember(CONTEXT_VARIABLE, "w")), "f1"));

    Assertions.assertEquals(parseFieldPath("$.w[5].f1"),
        structMember(collectionLookup(structMember(CONTEXT_VARIABLE, "w"), 5), "f1"));

    Assertions.assertEquals(parseFieldPath("$.w[?(@.f1 == 'yth' && @.f2 == 'tg')][:].f1"),
        structMember(
            collectionStream(
                applyFilter(
                    structMember(CONTEXT_VARIABLE, "w"),
                    new BinaryLogicalOperatorExpression(
                        new ComparisonExpression(structMember(PREDICATE_CURRENT_VARIABLE, "f1"),
                            new StringLiteral("yth"), EQUALS),
                        new ComparisonExpression(structMember(PREDICATE_CURRENT_VARIABLE, "f2"),
                            new StringLiteral("tg"), EQUALS),
                        BinaryLogicalOperator.AND))),
            "f1"));

    // v is Array(row(
    //            f1 row(f2 varchar, f3 varchar),
    //            f4 array(row(f5 varchar)))

    Assertions.assertEquals(
        parseFieldPath("$.v[?(@.f1.f2 == 'cc')][:].f4[?(!(@ == 't'))][:].f5"),
        structMember(
            collectionStream(
                applyFilter(
                  structMember(
                      collectionStream(
                          applyFilter(
                              structMember(CONTEXT_VARIABLE, "v"),
                              new ComparisonExpression(
                                  structMember(structMember(PREDICATE_CURRENT_VARIABLE, "f1"), "f2"),
                                  new StringLiteral("cc"), EQUALS))),
                          "f4"),
                    new NotExpression(new ComparisonExpression(
                        PREDICATE_CURRENT_VARIABLE, new StringLiteral("t"), EQUALS)))),
            "f5"));

    Assertions.assertEquals(parseFieldPath("$.p[?((1-2)*3==4)]"),
        applyFilter(
            structMember(CONTEXT_VARIABLE, "p"),
            new ComparisonExpression(
                new BinaryArithmeticExpression(
                    new BinaryArithmeticExpression(new IntegerLiteral("1"), new IntegerLiteral("2"), SUBTRACTION),
                    new IntegerLiteral("3"),
                    MULTIPLICATION),
                new IntegerLiteral("4"),
                EQUALS)));

    // validate decimal literals, integer literals, and binary operators
    Assertions.assertEquals(parseFieldPath("$.a[?(1 + 2 - 3 * 4 % 5 / 6 <= -78.9)]"),
        applyFilter(
            structMember(
                CONTEXT_VARIABLE, "a"),
            new ComparisonExpression(
                new BinaryArithmeticExpression(
                    new BinaryArithmeticExpression(
                        new IntegerLiteral("1"),
                        new IntegerLiteral("2"),
                        BinaryArithmeticOperator.ADDITION),
                    new BinaryArithmeticExpression(
                        new BinaryArithmeticExpression(
                            new BinaryArithmeticExpression(
                                new IntegerLiteral("3"),
                                new IntegerLiteral("4"),
                                BinaryArithmeticOperator.MULTIPLICATION),
                            new IntegerLiteral("5"),
                            BinaryArithmeticOperator.MODULO),
                        new IntegerLiteral("6"),
                        BinaryArithmeticOperator.DIVISION),
                    BinaryArithmeticOperator.SUBTRACTION),
                new DecimalLiteral("-78.90"),
                ComparisonOperator.LTE)));

    // validate logical literals
    Assertions.assertEquals(parseFieldPath("$.a[?(true || !(false))]"),
        applyFilter(
            structMember(
                CONTEXT_VARIABLE,
                "a"),
            new BinaryLogicalOperatorExpression(
                new BooleanLiteral("true"),
                new NotExpression(
                    new BooleanLiteral("false")),
                BinaryLogicalOperator.OR)));

    Assertions.assertEquals(parseFieldPath("$.p[1]"),
        new CollectionLookupReferenceExpression(
            structMember(CONTEXT_VARIABLE, "p"), new IntegerLiteral("1")));

    Assertions.assertEquals(parseFieldPath("$.p[:]"),
        collectionStream(structMember(CONTEXT_VARIABLE, "p")));

    Assertions.assertEquals(parseFieldPath("$.x[?(@.y == 5)][:].z[1]"),
        collectionLookup(
            structMember(
                collectionStream(
                    applyFilter(
                        structMember(CONTEXT_VARIABLE, "x"),
                        new ComparisonExpression(
                            structMember(PREDICATE_CURRENT_VARIABLE, "y"),
                            new IntegerLiteral("5"), EQUALS))),
              "z"),
            1));

    Assertions.assertEquals(parseFieldPath("$.x[getFDSIndex1D($.c1.f1, 'filtervalue')]"),
        collectionLookup(
            structMember(CONTEXT_VARIABLE, "x"),
            new FunctionCall(
                new Identifier("getFDSIndex1D"),
                ImmutableList.of(
                    structMember(structMember(CONTEXT_VARIABLE, "c1"), "f1"),
                    new StringLiteral("filtervalue")))));

    Assertions.assertEquals(
        parseRowSelectorAwareFieldPath("[?(callisto__getPurposeForPagekey($.c1.f1) == 'foo')]$"),
        new RowSelectorAwareFieldPath(
            Optional.of(
                new ComparisonExpression(
                    new FunctionCall(
                        new Identifier("callisto__getPurposeForPagekey"),
                        ImmutableList.of(structMember(structMember(CONTEXT_VARIABLE, "c1"), "f1"))),
                    new StringLiteral("foo"),
                    EQUALS)),
            CONTEXT_VARIABLE));
  }

  @Test
  public void testTSARSecondarySchemas() {
    String secondarySchemaFieldPath = "$.entityFeatures[:]"
        + ".intFeatures[?(@.featureId==($.featureMetadata[?(@.name=='age')][0].featureId))][:]"
        + ".value";
    FieldReferenceExpression expectedFieldPathTree =
        structMember(
            collectionStream(
                applyFilter(
                    structMember(
                        collectionStream(structMember(CONTEXT_VARIABLE, "entityFeatures")),
                        "intFeatures"),
                    new ComparisonExpression(
                        structMember(PREDICATE_CURRENT_VARIABLE, "featureId"),
                        structMember(
                            collectionLookup(
                                applyFilter(
                                    structMember(CONTEXT_VARIABLE, "featureMetadata"),
                                    new ComparisonExpression(
                                        structMember(PREDICATE_CURRENT_VARIABLE, "name"),
                                        new StringLiteral("age"),
                                        EQUALS)),
                                0),
                        "featureId"),
                        EQUALS))),
            "value");

    Assertions.assertEquals(parseFieldPath(secondarySchemaFieldPath), expectedFieldPathTree);

    String mountPoint = "[mount_point=([?($.grouperContext.a == 'a' && $.grouperContext.b == 'b')])]";
    String mountedPathExpectedFromDatahub = mountPoint + secondarySchemaFieldPath;
    Assertions.assertEquals(parseMountedPath(mountedPathExpectedFromDatahub),
        new RowSelectorAwareFieldPath(
            Optional.of(
                new BinaryLogicalOperatorExpression(
                    new ComparisonExpression(
                        structMember(structMember(CONTEXT_VARIABLE,"grouperContext"), "a"),
                        new StringLiteral("a"),
                        EQUALS),
                    new ComparisonExpression(
                        structMember(structMember(CONTEXT_VARIABLE,"grouperContext"), "b"),
                        new StringLiteral("b"),
                        EQUALS),
                    BinaryLogicalOperator.AND)),
            expectedFieldPathTree));
  }

  @Test
  public void testUnsupportedFieldPaths() {
    assertThatThrownBy(() -> parseFieldPath("$xyz")).isInstanceOf(RuntimeException.class);
    assertThatThrownBy(() -> parseFieldPath("xyz")).isInstanceOf(RuntimeException.class);
    assertThatThrownBy(() -> parseFieldPath("1")).isInstanceOf(RuntimeException.class);
    assertThatThrownBy(() -> parseFieldPath("!")).isInstanceOf(RuntimeException.class);
    assertThatThrownBy(() -> parseFieldPath("$.xyz($ == 5)")).isInstanceOf(RuntimeException.class);
    assertThatThrownBy(() -> parseFieldPath("$.[1]")).isInstanceOf(RuntimeException.class);
    assertThatThrownBy(() -> parseFieldPath("$.[:]")).isInstanceOf(RuntimeException.class);
    assertThatThrownBy(() -> parseFieldPath("$.xyz(?)")).isInstanceOf(RuntimeException.class);
    assertThatThrownBy(() -> parseFieldPath("$.xyz[5 + 7]")).isInstanceOf(RuntimeException.class);
    assertThatThrownBy(() -> parseFieldPath("$.xyz[@ == 5]")).isInstanceOf(RuntimeException.class);
    assertThatThrownBy(() -> parseFieldPath("$.xyz(?@ == 5)")).isInstanceOf(RuntimeException.class);
    assertThatThrownBy(() -> parseFieldPath("$.x + $.y")).isInstanceOf(RuntimeException.class);
    assertThatThrownBy(() -> parseFieldPath("$.x.'k'")).isInstanceOf(RuntimeException.class);
    assertThatThrownBy(() -> parseFieldPath("'f'.k")).isInstanceOf(RuntimeException.class);
    assertThatThrownBy(() -> parseFieldPath("$.y?(@.f == null)")).isInstanceOf(RuntimeException.class);
  }

  @Test
  public void testFieldPathRoundTripForWellFormattedInput() {
      validateRoundTrip("$");
      validateRoundTrip("$.k[?(@.x == 'a')]");
      validateRoundTrip("$.x");
      validateRoundTrip("$.x[?(@ != 'a')]");
      validateRoundTrip("$.y[5]");
      validateRoundTrip("$.y[?((@ != 'k') || (5 < 6))][5]");
      validateRoundTrip("$.y[?(@ >= 'b')][?(8 > 7 + 4)]");
      validateRoundTrip("$.z[?(@.f1 == 'y')]");
      validateRoundTrip("$.z.f1");
      validateRoundTrip("$.w[:].f1");
      validateRoundTrip("$.w[5].f2");
      validateRoundTrip("$.w[?((@.f1 == 'yth') && (@.f2 == 'tg'))][:].f1");
      validateRoundTrip("$.v[?(@.f1.f2 == 'cc')][:].f4[:].f5");
      validateRoundTrip("$.v[?(@.f1.f2 == 'cc')][:].f4[:].f5[?(!(@ == 't'))]");
      validateRoundTrip("$.a[?(1 + 2 - 3 * 4 % 5 / 6 <= -78.9)]");
      validateRoundTrip("$.a[?((true) || (false))]");
  }

  @Test
  public void testMountedPathParsing() {
    // Mount point is empty
    Assertions.assertEquals(parseMountedPath("[mount_point=()]$.y"),
        new RowSelectorAwareFieldPath(Optional.empty(),
            structMember(CONTEXT_VARIABLE, "y")));

    // Mount point contains row-selector only.
    Assertions.assertEquals(parseMountedPath("[mount_point=([?($.x == 2)])]$.y"),
        new RowSelectorAwareFieldPath(
            Optional.of(new ComparisonExpression(
                structMember(CONTEXT_VARIABLE, "x"),
                new IntegerLiteral("2"),
                EQUALS)),
        structMember(CONTEXT_VARIABLE, "y")));

    Assertions.assertEquals(parseMountedPath("[mount_point=([?($.x IN [2, 4])])]$.y"),
        new RowSelectorAwareFieldPath(
            Optional.of(new MembershipOperatorExpression(structMember(CONTEXT_VARIABLE, "x"),
                    ImmutableList.of(new IntegerLiteral("2"), new IntegerLiteral("4")),
                    IN)),
            structMember(CONTEXT_VARIABLE, "y")));

    // Mount point contains field reference only
    Assertions.assertEquals(parseMountedPath("[mount_point=($.x)]$.y"),
        new RowSelectorAwareFieldPath(
            Optional.empty(),
            structMember(structMember(CONTEXT_VARIABLE, "x"), "y")));

    // Mount point contains field reference only
    Assertions.assertEquals(parseMountedPath("[mount_point=($.x)]$.y[?(@.z == 5)]"),
        new RowSelectorAwareFieldPath(
            Optional.empty(),
            applyFilter(
                structMember(structMember(CONTEXT_VARIABLE, "x"), "y"),
                new ComparisonExpression(
                    structMember(PREDICATE_CURRENT_VARIABLE,"z"),
                    new IntegerLiteral("5"),
                    EQUALS))));

    // Mount point contains row selector and further operations
    Assertions.assertEquals(parseMountedPath("[mount_point=([?($.x == 2)]$.z)]$.y[?($.k < 5)]"),
        new RowSelectorAwareFieldPath(
            Optional.of(new ComparisonExpression(
                structMember(CONTEXT_VARIABLE, "x"),
                new IntegerLiteral("2"),
                EQUALS)),
            applyFilter(
                structMember(structMember(CONTEXT_VARIABLE, "z"), "y"),
                new ComparisonExpression(
                    structMember(structMember(CONTEXT_VARIABLE, "z"), "k"),
                    new IntegerLiteral("5"),
                    LT))));

    assertThatThrownBy(() -> parseMountedPath("[mount_point=([?($.x == 2)])][?($.y == 5)]$.z"))
        .isInstanceOf(ParsingException.class)
        .hasMessageContaining("Multiple row-selectors are not supported in the mounted path");

    // Validate that mounted path is converted into a row-selector based path
    Assertions.assertEquals(
        parseMountedPath("[mount_point=([?($.x == 2)]$.z)]$.y[?($.k < 5)]"),
        parseRowSelectorAwareFieldPath("[?($.x == 2)]$.z.y[?($.z.k < 5)]"));

    Assertions.assertEquals(parseRowSelectorAwareFieldPath("[?(true)]$.z"),
        new RowSelectorAwareFieldPath(
            Optional.of(new BooleanLiteral("true")),
            structMember(CONTEXT_VARIABLE, "z")));

    Assertions.assertEquals(parseRowSelectorAwareFieldPath("$.z"),
        new RowSelectorAwareFieldPath(Optional.empty(),
            structMember(CONTEXT_VARIABLE, "z")));
  }

  private static void validateRoundTrip(String input) {
    FieldReferenceExpression expression = parseFieldPath(input);
    String output = expression.accept(new FieldPathPrinter(), null);
    assertEquals(input, output);
  }
}
