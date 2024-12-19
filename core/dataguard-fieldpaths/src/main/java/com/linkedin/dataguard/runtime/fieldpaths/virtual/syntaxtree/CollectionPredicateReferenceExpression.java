package com.linkedin.dataguard.runtime.fieldpaths.virtual.syntaxtree;

import java.util.Objects;

import static java.util.Objects.*;


/**
 * Supports applying predicate on an existing field reference.
 * e.g. in $.x[?(@ == 5)]. Here the condition inside the parenthesis represents a predicate, and
 * "$.x" is the base, which will be validated to be an array during semantic analysis.
 */
public class CollectionPredicateReferenceExpression extends FieldReferenceExpression {

  private final FieldReferenceExpression base;
  private final Expression predicate;

  public CollectionPredicateReferenceExpression(FieldReferenceExpression base, Expression predicate) {
    this.base = requireNonNull(base);
    this.predicate = requireNonNull(predicate);
  }

  public FieldReferenceExpression getBase() {
    return base;
  }

  public Expression getPredicate() {
    return predicate;
  }

  @Override
  public <S, T> T accept(SyntaxTreeVisitor<S, T> visitor, S input) {
    return visitor.visitCollectionPredicateReferenceExpression(this, input);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CollectionPredicateReferenceExpression that = (CollectionPredicateReferenceExpression) o;
    return base.equals(that.base) && predicate.equals(that.predicate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(base, predicate);
  }
}
