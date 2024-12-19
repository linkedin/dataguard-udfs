package com.linkedin.dataguard.runtime.fieldpaths.virtual.syntaxtree;

import java.util.Objects;

import static java.util.Objects.*;


/**
 * Represents a unary +/- operations on an expression.
 * i.e. - x.y, +5
 */
public class SignedExpression extends Expression {

  private final boolean negate;
  private final Expression operand;

  public SignedExpression(boolean negate, Expression operand) {
    this.negate = negate;
    this.operand = requireNonNull(operand);
  }

  public boolean isNegate() {
    return negate;
  }

  public Expression getOperand() {
    return operand;
  }

  @Override
  public <S, T> T accept(SyntaxTreeVisitor<S, T> visitor, S input) {
    return visitor.visitSignedExpression(this, input);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SignedExpression that = (SignedExpression) o;
    return negate == that.negate && operand.equals(that.operand);
  }

  @Override
  public int hashCode() {
    return Objects.hash(negate, operand);
  }
}
