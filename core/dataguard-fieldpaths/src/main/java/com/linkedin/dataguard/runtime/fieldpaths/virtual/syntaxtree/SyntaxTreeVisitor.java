package com.linkedin.dataguard.runtime.fieldpaths.virtual.syntaxtree;

/**
 * A class that can be implemented for the purpose of traversing the syntax tree.
  */
public class SyntaxTreeVisitor<S, T> {

  public T visit(FieldPathNode fieldPathNode, S input) {
    return fieldPathNode.accept(this, input);
  }

  public T visitFieldPathNode(FieldPathNode node, S input) {
    return null;
  }

  public T visitRowSelectorAwareFieldPath(RowSelectorAwareFieldPath node, S input) {
    return visitFieldPathNode(node, input);
  }

  public T visitFieldReferenceExpression(FieldReferenceExpression node, S input) {
    return visitExpression(node, input);
  }

  public T visitMemberReferenceExpression(MemberReferenceExpression node, S input) {
    return visitFieldPathNode(node, input);
  }

  public T visitCollectionLookupReferenceExpression(CollectionLookupReferenceExpression node, S input) {
    return visitFieldReferenceExpression(node, input);
  }

  public T visitCollectionStreamReferenceExpression(CollectionStreamReferenceExpression node, S input) {
    return visitFieldReferenceExpression(node, input);
  }

  public T visitCollectionPredicateReferenceExpression(CollectionPredicateReferenceExpression node, S input) {
    return visitFieldPathNode(node, input);
  }

  public T visitContextVariable(ContextVariable node, S input) {
    return visitFieldPathNode(node, input);
  }

  public T visitPredicateCurrentVariable(PredicateCurrentVariable node, S input) {
    return visitFieldPathNode(node, input);
  }

  public T visitExpression(Expression node, S input) {
    return visitFieldPathNode(node, input);
  }

  public T visitComparisonExpression(ComparisonExpression node, S input) {
    return visitExpression(node, input);
  }

  public T visitMembershipOperatorExpression(MembershipOperatorExpression node, S input) {
    return visitFieldPathNode(node, input);
  }

  public T visitBinaryArithmeticExpression(BinaryArithmeticExpression node, S input) {
    return null;
  }

  public T visitLogicalOperatorExpression(BinaryLogicalOperatorExpression node, S input) {
    return visitFieldPathNode(node, input);
  }

  public T visitNotExpression(NotExpression node, S input) {
    return visitFieldPathNode(node, input);
  }

  public T visitSignedExpression(SignedExpression node, S input) {
    return visitFieldPathNode(node, input);
  }

  public T visitFunctionCall(FunctionCall node, S input) {
    return visitExpression(node, input);
  }

  public T visitIdentifier(Identifier node, S input) {
    return visitExpression(node, input);
  }

  public T visitLiteral(Literal node, S input) {
    return visitExpression(node, input);
  }

  public T visitDecimalLiteral(DecimalLiteral node, S input) {
    return visitLiteral(node, input);
  }

  public T visitIntegerLiteral(IntegerLiteral node, S input) {
    return visitLiteral(node, input);
  }

  public T visitBooleanLiteral(BooleanLiteral node, S input) {
    return visitLiteral(node, input);
  }

  public T visitStringLiteral(StringLiteral node, S input) {
    return visitLiteral(node, input);
  }
}
