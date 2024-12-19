package com.linkedin.dataguard.runtime.fieldpaths.virtual.syntaxtree;

public abstract class FieldPathNode {

  public <S, T> T accept(SyntaxTreeVisitor<S, T> visitor, S input) {
    return visitor.visitFieldPathNode(this, input);
  }

  @Override
  public abstract boolean equals(Object o);

  @Override
  public abstract int hashCode();
}
