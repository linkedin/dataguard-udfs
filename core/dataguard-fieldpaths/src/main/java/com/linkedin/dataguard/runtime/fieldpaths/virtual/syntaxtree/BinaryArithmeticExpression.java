package com.linkedin.dataguard.runtime.fieldpaths.virtual.syntaxtree;

import java.util.Objects;

import static java.util.Objects.*;

/**
 * Represents arithmetic operations (*,/,%,+,-) on two operands.
 * e.g. 5 * 7, a / b, p.x / 6
 */
public class BinaryArithmeticExpression extends Expression {

  private final Expression leftExpression;
  private final Expression rightExpression;
  private final BinaryArithmeticOperator operator;

  public BinaryArithmeticExpression(Expression leftExpression, Expression rightExpression,
      BinaryArithmeticOperator operator) {
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

  public BinaryArithmeticOperator getOperator() {
    return operator;
  }

  @Override
  public <S, T> T accept(SyntaxTreeVisitor<S, T> visitor, S input) {
    return visitor.visitBinaryArithmeticExpression(this, input);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BinaryArithmeticExpression that = (BinaryArithmeticExpression) o;
    return leftExpression.equals(that.leftExpression) && rightExpression.equals(that.rightExpression)
        && operator == that.operator;
  }

  @Override
  public int hashCode() {
    return Objects.hash(leftExpression, rightExpression, operator);
  }
}
