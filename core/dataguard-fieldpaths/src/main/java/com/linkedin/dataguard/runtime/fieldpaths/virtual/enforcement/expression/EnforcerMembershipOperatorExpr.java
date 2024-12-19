package com.linkedin.dataguard.runtime.fieldpaths.virtual.enforcement.expression;

import com.linkedin.dataguard.runtime.fieldpaths.virtual.enforcement.EnforcerExprVisitor;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.syntaxtree.MembershipOperator;
import com.linkedin.transport.api.StdFactory;
import com.linkedin.transport.api.types.StdType;
import java.util.List;
import java.util.stream.Collectors;

import static com.linkedin.dataguard.runtime.fieldpaths.virtual.semantics.SemanticValidator.SemanticValidatorImpl.*;
import static java.util.Objects.*;


public class EnforcerMembershipOperatorExpr extends EnforcerExpr {

  private final EnforcerExpr left;
  private final List<EnforcerExpr> right;
  private final MembershipOperator operator;
  private final StdType type;

  public EnforcerMembershipOperatorExpr(
      EnforcerExpr left,
      List<EnforcerExpr> right,
      MembershipOperator operator,
      StdFactory factory) {
    this.left = requireNonNull(left);
    this.right = requireNonNull(right);
    this.operator = requireNonNull(operator);

    List<StdType> rightTypes = right.stream().map(EnforcerExpr::getType).collect(Collectors.toList());
    validateMembershipOperatorTypes(left.getType(), rightTypes, operator);
    this.type = factory.createStdType("boolean");
  }

  public EnforcerExpr getLeft() {
    return left;
  }

  public List<EnforcerExpr> getRight() {
    return right;
  }

  public MembershipOperator getOperator() {
    return operator;
  }

  @Override
  public <S, T> T accept(EnforcerExprVisitor<S, T> visitor, S input) {
    return visitor.visitMembershipOperatorExpr(this, input);
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
    EnforcerMembershipOperatorExpr that = (EnforcerMembershipOperatorExpr) o;
    return left.equals(that.left) && right.equals(that.right)
        && operator == that.operator;
  }

  @Override
  public int hashCode() {
    return hash(left, right, operator);
  }
}
