package com.linkedin.dataguard.runtime.fieldpaths.virtual.enforcement;

import com.linkedin.dataguard.runtime.fieldpaths.virtual.TypedFunctionInfo;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.enforcement.expression.EnforcerExpr;
import com.linkedin.transport.api.types.StdType;
import java.util.List;


public class EnforcerFunctionCall extends EnforcerExpr {

  private final TypedFunctionInfo typedFunctionInfo;
  private final List<EnforcerExpr> arguments;

  public EnforcerFunctionCall(
      TypedFunctionInfo typedFunctionInfo, List<EnforcerExpr> arguments) {
    this.typedFunctionInfo = typedFunctionInfo;
    this.arguments = arguments;
  }

  public TypedFunctionInfo getTypedFunctionInfo() {
    return typedFunctionInfo;
  }

  public List<EnforcerExpr> getArguments() {
    return arguments;
  }

  @Override
  public StdType getType() {
    return typedFunctionInfo.getReturnType();
  }

  @Override
  public <S, T> T accept(EnforcerExprVisitor<S, T> visitor, S input) {
    return visitor.visitEnforcerFunctionCall(this, input);
  }

}
