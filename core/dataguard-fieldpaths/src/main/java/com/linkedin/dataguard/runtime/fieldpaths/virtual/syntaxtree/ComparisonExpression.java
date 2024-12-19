package com.linkedin.dataguard.runtime.fieldpaths.virtual.syntaxtree;

import java.util.Objects;

import static java.util.Objects.*;


/**
 * Represents comparison operations (`==`, `!=`, {@literal >=}, {@literal <=}, {@literal >}, {@literal <}) on
 * two operands. e.g. x == y, z.w != 5.
 */
public class ComparisonExpression extends Expression {

  private final Expression leftExpression;
  private final Expression rightExpression;
  private final ComparisonOperator operator;

  public ComparisonExpression(Expression leftExpression, Expression rightExpression,
      ComparisonOperator operator) {
    this.leftExpression = requireNonNull(leftExpression);
    this.rightExpression = requireNonNull(rightExpression);
    this.operator = requireNonNull(operator);
  }

  public Expression getLeftExpression() {
    return leftExpression;
  }

  public Expression getRightExpression() {
    return rightExpression;
  }

  public ComparisonOperator getOperator() {
    return operator;
  }

  @Override
  public <S, T> T accept(SyntaxTreeVisitor<S, T> visitor, S input) {
    return visitor.visitComparisonExpression(this, input);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ComparisonExpression that = (ComparisonExpression) o;
    return leftExpression.equals(that.leftExpression) && rightExpression.equals(that.rightExpression)
        && operator == that.operator;
  }

  @Override
  public int hashCode() {
    return Objects.hash(leftExpression, rightExpression, operator);
  }
}
