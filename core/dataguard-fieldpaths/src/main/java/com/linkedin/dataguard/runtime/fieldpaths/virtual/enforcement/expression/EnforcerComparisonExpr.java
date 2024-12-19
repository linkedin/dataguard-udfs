package com.linkedin.dataguard.runtime.fieldpaths.virtual.enforcement.expression;

import com.linkedin.dataguard.runtime.fieldpaths.virtual.enforcement.EnforcerExprVisitor;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.syntaxtree.ComparisonOperator;
import com.linkedin.transport.api.StdFactory;
import com.linkedin.transport.api.types.StdType;

import static com.linkedin.dataguard.runtime.fieldpaths.virtual.semantics.SemanticValidator.*;
import static java.util.Objects.*;


/**
 * Represents comparison operations (`==`, `!=`, {@literal >=}, {@literal <=}, {@literal >}, {@literal <}) on
 * two operands. e.g. x == y, z.w != 5.
 */
public class EnforcerComparisonExpr extends EnforcerExpr {

  private final EnforcerExpr left;
  private final EnforcerExpr right;
  private final ComparisonOperator operator;
  private final StdType type;

  public EnforcerComparisonExpr(
      EnforcerExpr left,
      EnforcerExpr right,
      ComparisonOperator operator,
      StdFactory factory) {
    this.left = requireNonNull(left);
    this.right = requireNonNull(right);
    this.operator = requireNonNull(operator);
    validateComparisonTypes(left.getType(), right.getType(), operator);
    this.type = factory.createStdType("boolean");
  }

  public EnforcerExpr getLeft() {
    return left;
  }

  public EnforcerExpr getRight() {
    return right;
  }

  public ComparisonOperator getOperator() {
    return operator;
  }

  @Override
  public <S, T> T accept(EnforcerExprVisitor<S, T> visitor, S input) {
    return visitor.visitEnforcerComparisonExpr(this, input);
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
    EnforcerComparisonExpr that = (EnforcerComparisonExpr) o;
    return left.equals(that.left) && right.equals(that.right)
        && operator == that.operator;
  }

  @Override
  public int hashCode() {
    return hash(left, right, operator);
  }
}
