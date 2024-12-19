package com.linkedin.dataguard.runtime.fieldpaths.virtual.syntaxtree;

import java.util.Objects;

import static java.util.Objects.*;


/**
 * Represents AND / OR operations.
 * e.g. true {@literal &&} false, x.y || z
 */
public class BinaryLogicalOperatorExpression extends Expression {

  private final BinaryLogicalOperator operator;
  private final Expression leftOperand;
  private final Expression rightOperand;

  public BinaryLogicalOperatorExpression(Expression leftOperand, Expression rightOperand,
      BinaryLogicalOperator operator) {
    this.leftOperand = requireNonNull(leftOperand);
    this.rightOperand = requireNonNull(rightOperand);
    this.operator = requireNonNull(operator);
  }

  public BinaryLogicalOperator getOperator() {
    return operator;
  }

  public Expression getLeftOperand() {
    return leftOperand;
  }

  public Expression getRightOperand() {
    return rightOperand;
  }

  @Override
  public <S, T> T accept(SyntaxTreeVisitor<S, T> visitor, S input) {
    return visitor.visitLogicalOperatorExpression(this, input);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BinaryLogicalOperatorExpression that = (BinaryLogicalOperatorExpression) o;
    return operator == that.operator && leftOperand.equals(that.leftOperand) && rightOperand.equals(that.rightOperand);
  }

  @Override
  public int hashCode() {
    return Objects.hash(operator, leftOperand, rightOperand);
  }
}
