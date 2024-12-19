package com.linkedin.dataguard.runtime.fieldpaths.virtual;

import com.linkedin.dataguard.runtime.transport.common.FormatSpecificTypeDataProvider;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.semantics.SemanticException;
import com.linkedin.transport.api.types.StdType;
import java.util.Optional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static com.linkedin.dataguard.runtime.fieldpaths.virtual.DataGuardPathOperations.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class TestValidation {

  private FormatSpecificTypeDataProvider typeDataProvider;
  private StdType testingTableSchema;
  private DataGuardPathOperations dataguardPathOperations;

  @BeforeAll
  public void setup() {
    this.typeDataProvider = typeDataProvider();
    this.dataguardPathOperations = new DataGuardPathOperations(typeDataProvider);
    testingTableSchema = typeDataProvider.getStdFactory().createStdType(""
        + "row("
        + "   a integer, "
        + "   b row(f0 integer, f1 boolean, f2 varchar),"
        + "   c array(row(f0 integer, f1 array(varchar))),"
        + "   d map(varchar, row(f0 integer, f1 array(varchar), f2 row(f3 integer, f4 integer))),"
        + "   e map(varchar, integer),"
        + "   f array(array(varchar)))");
  }

  protected abstract FormatSpecificTypeDataProvider typeDataProvider();

  @Test
  public void testValidation() {
    assertValidation("$.a", "integer");
    assertValidation("$.b", "row(f0 integer, f1 boolean, f2 varchar)");

    assertValidation("$.c[:].f0", "integer");
    assertValidation("$.c[?(@.f0 == 5)][:].f1", "array(varchar)");
    assertValidation("$.c[?(@.f0 > 5 && @.f0 < $.a)][:].f1[:]", "varchar");
    assertValidation("$.c[:].f1[?(@ > 'foo')]", "array(varchar)");

    assertValidation("$.d[:].key", "varchar");
    assertValidation("$.d[:].value.f2.f3", "integer");
    assertValidation("$.d[?(@.key == 'abc' && @.value.f0 > 4)][:].value.f1[:]",
        "varchar");

    assertValidation("$.e", "map(varchar, integer)");
    assertValidation("$.e[:]", "row(key varchar, value integer)");

    // Say f is
    // [ [11, 22, 33],
    // [ [44, 55, 66] ]

    // represents f
    assertValidation("$.f", "array(array(varchar))");
    // represents [11, 22, 33]
    assertValidation("$.f[0]", "array(varchar)");
    // represents 11
    assertValidation("$.f[0][0]", "varchar");
    // represents f, but further operations on 1D arrays
    assertValidation("$.f[:]", "array(varchar)");
    // represents [ 11, 44 ]
    assertValidation("$.f[:][0]", "varchar");
    // represents [11, 22, 33]
    assertValidation("$.f[0][:]", "varchar");
    // represents f, but further operations on individual elements
    assertValidation("$.f[:][:]", "varchar");
    // represents [11, 22, 33]
    assertValidation("$.f[?(true)][0]", "array(varchar)");
    // represents [11, 44]
    assertValidation("$.f[?(true)][:][0]", "varchar");
    // represents 11
    assertValidationFailure("$.f[:][0][?(true)]", "Predicates can only be applied on arrays or maps.*");
    assertValidationFailure("$.f[:][:][?(true)]", "Predicates can only be applied on arrays or maps.*");
  }

  @Test
  public void assertRowSelectorValidation() {
    // nothing in mountpoint
    assertMountingValidation("", "$.a", "integer");
    // row-selector in mountpoint
    assertMountingValidation("[?($.b.f0 == 2)]", "$.a", "integer");
    // row-selector in mountpoint using the IN operator
    assertMountingValidation("[?($.b.f0 IN [2, 4])]", "$.a", "integer");
    // field reference expression in mountpoint, that is used to "resolve" $.f1 path
    assertMountingValidation("$.b", "$.f1", "boolean");
    // row-selector and field reference in mountpoint
    assertMountingValidation("[?($.b.f0 == 2)]$.b", "$.f1", "boolean");
    // row-selector in fieldpath
    assertMountingValidation("$.b", "[?($.b.f0 == 2)]$.f1", "boolean");
    // row-selector with a function
    assertMountingValidation("[?(callisto__getPurposeForPagekey($.b.f2) == 'abc')]", "$.a", "integer");
    // invalid reference after resolution
    assertMountingValidationFailure("$.b", "$.a", "No field with name a found in .*");
    // row-selector and field reference in mountpoint with IN operator
    assertMountingValidation("[?($.b.f0 IN [2, 4])]$.b", "$.f1", "boolean");
    // invalid function in row selector
    assertMountingValidationFailure("[?(foo_function($.b.f2) == 'abc')]", "$.a", "Function foo_function not found.*");
  }

  @Test
  public void testValidationFailures() {
    assertValidationFailure("$.foo", "No field with name foo found in .*");
    assertValidationFailure("$.b.f0[:]", "Only array and map typed columns can be streamed. Found .* with type .*");
    assertValidationFailure("$.c.f0", "Expected .*, found class .*");
    assertValidationFailure("$.c[?(@.f1[:] == 'a')]", "\\[:\\] operation is not supported within a predicate");
    assertValidationFailure("$.c[?(@.f0 == 'b')]", "Cannot perform operation 'EQUALS' between operands of type .* and .*");
    assertValidationFailure("$.c[?(5.0 + 6 < 11)]", "Cannot perform operation 'ADDITION' between operands of type .* and .*");
  }

  private void assertValidationFailure(String path, String errorMatches) {
    assertThatThrownBy(() -> dataguardPathOperations.validateFieldPathAndGetType(path, testingTableSchema))
        .isInstanceOf(SemanticException.class)
        .hasMessageMatching(errorMatches);
  }

  private void assertValidation(String path, String expectedType) {
    assertEquals(dataguardPathOperations.validateFieldPathAndGetType(path, testingTableSchema).underlyingType(),
        typeDataProvider.getStdFactory().createStdType(expectedType).underlyingType());
  }

  private void assertMountingValidation(String mount, String path, String expectedType) {
    assertEquals(dataguardPathOperations.validateMountingAndGetType(Optional.of(mount), path, testingTableSchema).underlyingType(),
        typeDataProvider.getStdFactory().createStdType(expectedType).underlyingType());
  }

  private void assertMountingValidationFailure(String mount, String path, String errorMatches) {
    assertThatThrownBy(() -> dataguardPathOperations.validateMountingAndGetType(Optional.of(mount), path, testingTableSchema))
        .isInstanceOf(SemanticException.class)
        .hasMessageMatching(errorMatches);
  }
}
