package com.linkedin.dataguard.runtime.fieldpaths.virtual.enforcement.expression;

import com.linkedin.dataguard.runtime.fieldpaths.virtual.enforcement.EnforcerExprVisitor;
import com.linkedin.transport.api.types.StdType;

import static com.linkedin.dataguard.runtime.fieldpaths.virtual.semantics.SemanticValidator.*;
import static java.util.Objects.*;


/**
 * Represents a unary +/- operations on an expression.
 * i.e. - x.y, +5
 */
public class EnforcerSignedExpr extends EnforcerExpr {

  private final boolean negate;
  private final EnforcerExpr operand;
  private final StdType type;

  public EnforcerSignedExpr(boolean negate, EnforcerExpr operand) {
    this.negate = negate;
    this.operand = requireNonNull(operand);
    this.type = operand.getType();
    validateTypeForNumericExpression(type);
  }

  public boolean isNegate() {
    return negate;
  }

  public EnforcerExpr getOperand() {
    return operand;
  }

  @Override
  public <S, T> T accept(EnforcerExprVisitor<S, T> visitor, S input) {
    return visitor.visitEnforcerSignedExpr(this, input);
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
    EnforcerSignedExpr that = (EnforcerSignedExpr) o;
    return negate == that.negate && operand.equals(that.operand);
  }

  @Override
  public int hashCode() {
    return hash(negate, operand);
  }
}
