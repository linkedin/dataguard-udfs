package com.linkedin.dataguard.runtime.fieldpaths.virtual.syntaxtree;

/**
 * Provides reference to the "current" variable item while defining a predicate.
 */
public final class PredicateCurrentVariable extends FieldReferenceExpression {

  public PredicateCurrentVariable() {
  }

  public String toString() {
    return "@";
  }

  @Override
  public <S, T> T accept(SyntaxTreeVisitor<S, T> visitor, S input) {
    return visitor.visitPredicateCurrentVariable(this, input);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    return PredicateCurrentVariable.class.hashCode();
  }
}
