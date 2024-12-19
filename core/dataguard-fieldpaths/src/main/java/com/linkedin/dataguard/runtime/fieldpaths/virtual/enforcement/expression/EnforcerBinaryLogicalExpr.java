package com.linkedin.dataguard.runtime.fieldpaths.virtual.enforcement.expression;

import com.linkedin.dataguard.runtime.fieldpaths.virtual.enforcement.EnforcerExprVisitor;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.syntaxtree.BinaryLogicalOperator;
import com.linkedin.transport.api.StdFactory;
import com.linkedin.transport.api.types.StdBooleanType;
import com.linkedin.transport.api.types.StdType;

import static com.google.common.base.Preconditions.*;
import static java.util.Objects.*;


/**
 * Represents AND / OR operations.
 * e.g. true {@literal &&} false, x.y || z
 *
 */
public class EnforcerBinaryLogicalExpr extends EnforcerExpr {

  private final BinaryLogicalOperator operator;
  private final EnforcerExpr left;
  private final EnforcerExpr right;
  private final StdType type;

  public EnforcerBinaryLogicalExpr(
      EnforcerExpr left,
      EnforcerExpr right,
      BinaryLogicalOperator operator,
      StdFactory factory) {
    this.left = requireNonNull(left);
    this.right = requireNonNull(right);
    checkArgument(left.getType() instanceof StdBooleanType
        && right.getType() instanceof StdBooleanType);
    this.type = factory.createStdType("boolean");
    this.operator = requireNonNull(operator);
  }

  public BinaryLogicalOperator getOperator() {
    return operator;
  }

  public EnforcerExpr getLeft() {
    return left;
  }

  public EnforcerExpr getRight() {
    return right;
  }

  @Override
  public <S, T> T accept(EnforcerExprVisitor<S, T> visitor, S input) {
    return visitor.visitEnforcerBinaryLogicalExpr(this, input);
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
    EnforcerBinaryLogicalExpr that = (EnforcerBinaryLogicalExpr) o;
    return operator == that.operator && left.equals(that.left) && right.equals(that.right);
  }

  @Override
  public int hashCode() {
    return hash(operator, left, right);
  }
}
