package com.linkedin.dataguard.runtime.fieldpaths.virtual.enforcement;

import com.linkedin.dataguard.runtime.fieldpaths.virtual.enforcement.expression.EnforcerBinaryArithmeticExpr;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.enforcement.expression.EnforcerBinaryLogicalExpr;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.enforcement.expression.EnforcerComparisonExpr;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.enforcement.expression.EnforcerExpr;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.enforcement.expression.EnforcerFieldPathExpression;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.enforcement.expression.EnforcerLiteral;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.enforcement.expression.EnforcerMembershipOperatorExpr;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.enforcement.expression.EnforcerNotExpr;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.enforcement.expression.EnforcerSignedExpr;


/**
 * Base class for visiting EnforcerExpr tree.
 *
 * @param <S> input context class type
 * @param <T> output class type
 */
public class EnforcerExprVisitor<S, T> {

  public T visit(EnforcerExpr enforcerExpr, S input) {
    return enforcerExpr.accept(this, input);
  }

  public T visitEnforcerExpr(EnforcerExpr expr, S input) {
    return null;
  }

  public T visitEnforcerBinaryArithmeticExpr(EnforcerBinaryArithmeticExpr node, S input) {
    return visitEnforcerExpr(node, input);
  }

  public T visitEnforcerBinaryLogicalExpr(EnforcerBinaryLogicalExpr node, S input) {
    return visitEnforcerExpr(node, input);
  }

  public T visitEnforcerComparisonExpr(EnforcerComparisonExpr node, S input) {
    return visitEnforcerExpr(node, input);
  }

  public T visitMembershipOperatorExpr(EnforcerMembershipOperatorExpr node, S input) {
    return visitEnforcerExpr(node, input);
  }

  public T visitEnforcerFieldPathExpression(EnforcerFieldPathExpression node, S input) {
    return visitEnforcerExpr(node, input);
  }

  public T visitEnforcerLiteral(EnforcerLiteral node, S input) {
    return visitEnforcerExpr(node, input);
  }

  public T visitEnforcerNotExpr(EnforcerNotExpr node, S input) {
    return visitEnforcerExpr(node, input);
  }

  public T visitEnforcerSignedExpr(EnforcerSignedExpr node, S input) {
    return visitEnforcerExpr(node, input);
  }

  public T visitEnforcerFunctionCall(EnforcerFunctionCall node, S input) {
    return visitEnforcerExpr(node, input);
  }
}

