package com.linkedin.dataguard.runtime.fieldpaths.virtual.syntaxtree;

import com.linkedin.dataguard.runtime.fieldpaths.virtual.parsing.ParsingException;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.parsing.VirtualFieldPathBaseVisitor;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.parsing.VirtualFieldPathLexer;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.parsing.VirtualFieldPathParser;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.TerminalNode;

import static com.google.common.collect.ImmutableList.*;
import static com.linkedin.dataguard.runtime.fieldpaths.virtual.syntaxtree.ContextVariableReplacer.*;


// Convert the parse tree to an AST which can later be processed for semantic understanding.
public class FieldPathSyntaxTreeBuilder extends VirtualFieldPathBaseVisitor<FieldPathNode> {

  /**
   * Resolves mounting and produces a {@link RowSelectorAwareFieldPath} node.
   *
   * Row selector can be either present in the mountPoint or in the field path but not both.
   * Otherwise, resolves the path with respect to the mountpoint by replacing the context variable
   * with the mountpoint.
   */
  @Override
  public FieldPathNode visitMountedPath(VirtualFieldPathParser.MountedPathContext ctx) {

    Optional<Expression> rowSelectorInMountPoint = Optional.ofNullable(ctx.mountPoint().rowSelector())
        .map(this::visit).map(Expression.class::cast);

    Optional<FieldReferenceExpression> fieldPathInMountPoint = Optional.ofNullable(ctx.mountPoint().fieldReferenceExpr())
        .map(this::visit).map(FieldReferenceExpression.class::cast);

    RowSelectorAwareFieldPath rowSelectorAwareFieldPath =
        (RowSelectorAwareFieldPath) visit(ctx.rowSelectorAwareFieldPath());

    if (rowSelectorInMountPoint.isPresent() && rowSelectorAwareFieldPath.getRowSelectorPredicate().isPresent()) {
      throw new ParsingException("Multiple row-selectors are not supported in the mounted path");
    }

    Optional<Expression> rowSelector = rowSelectorInMountPoint;
    if (!rowSelector.isPresent()) {
      rowSelector = rowSelectorAwareFieldPath.getRowSelectorPredicate();
    }

    FieldReferenceExpression resolvedMount = resolveMount(rowSelectorAwareFieldPath.getFieldPath(), fieldPathInMountPoint);
    return new RowSelectorAwareFieldPath(rowSelector, resolvedMount);
  }

  @Override
  public FieldPathNode visitRowSelectorAwareFieldPath(VirtualFieldPathParser.RowSelectorAwareFieldPathContext ctx) {
    return new RowSelectorAwareFieldPath(
        Optional.ofNullable(ctx.rowSelector())
            .map(this::visit)
            .map(Expression.class::cast),
        (FieldReferenceExpression) visit(ctx.fieldReferenceExpr()));
  }

  @Override
  public FieldPathNode visitRowSelector(VirtualFieldPathParser.RowSelectorContext ctx) {
    return visit(ctx.collectionPredicate().predicate());
  }

  @Override
  public FieldPathNode visitVirtualFieldPath(VirtualFieldPathParser.VirtualFieldPathContext ctx) {
    return visit(ctx.fieldReferenceExpr());
  }

  @Override
  public FieldPathNode visitMemberRef(VirtualFieldPathParser.MemberRefContext ctx) {
    FieldReferenceExpression base = (FieldReferenceExpression) visit(ctx.fieldReferenceExpr());
    Identifier member = (Identifier) visit(ctx.identifier());
    return new MemberReferenceExpression(base, member);
  }

  @Override
  public FieldPathNode visitStreamRef(VirtualFieldPathParser.StreamRefContext ctx) {
    return new CollectionStreamReferenceExpression((FieldReferenceExpression) visit(ctx.fieldReferenceExpr()));
  }

  @Override
  public FieldPathNode visitLookupRef(VirtualFieldPathParser.LookupRefContext ctx) {
    FieldReferenceExpression base = (FieldReferenceExpression) visit(ctx.fieldReferenceExpr());
    if (ctx.literal() != null) {
      return new CollectionLookupReferenceExpression(base, (Literal) visit(ctx.literal()));
    }

    return new CollectionLookupReferenceExpression(base, (FunctionCall) visit(ctx.functionCall()));
  }

  @Override
  public FieldPathNode visitFunctionCall(VirtualFieldPathParser.FunctionCallContext ctx) {
    return new FunctionCall(
        (Identifier) visit(ctx.identifier()),
        ctx.expr().stream()
            .map(this::visit)
            .map(Expression.class::cast)
            .collect(toImmutableList()));
  }

  @Override
  public FieldPathNode visitFilterRef(VirtualFieldPathParser.FilterRefContext ctx) {
    FieldReferenceExpression base = (FieldReferenceExpression) visit(ctx.fieldReferenceExpr());
    Expression predicate = (Expression) visit(ctx.collectionPredicate().predicate());
    return new CollectionPredicateReferenceExpression(base, predicate);
  }

  @Override
  public FieldPathNode visitPredicateBase(VirtualFieldPathParser.PredicateBaseContext ctx) {
    Expression left = (Expression) visit(ctx.leftExpr);
    Expression right = (Expression) visit(ctx.rightExpr);
    ComparisonOperator operator = getComparisonOperator(getTerminalSymbol(ctx.comparisonOperator()));
    return new ComparisonExpression(left, right, operator);
  }

  @Override
  public FieldPathNode visitPredicateParen(VirtualFieldPathParser.PredicateParenContext ctx) {
    return visit(ctx.predicateWithParenthesis().predicate());
  }

  @Override
  public FieldPathNode visitNotPredicate(VirtualFieldPathParser.NotPredicateContext ctx) {
    return new NotExpression((Expression) visit(ctx.predicateWithParenthesis().predicate()));
  }

  @Override
  public FieldPathNode visitAndPredicate(VirtualFieldPathParser.AndPredicateContext ctx) {
    Expression left = (Expression) visit(ctx.leftPredicate);
    Expression right = (Expression) visit(ctx.rightPredicate);
    return new BinaryLogicalOperatorExpression(left, right, BinaryLogicalOperator.AND);
  }

  @Override
  public FieldPathNode visitOrPredicate(VirtualFieldPathParser.OrPredicateContext ctx) {
    Expression left = (Expression) visit(ctx.leftPredicate);
    Expression right = (Expression) visit(ctx.rightPredicate);
    return new BinaryLogicalOperatorExpression(left, right, BinaryLogicalOperator.OR);
  }

  @Override
  public FieldPathNode visitInPredicate(VirtualFieldPathParser.InPredicateContext ctx) {
    Expression left = (Expression) visit(ctx.leftExpr);
    List<Expression> right = new ArrayList<>();

    for (VirtualFieldPathParser.LiteralContext literalContext: ctx.literal()) {
      right.add((Expression) visit(literalContext));
    }
    return new MembershipOperatorExpression(left, right, MembershipOperator.IN);
  }

  @Override
  public FieldPathNode visitParenthesizedExpr(VirtualFieldPathParser.ParenthesizedExprContext ctx) {
    return visit(ctx.expr());
  }

  @Override
  public FieldPathNode visitSignedExpr(VirtualFieldPathParser.SignedExprContext ctx) {
    Expression operand = (Expression) visit(ctx.expr());
    Token sign = getTerminalSymbol(ctx.expr());
    return new SignedExpression(isNegated(sign), operand);
  }

  private boolean isNegated(Token operator) {
    switch (operator.getType()) {
      case VirtualFieldPathLexer.PLUS:
        return false;
      case VirtualFieldPathLexer.MINUS:
        return true;
      default:
        throw new RuntimeException("Unknown operator");
    }
  }

  @Override
  public FieldPathNode visitMultiplicativeExpr(VirtualFieldPathParser.MultiplicativeExprContext ctx) {
    Expression left = (Expression) visit(ctx.left);
    Expression right = (Expression) visit(ctx.right);
    BinaryArithmeticOperator operator = getMultiplicativeOperator(getTerminalSymbol(ctx.operator));
    return new BinaryArithmeticExpression(left, right, operator);
  }

  @Override
  public FieldPathNode visitAdditiveExpr(VirtualFieldPathParser.AdditiveExprContext ctx) {
    Expression left = (Expression) visit(ctx.left);
    Expression right = (Expression) visit(ctx.right);
    BinaryArithmeticOperator operator = getAdditiveOperator(getTerminalSymbol(ctx.operator));
    return new BinaryArithmeticExpression(left, right, operator);
  }

  private Token getTerminalSymbol(ParserRuleContext context) {
    if (context.getChildCount() != 1) {
      throw new RuntimeException("Cannot get single terminal symbol for a node with multiple children");
    }

    if (!(context.getChild(0) instanceof TerminalNode)) {
      throw new RuntimeException("Cannot get terminal symbol from an intermediate node");
    }
    return ((TerminalNode) context.getChild(0)).getSymbol();
  }

  private BinaryArithmeticOperator getAdditiveOperator(Token operator) {
    switch (operator.getType()) {
      case VirtualFieldPathLexer.PLUS:
        return BinaryArithmeticOperator.ADDITION;
      case VirtualFieldPathLexer.MINUS:
        return BinaryArithmeticOperator.SUBTRACTION;
      default:
        throw new RuntimeException("Unknown operator");
    }
  }

  private BinaryArithmeticOperator getMultiplicativeOperator(Token operator) {
    switch (operator.getType()) {
      case VirtualFieldPathLexer.ASTERISK:
        return BinaryArithmeticOperator.MULTIPLICATION;
      case VirtualFieldPathLexer.DIVISION_OP:
        return BinaryArithmeticOperator.DIVISION;
      case VirtualFieldPathLexer.MOD_OP:
        return BinaryArithmeticOperator.MODULO;
      default:
        throw new RuntimeException("Unknown operator");
    }
  }

  private ComparisonOperator getComparisonOperator(Token operator) {
    switch (operator.getType()) {
      case VirtualFieldPathLexer.EQUALITY_OP:
        return ComparisonOperator.EQUALS;
      case VirtualFieldPathLexer.INEQUALITY_OP:
        return ComparisonOperator.NOT_EQUALS;
      case VirtualFieldPathLexer.LT_OP:
        return ComparisonOperator.LT;
      case VirtualFieldPathLexer.GT_OP:
        return ComparisonOperator.GT;
      case VirtualFieldPathLexer.LTE_OP:
        return ComparisonOperator.LTE;
      case VirtualFieldPathLexer.GTE_OP:
        return ComparisonOperator.GTE;
      default:
        throw new RuntimeException("Unknown operator");
    }
  }

  @Override
  public FieldPathNode visitDecimalLiteral(VirtualFieldPathParser.DecimalLiteralContext ctx) {
    return new DecimalLiteral(ctx.getText());
  }

  @Override
  public FieldPathNode visitIntegerLiteral(VirtualFieldPathParser.IntegerLiteralContext ctx) {
    return new IntegerLiteral(ctx.getText());
  }

  @Override
  public FieldPathNode visitStringLiteral(VirtualFieldPathParser.StringLiteralContext ctx) {
    String stringWithQuotes = ctx.getText();
    String value = stringWithQuotes.substring(1, stringWithQuotes.length() - 1);
    return new StringLiteral(value);
  }

  @Override
  public FieldPathNode visitBooleanLiteral(VirtualFieldPathParser.BooleanLiteralContext ctx) {
    return new BooleanLiteral(ctx.getText());
  }

  @Override
  public FieldPathNode visitContextVariable(VirtualFieldPathParser.ContextVariableContext ctx) {
    return ContextVariable.CONTEXT_VARIABLE;
  }

  @Override
  public FieldPathNode visitPredicateCurrentVariable(VirtualFieldPathParser.PredicateCurrentVariableContext ctx) {
    return new PredicateCurrentVariable();
  }

  @Override
  public FieldPathNode visitIdentifier(VirtualFieldPathParser.IdentifierContext ctx) {
    return new Identifier(ctx.getText());
  }

  private FieldReferenceExpression resolveMount(FieldReferenceExpression secondaryPathFieldReference,
      Optional<FieldReferenceExpression> mount) {
    return mount.map(mountPoint -> replaceContextVariable(secondaryPathFieldReference, mountPoint))
        .orElse(secondaryPathFieldReference);
  }
}
