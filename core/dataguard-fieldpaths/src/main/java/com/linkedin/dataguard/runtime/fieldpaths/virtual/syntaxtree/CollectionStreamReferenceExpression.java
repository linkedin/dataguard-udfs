package com.linkedin.dataguard.runtime.fieldpaths.virtual.syntaxtree;

import static java.util.Objects.*;

/**
 * Supports streaming all elements out of the array or map.
 */
public class CollectionStreamReferenceExpression extends FieldReferenceExpression {
  private final FieldReferenceExpression base;

  public CollectionStreamReferenceExpression(FieldReferenceExpression base) {
    this.base = requireNonNull(base);
  }

  public FieldReferenceExpression getBase() {
    return base;
  }

  @Override
  public <S, T> T accept(SyntaxTreeVisitor<S, T> visitor, S input) {
    return visitor.visitCollectionStreamReferenceExpression(this, input);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CollectionStreamReferenceExpression that = (CollectionStreamReferenceExpression) o;
    return base.equals(that.base);
  }

  @Override
  public int hashCode() {
    return hash(base);
  }
}
