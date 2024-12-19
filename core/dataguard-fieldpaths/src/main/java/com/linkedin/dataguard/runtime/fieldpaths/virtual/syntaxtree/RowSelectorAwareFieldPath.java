package com.linkedin.dataguard.runtime.fieldpaths.virtual.syntaxtree;

import java.util.Objects;
import java.util.Optional;


/**
 * Represents the fully qualified virtual field path, with an optional row-selector.
 * All enforcement in future will use objects of this class as the top-level field address.
 */
public class RowSelectorAwareFieldPath extends FieldPathNode {

  private final Optional<Expression> rowSelectorPredicate;
  private final FieldReferenceExpression fieldReferenceExpression;

  public RowSelectorAwareFieldPath(
      Optional<Expression> rowSelectorPredicate,
      FieldReferenceExpression fieldReferenceExpression) {
    this.rowSelectorPredicate = rowSelectorPredicate;
    this.fieldReferenceExpression = fieldReferenceExpression;
  }

  @Override
  public <S, T> T accept(SyntaxTreeVisitor<S, T> visitor, S input) {
    return visitor.visitRowSelectorAwareFieldPath(this, input);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RowSelectorAwareFieldPath that = (RowSelectorAwareFieldPath) o;
    return rowSelectorPredicate.equals(that.rowSelectorPredicate) && fieldReferenceExpression.equals(that.fieldReferenceExpression);
  }

  @Override
  public int hashCode() {
    return Objects.hash(rowSelectorPredicate, fieldReferenceExpression);
  }

  public Optional<Expression> getRowSelectorPredicate() {
    return rowSelectorPredicate;
  }

  public FieldReferenceExpression getFieldPath() {
    return fieldReferenceExpression;
  }
}
