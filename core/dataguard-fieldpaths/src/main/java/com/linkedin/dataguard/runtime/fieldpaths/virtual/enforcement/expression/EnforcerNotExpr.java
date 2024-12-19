package com.linkedin.dataguard.runtime.fieldpaths.virtual.enforcement.expression;

import com.linkedin.dataguard.runtime.fieldpaths.virtual.enforcement.EnforcerExprVisitor;
import com.linkedin.transport.api.StdFactory;
import com.linkedin.transport.api.types.StdBooleanType;
import com.linkedin.transport.api.types.StdType;
import java.util.Objects;

import static com.google.common.base.Preconditions.*;
import static java.util.Objects.*;


/**
 * Supports negating an expression. eg. !(x) where x is another expression.
 */
public class EnforcerNotExpr extends EnforcerExpr {

  private final EnforcerExpr operand;
  private final StdType type;

  public EnforcerNotExpr(EnforcerExpr operand, StdFactory factory) {
    checkArgument(operand.getType() instanceof StdBooleanType);
    this.type = factory.createStdType("boolean");
    this.operand = requireNonNull(operand);
  }

  public EnforcerExpr getOperand() {
    return operand;
  }

  @Override
  public <S, T> T accept(EnforcerExprVisitor<S, T> visitor, S input) {
    return visitor.visitEnforcerNotExpr(this, input);
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
    EnforcerNotExpr that = (EnforcerNotExpr) o;
    return Objects.equals(operand, that.operand);
  }

  @Override
  public int hashCode() {
    return hash(operand);
  }
}
