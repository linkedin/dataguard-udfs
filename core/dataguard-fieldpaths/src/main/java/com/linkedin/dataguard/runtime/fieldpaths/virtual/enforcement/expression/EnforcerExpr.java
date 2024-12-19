package com.linkedin.dataguard.runtime.fieldpaths.virtual.enforcement.expression;

import com.linkedin.dataguard.runtime.fieldpaths.virtual.enforcement.EnforcerExprVisitor;
import com.linkedin.transport.api.types.StdType;


/**
 * An umbrella class representing all expressions, used by the enforcer implementation. This is intentionally separate
 * from {@link com.linkedin.dataguard.runtime.fieldpaths.virtual.syntaxtree.Expression}, which represents syntax tree before the type
 * validation. (Syntax tree models what the path looks like. Enforcer models the recipe of operations to perform on the
 * input data.) Some implementations of this class may look very similar to the corresponding syntax tree nodes, but
 * this isolation helps evolve parsing/validation and enforcement independently.
 */
public abstract class EnforcerExpr {

  public <S, T> T accept(EnforcerExprVisitor<S, T> visitor, S input) {
    return visitor.visitEnforcerExpr(this, input);
  }

  public abstract StdType getType();
}
