package com.linkedin.dataguard.runtime.fieldpaths.virtual.syntaxtree;

public abstract class Expression extends FieldPathNode {

  @Override
  public <S, T> T accept(SyntaxTreeVisitor<S, T> visitor, S input) {
    return visitor.visitExpression(this, input);
  }
}
