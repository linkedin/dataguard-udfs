package com.linkedin.dataguard.runtime.fieldpaths.virtual.syntaxtree;

// Umbrella class representing all reference expressions.
public abstract class FieldReferenceExpression extends Expression {

  @Override
  public <S, T> T accept(SyntaxTreeVisitor<S, T> visitor, S input) {
    return visitor.visitFieldReferenceExpression(this, input);
  }
}
