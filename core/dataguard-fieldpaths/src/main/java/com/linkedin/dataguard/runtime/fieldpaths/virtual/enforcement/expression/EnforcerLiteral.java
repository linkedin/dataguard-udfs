package com.linkedin.dataguard.runtime.fieldpaths.virtual.enforcement.expression;

import com.linkedin.dataguard.runtime.fieldpaths.virtual.enforcement.EnforcerExprVisitor;
import com.linkedin.transport.api.data.StdData;
import com.linkedin.transport.api.types.StdType;

import static com.linkedin.dataguard.runtime.fieldpaths.virtual.semantics.SemanticValidator.*;
import static java.util.Objects.*;


public class EnforcerLiteral extends EnforcerExpr {

  private final StdType type;
  private final Object value;

  private final StdData stdValue;

  public EnforcerLiteral(StdType type, Object value, StdData stdValue) {
    this.type = requireNonNull(type);
    validatePrimitiveType(type);
    this.value = requireNonNull(value);
    this.stdValue = requireNonNull(stdValue);
  }

  @Override
  public StdType getType() {
    return type;
  }

  public Object getValue() {
    return value;
  }

  public StdData getStdValue() {
    return stdValue;
  }

  @Override
  public <S, T> T accept(EnforcerExprVisitor<S, T> visitor, S input) {
    return visitor.visitEnforcerLiteral(this, input);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EnforcerLiteral that = (EnforcerLiteral) o;
    return value.equals(that.value);
  }

  @Override
  public int hashCode() {
    return hash(value);
  }


}
