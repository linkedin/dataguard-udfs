package com.linkedin.dataguard.runtime.fieldpaths.virtual.syntaxtree;

/**
 * Represents the "Root" of a virtual field path.
 */
public final class ContextVariable extends FieldReferenceExpression {

  public static final ContextVariable CONTEXT_VARIABLE = new ContextVariable();

  private ContextVariable() {
  }

  public String toString() {
    return "$";
  }

  @Override
  public <S, T> T accept(SyntaxTreeVisitor<S, T> visitor, S input) {
    return visitor.visitContextVariable(this, input);
  }

  @Override
  public boolean equals(Object o) {
    return this == o;
  }

  @Override
  public int hashCode() {
    return ContextVariable.class.hashCode();
  }
}
