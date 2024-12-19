package com.linkedin.dataguard.runtime.fieldpaths.virtual.syntaxtree;

import java.util.ArrayList;
import java.util.List;


public final class ContextVariableReplacer {

  private ContextVariableReplacer() {
  }

  public static FieldReferenceExpression replaceContextVariable(FieldReferenceExpression operand, FieldReferenceExpression replacer) {
    return (FieldReferenceExpression) operand.accept(new ContextVariableReplacerVisitor(replacer), null);
  }

  public static class ContextVariableReplacerVisitor extends SyntaxTreeVisitor<Void, FieldPathNode> {

    private final FieldReferenceExpression replacerExpression;

    ContextVariableReplacerVisitor(FieldReferenceExpression replacerExpression) {
      this.replacerExpression = replacerExpression;
    }

    @Override
    public FieldPathNode visitMemberReferenceExpression(MemberReferenceExpression node, Void input) {
      return new MemberReferenceExpression((FieldReferenceExpression) visit(node.getBase(), input), node.getFieldName());
    }

    @Override
    public FieldPathNode visitCollectionLookupReferenceExpression(CollectionLookupReferenceExpression node, Void input) {
      return new CollectionLookupReferenceExpression(
          (FieldReferenceExpression) visit(node.getBase(), input),
          (Literal) visit(node.getLookupLiteral(), input));
    }

    @Override
    public FieldPathNode visitCollectionStreamReferenceExpression(CollectionStreamReferenceExpression node, Void input) {
      return new CollectionStreamReferenceExpression((FieldReferenceExpression) visit(node.getBase(), input));
    }

    @Override
    public FieldPathNode visitCollectionPredicateReferenceExpression(CollectionPredicateReferenceExpression node, Void input) {
      FieldReferenceExpression base = (FieldReferenceExpression) visit(node.getBase(), input);
      Expression predicate = (Expression) visit(node.getPredicate(), input);
      return new CollectionPredicateReferenceExpression(base, predicate);
    }

    @Override
    public FieldPathNode visitContextVariable(ContextVariable node, Void input) {
      return replacerExpression;
    }

    @Override
    public FieldPathNode visitPredicateCurrentVariable(PredicateCurrentVariable node, Void input) {
      return node;
    }

    @Override
    public FieldPathNode visitComparisonExpression(ComparisonExpression node, Void input) {
      return new ComparisonExpression(
          (Expression) visit(node.getLeftExpression(), input),
          (Expression) visit(node.getRightExpression(), input),
          node.getOperator());
    }

    @Override
    public FieldPathNode visitMembershipOperatorExpression(MembershipOperatorExpression node, Void input) {
      List<Expression> rightExpressionList = new ArrayList<>();
      for (Expression expression: node.getRightExpressionList()) {
        rightExpressionList.add((Expression) visit(expression, input));
      }

      return new MembershipOperatorExpression(
          (Expression) visit(node.getLeftExpression(), input),
          rightExpressionList,
          node.getOperator());
    }

    @Override
    public FieldPathNode visitBinaryArithmeticExpression(BinaryArithmeticExpression node, Void input) {
      return new BinaryArithmeticExpression(
          (Expression) visit(node.getLeftExpression(), input),
          (Expression) visit(node.getRightExpression(), input),
          node.getOperator());
    }

    @Override
    public FieldPathNode visitLogicalOperatorExpression(BinaryLogicalOperatorExpression node, Void input) {
      return new BinaryLogicalOperatorExpression(
          (Expression) visit(node.getLeftOperand(), input),
          (Expression) visit(node.getRightOperand(), input),
          node.getOperator());
    }

    @Override
    public FieldPathNode visitNotExpression(NotExpression node, Void input) {
      return new NotExpression((Expression) visit(node.getOperand(), input));
    }

    @Override
    public FieldPathNode visitSignedExpression(SignedExpression node, Void input) {
      return new SignedExpression(node.isNegate(),
          (Expression) visit(node.getOperand(), input));
    }

    @Override
    public FieldPathNode visitIdentifier(Identifier node, Void input) {
      return node;
    }

    @Override
    public FieldPathNode visitLiteral(Literal node, Void input) {
      return node;
    }
  }
}
