package com.linkedin.dataguard.runtime.fieldpaths.virtual.syntaxtree;

// "Unparse" to produce a virtual field path.
public class FieldPathPrinter extends SyntaxTreeVisitor<Void, String> {

  @Override
  public String visitFieldReferenceExpression(FieldReferenceExpression node, Void input) {
    throw new RuntimeException("FieldReferenceExpression not supported by printer");
  }

  @Override
  public String visitMemberReferenceExpression(MemberReferenceExpression node, Void input) {
    return new StringBuilder()
        .append(visit(node.getBase(), input))
        .append(".")
        .append(visit(node.getFieldName(), input))
        .toString();
  }

  @Override
  public String visitCollectionLookupReferenceExpression(CollectionLookupReferenceExpression node, Void input) {
    return new StringBuilder()
        .append(visit(node.getBase(), input))
        .append("[")
        .append(visit(node.getLookupLiteral(), input))
        .append("]")
        .toString();
  }

  @Override
  public String visitCollectionStreamReferenceExpression(CollectionStreamReferenceExpression node, Void input) {
    return new StringBuilder()
        .append(visit(node.getBase(), input))
        .append("[:]")
        .toString();
  }

  @Override
  public String visitCollectionPredicateReferenceExpression(CollectionPredicateReferenceExpression node, Void input) {
    return new StringBuilder()
        .append(visit(node.getBase(), input))
        .append("[?(")
        .append(visit(node.getPredicate(), input))
        .append(")]")
        .toString();
  }

  @Override
  public String visitContextVariable(ContextVariable node, Void input) {
    return "$";
  }

  @Override
  public String visitPredicateCurrentVariable(PredicateCurrentVariable node, Void input) {
    return "@";
  }

  @Override
  public String visitExpression(Expression node, Void input) {
    throw new RuntimeException("Expression not supported by printer");
  }

  @Override
  public String visitComparisonExpression(ComparisonExpression node, Void input) {
    return new StringBuilder()
        .append(visit(node.getLeftExpression(), input))
        .append(" ")
        .append(node.getOperator().getOperator())
        .append(" ")
        .append(visit(node.getRightExpression(), input))
        .toString();
  }

  @Override
  public String visitBinaryArithmeticExpression(BinaryArithmeticExpression node, Void input) {
    return new StringBuilder()
        .append(visit(node.getLeftExpression(), input))
        .append(" ")
        .append(node.getOperator().getOperator())
        .append(" ")
        .append(visit(node.getRightExpression(), input))
        .toString();
  }

  @Override
  public String visitLogicalOperatorExpression(BinaryLogicalOperatorExpression node, Void input) {
    return new StringBuilder()
        .append("(")
        .append(visit(node.getLeftOperand(), input))
        .append(") ")
        .append(node.getOperator().getValue())
        .append(" (")
        .append(visit(node.getRightOperand(), input))
        .append(")")
        .toString();
  }

  @Override
  public String visitNotExpression(NotExpression node, Void input) {
    return new StringBuilder()
        .append("!")
        .append("(")
        .append(visit(node.getOperand(), input))
        .append(")")
        .toString();
  }

  @Override
  public String visitSignedExpression(SignedExpression node, Void input) {
    return new StringBuilder()
        .append(node.isNegate() ? "-" : "+")
        .append("(")
        .append(visit(node.getOperand(), input))
        .append(")")
        .toString();
  }

  @Override
  public String visitLiteral(Literal node, Void input) {
    throw new RuntimeException("Literal not supported by printer");
  }

  @Override
  public String visitIdentifier(Identifier node, Void input) {
    return node.getValue();
  }

  @Override
  public String visitBooleanLiteral(BooleanLiteral node, Void input) {
    return Boolean.toString(node.getValue());
  }

  @Override
  public String visitDecimalLiteral(DecimalLiteral node, Void input) {
    return Double.toString(node.getValue());
  }

  @Override
  public String visitIntegerLiteral(IntegerLiteral node, Void input) {
    return Integer.toString(node.getValue());
  }

  @Override
  public String visitStringLiteral(StringLiteral node, Void input) {
    return "'" + node.getValue() + "'";
  }
}
