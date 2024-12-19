package com.linkedin.dataguard.runtime.fieldpaths.virtual.enforcement.expression;

import com.linkedin.dataguard.runtime.fieldpaths.virtual.enforcement.EnforcerExprVisitor;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.syntaxtree.BinaryArithmeticOperator;
import com.linkedin.transport.api.types.StdType;

import static com.linkedin.dataguard.runtime.fieldpaths.virtual.semantics.SemanticValidator.*;
import static java.util.Objects.*;

/**
 * Represents arithmetic operations (*,/,%,+,-) on two operands.
 * e.g. 5 * 7, a / b, p.x / 6
 *
 */
public class EnforcerBinaryArithmeticExpr extends EnforcerExpr {

  private final EnforcerExpr left;
  private final EnforcerExpr right;
  private final StdType type;
  private final BinaryArithmeticOperator operator;

  public EnforcerBinaryArithmeticExpr(
      EnforcerExpr left,
      EnforcerExpr right,
      BinaryArithmeticOperator operator) {
    this.left = requireNonNull(left);
    this.right = requireNonNull(right);
    this.type = checkAndGetArithmeticExprType(left.getType(), right.getType(), operator);
    this.operator = requireNonNull(operator);
  }

  public EnforcerExpr getLeft() {
    return left;
  }

  public EnforcerExpr getRight() {
    return right;
  }

  public BinaryArithmeticOperator getOperator() {
    return operator;
  }

  @Override
  public <S, T> T accept(EnforcerExprVisitor<S, T> visitor, S input) {
    return visitor.visitEnforcerBinaryArithmeticExpr(this, input);
  }

  @Override
  public StdType getType() {
    return type;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EnforcerBinaryArithmeticExpr that = (EnforcerBinaryArithmeticExpr) o;
    return left.equals(that.left) && right.equals(that.right) && type.equals(
        that.type) && operator == that.operator;
  }

  @Override
  public int hashCode() {
    return hash(left, right, type, operator);
  }
}
