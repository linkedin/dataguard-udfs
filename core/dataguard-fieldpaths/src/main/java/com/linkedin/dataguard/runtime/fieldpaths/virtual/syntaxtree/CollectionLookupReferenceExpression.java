package com.linkedin.dataguard.runtime.fieldpaths.virtual.syntaxtree;

import java.util.Objects;
import java.util.Optional;

import static java.util.Objects.*;

/**
 * Supports referencing an element out of the array.
 * e.g. x[5] is used to indicate referencing 5th element (0-indexed) from array `x`.
 */
public class CollectionLookupReferenceExpression extends FieldReferenceExpression {

  private final FieldReferenceExpression base;
  private final Optional<Literal> lookupLiteral;
  private final Optional<FunctionCall> lookupFunctionCall;

  public CollectionLookupReferenceExpression(FieldReferenceExpression base, Literal lookupLiteral) {
    this.base = requireNonNull(base);
    this.lookupLiteral = Optional.of(requireNonNull(lookupLiteral));
    this.lookupFunctionCall = Optional.empty();
  }

  public CollectionLookupReferenceExpression(FieldReferenceExpression base, FunctionCall lookupFunctionCall) {
    this.base = requireNonNull(base);
    this.lookupFunctionCall = Optional.of(requireNonNull(lookupFunctionCall));
    this.lookupLiteral = Optional.empty();
  }

  public FieldReferenceExpression getBase() {
    return base;
  }

  /**
   * use isLiteralLookup method before calling this to avoid exception
   */
  public Literal getLookupLiteral() {
    return lookupLiteral.orElseThrow(() -> new RuntimeException("lookup is not a literal"));
  }

  /**
   * use isLiteralLookup method before calling this to avoid exception
   */
  public FunctionCall getLookupFunctionCall() {
    return lookupFunctionCall.orElseThrow(() -> new RuntimeException("lookup is not a functioncall"));
  }

  public boolean isLiteralLookup() {
    return lookupLiteral.isPresent();
  }

  @Override
  public <S, T> T accept(SyntaxTreeVisitor<S, T> visitor, S input) {
    return visitor.visitCollectionLookupReferenceExpression(this, input);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CollectionLookupReferenceExpression that = (CollectionLookupReferenceExpression) o;
    return base.equals(that.base) && lookupLiteral.equals(that.lookupLiteral) && lookupFunctionCall.equals(
        that.lookupFunctionCall);
  }

  @Override
  public int hashCode() {
    return Objects.hash(base, lookupLiteral, lookupFunctionCall);
  }
}
