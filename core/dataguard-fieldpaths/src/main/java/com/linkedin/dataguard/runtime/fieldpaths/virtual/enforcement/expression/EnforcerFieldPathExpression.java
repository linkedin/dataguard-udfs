package com.linkedin.dataguard.runtime.fieldpaths.virtual.enforcement.expression;

import com.linkedin.dataguard.runtime.fieldpaths.virtual.enforcement.Enforcer;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.enforcement.EnforcerBaseElement;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.enforcement.EnforcerExprVisitor;
import com.linkedin.transport.api.StdFactory;
import com.linkedin.transport.api.types.StdType;

import static java.util.Objects.*;

/**
 * A fieldpath is also an expression to be evaluated on the base $ or @.
 *
 * e.g. in the fieldpath $.x[?(@.p == 4)] there are two EnforcerFieldPathExpressions. One
 * corresponds to @.p, and the other corresponds to the whole path.
 */
public class EnforcerFieldPathExpression extends EnforcerExpr {

  private final EnforcerBaseElement enforcerBaseElement;
  private final StdType type;
  private final Enforcer enforcer;

  public EnforcerFieldPathExpression(
      EnforcerBaseElement enforcerBaseElement,
      Enforcer enforcer,
      StdType type) {
    this.enforcerBaseElement = requireNonNull(enforcerBaseElement);
    this.enforcer = requireNonNull(enforcer);
    this.type = requireNonNull(type);
  }

  public EnforcerBaseElement getEnforcerBaseElement() {
    return enforcerBaseElement;
  }

  public Enforcer getEnforcer() {
    return enforcer;
  }

  public EnforcerFieldPathExpression withRowSelector(EnforcerExpr rowSelectorEnforcerExpr, StdFactory stdFactory) {
    return new EnforcerFieldPathExpression(enforcerBaseElement,
        Enforcer.withEnforcerCondition(enforcer, rowSelectorEnforcerExpr, stdFactory),
        type);
  }

  @Override
  public <S, T> T accept(EnforcerExprVisitor<S, T> visitor, S input) {
    return visitor.visitEnforcerFieldPathExpression(this, input);
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
    EnforcerFieldPathExpression that = (EnforcerFieldPathExpression) o;
    return enforcerBaseElement == that.enforcerBaseElement && enforcer.equals(that.enforcer);
  }

  @Override
  public int hashCode() {
    return hash(enforcerBaseElement, enforcer);
  }
}
