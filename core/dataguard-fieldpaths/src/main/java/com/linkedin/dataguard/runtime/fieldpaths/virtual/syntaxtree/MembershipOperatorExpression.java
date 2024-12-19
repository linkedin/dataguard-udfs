package com.linkedin.dataguard.runtime.fieldpaths.virtual.syntaxtree;

import java.util.List;
import java.util.Objects;

import static java.util.Objects.*;


public class MembershipOperatorExpression extends Expression {
  private final MembershipOperator operator;
  private final Expression leftExpression;
  private final List<Expression> rightExpressionList;


  public MembershipOperatorExpression(Expression leftExpression, List<Expression> rightExpressionList,
      MembershipOperator operator) {
    this.leftExpression = requireNonNull(leftExpression);
    this.rightExpressionList = requireNonNull(rightExpressionList);
    this.operator = requireNonNull(operator);
  }

  public MembershipOperator getOperator() {
    return operator;
  }
  public Expression getLeftExpression() {
    return leftExpression;
  }
  public List<Expression> getRightExpressionList() {
    return rightExpressionList;
  }

  @Override
  public <S, T> T accept(SyntaxTreeVisitor<S, T> visitor, S input) {
    return visitor.visitMembershipOperatorExpression(this, input);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MembershipOperatorExpression that = (MembershipOperatorExpression) o;
    return leftExpression.equals(that.leftExpression) && rightExpressionList.equals(that.rightExpressionList)
        && operator == that.operator;
  }

  @Override
  public int hashCode() {
    return Objects.hash(operator, leftExpression, rightExpressionList);
  }
}
