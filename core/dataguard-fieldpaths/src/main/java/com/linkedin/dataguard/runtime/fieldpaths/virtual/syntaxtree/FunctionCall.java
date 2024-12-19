package com.linkedin.dataguard.runtime.fieldpaths.virtual.syntaxtree;

import java.util.List;

import static java.util.Objects.*;


public class FunctionCall extends Expression {
  private final Identifier functionName;
  private final List<Expression> parameters;

  public FunctionCall(Identifier functionName, List<Expression> parameters) {
    this.functionName = requireNonNull(functionName);
    this.parameters = requireNonNull(parameters);
  }

  public Identifier getFunctionName() {
    return functionName;
  }

  public List<Expression> getParameters() {
    return parameters;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FunctionCall that = (FunctionCall) o;
    return functionName.equals(that.functionName) && parameters.equals(that.parameters);
  }

  @Override
  public int hashCode() {
    return hash(functionName, parameters);
  }

  @Override
  public <S, T> T accept(SyntaxTreeVisitor<S, T> visitor, S input) {
    return visitor.visitFunctionCall(this, input);
  }
}
