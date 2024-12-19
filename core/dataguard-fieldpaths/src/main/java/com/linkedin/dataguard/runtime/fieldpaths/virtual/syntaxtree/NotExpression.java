package com.linkedin.dataguard.runtime.fieldpaths.virtual.syntaxtree;

import static java.util.Objects.*;


/**
 * Supports negating an expression. eg. !(x) where x is another expression.
 */
public class NotExpression extends Expression {

  private final Expression operand;

  public NotExpression(Expression operand) {
    this.operand = requireNonNull(operand);
  }

  public Expression getOperand() {
    return operand;
  }

  @Override
  public <S, T> T accept(SyntaxTreeVisitor<S, T> visitor, S input) {
    return visitor.visitNotExpression(this, input);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    NotExpression that = (NotExpression) o;
    return operand.equals(that.operand);
  }

  @Override
  public int hashCode() {
    return hash(operand);
  }
}
