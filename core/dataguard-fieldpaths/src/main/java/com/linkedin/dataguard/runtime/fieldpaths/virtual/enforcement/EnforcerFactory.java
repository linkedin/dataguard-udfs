package com.linkedin.dataguard.runtime.fieldpaths.virtual.enforcement;

import com.google.common.collect.ImmutableList;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.enforcement.expression.EnforcerMembershipOperatorExpr;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.syntaxtree.MembershipOperatorExpression;
import com.linkedin.dataguard.runtime.transport.common.FormatSpecificTypeDataProvider;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.enforcement.expression.EnforcerBinaryArithmeticExpr;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.enforcement.expression.EnforcerBinaryLogicalExpr;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.enforcement.expression.EnforcerComparisonExpr;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.enforcement.expression.EnforcerExpr;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.enforcement.expression.EnforcerFieldPathExpression;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.enforcement.expression.EnforcerLiteral;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.enforcement.expression.EnforcerNotExpr;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.enforcement.expression.EnforcerSignedExpr;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.semantics.SemanticValidator;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.semantics.SemanticValidator.TypeInfo;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.syntaxtree.BinaryArithmeticExpression;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.syntaxtree.BinaryLogicalOperatorExpression;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.syntaxtree.CollectionLookupReferenceExpression;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.syntaxtree.CollectionPredicateReferenceExpression;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.syntaxtree.CollectionStreamReferenceExpression;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.syntaxtree.ComparisonExpression;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.syntaxtree.ContextVariable;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.syntaxtree.Expression;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.syntaxtree.FieldPathNode;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.syntaxtree.FieldReferenceExpression;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.syntaxtree.FunctionCall;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.syntaxtree.IntegerLiteral;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.syntaxtree.Literal;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.syntaxtree.MemberReferenceExpression;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.syntaxtree.NotExpression;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.syntaxtree.PredicateCurrentVariable;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.syntaxtree.RowSelectorAwareFieldPath;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.syntaxtree.SignedExpression;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.syntaxtree.SyntaxTreeVisitor;
import com.linkedin.transport.api.types.StdArrayType;
import com.linkedin.transport.api.types.StdIntegerType;
import com.linkedin.transport.api.types.StdMapType;
import com.linkedin.transport.api.types.StdType;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.linkedin.dataguard.runtime.fieldpaths.virtual.enforcement.EnforcerBaseElement.*;
import static com.linkedin.dataguard.runtime.fieldpaths.virtual.semantics.FieldPathTypes.*;
import static java.util.Objects.*;


/**
 * Utility class to create enforcer from syntax tree or field paths
 */
public final class EnforcerFactory {

  private EnforcerFactory() {
  }

  /**
   * Given a field path and an input type, creates the plan for performing transformation or extraction on
   * {@link com.linkedin.transport.api.data.StdData} objects of {@code type}.
   */
  public static Enforcer createEnforcer(
      RowSelectorAwareFieldPath resolvedPath,
      StdType type,
      FormatSpecificTypeDataProvider typeDataProvider) {
    TypeInfo types = SemanticValidator.getTypeMapping(resolvedPath, type, typeDataProvider);
    return EnforcerFactory.create(resolvedPath, types, typeDataProvider);
  }

  public static Enforcer create(
      FieldPathNode tree,
      TypeInfo typeInfo,
      FormatSpecificTypeDataProvider typeDataProvider) {
    EnforcerExpr enforcerExpr = new Visitor(typeDataProvider, typeInfo).visit(tree, new Context());
    if (enforcerExpr instanceof EnforcerFieldPathExpression) {
      EnforcerFieldPathExpression fieldPathExpression = (EnforcerFieldPathExpression) enforcerExpr;
      if (fieldPathExpression.getEnforcerBaseElement() == ROOT_ELEMENT) {
        return fieldPathExpression.getEnforcer();
      }
    }
    throw new RuntimeException("Bad field path tree, expected a valid field path with optional row selector");
  }

  /**
   * Traverses the syntax tree and prepares an {@link EnforcerExpr}, which can be used
   * for extracting values out of input data or transforming the input using desired actions.
   *
   * At a high level, this visitor
   * - takes in field path representation operating on types, and
   * - produces a structure that can operate on data.
   *
   * e.g. For a path like "$.x[:].z[2], the tree and enforcer are shown
   * side by side.
   *
   *                                     │
   *           SyntaxTree                │         Enforcer
   *                                     │                                         T'
   *                        ArrayIndex   │  ┌────────────────────────┐  ┌───────────────────────┐
   *                           ▲ ▲       │  │    output StdData      │  │  output StdData       │
   *                     ┌─────┘ └────┐  │  │          ▲             │  │          ▲            │
   *                     │            │  │  │          │             │  │          │            │
   *               FieldAccess        │  │  │ ArrayTransform (T')    │  │ ArrayIndexAccess 2    │
   *                   ▲ ▲            2  │  │          ▲             │  │          ▲            │
   *             ┌─────┘ └───┐           │  │          │             │  │          │            │
   *             │           │           │  │ StructFieldAccess ".x" │  │ StructFieldAccess ".z"│
   *         Stream [:]      z           │  │          ▲             │  │          ▲            │
   *             ▲                       │  │          │             │  │          │            │
   *       ┌─────┘                       │  │     input StdData      │  │     input StdData     │
   *       │                             │  └────────────────────────┘  └───────────────────────┘
   *  FieldAccess                        │
   *      ▲ ▲                            │
   * ┌────┘ └────┐                       │
   * │           │                       │
   * $           x                       │
   *                                     │
   *
   */
  public static class Visitor
      extends SyntaxTreeVisitor<Context, EnforcerExpr> {
    private final FormatSpecificTypeDataProvider typeDataProvider;
    private final TypeInfo types;

    public Visitor(FormatSpecificTypeDataProvider typeDataProvider, TypeInfo types) {
      this.typeDataProvider = requireNonNull(typeDataProvider);
      this.types = requireNonNull(types);
    }

    @Override
    public EnforcerExpr visitFieldPathNode(FieldPathNode node, Context input) {
      throw new RuntimeException("Unsupported field path node for visitor: " + node);
    }

    @Override
    public EnforcerExpr visitRowSelectorAwareFieldPath(RowSelectorAwareFieldPath node, Context context) {
      EnforcerExpr enforcerExpr = visit(node.getFieldPath(), context);
      if (!node.getRowSelectorPredicate().isPresent()) {
        return enforcerExpr;
      }
      EnforcerExpr rowSelectorEnforcerExpr = visit(node.getRowSelectorPredicate().get(), context);
      return ((EnforcerFieldPathExpression) enforcerExpr)
          .withRowSelector(rowSelectorEnforcerExpr, typeDataProvider.getStdFactory());
    }

    @Override
    public EnforcerExpr visitFieldReferenceExpression(FieldReferenceExpression node, Context context) {
      throw new RuntimeException("Unsupported FieldReferenceExpression: " + node);
    }

    @Override
    public EnforcerExpr visitMemberReferenceExpression(MemberReferenceExpression node, Context input) {
      Operator projection = new StructFieldAccess(node.getFieldName().getValue());
      input.addOperator(projection, getType(node));
      return visit(node.getBase(), input);
    }

    @Override
    public EnforcerExpr visitCollectionLookupReferenceExpression(
        CollectionLookupReferenceExpression node, Context context) {
      StdType baseType = getType(node.getBase());

      StdType lookupType;
      if (node.isLiteralLookup()) {
        lookupType = getType(node.getLookupLiteral());
      } else {
        if (baseType instanceof StdMapType) {
          throw new RuntimeException("Function call based lookup not supported for map type");
        }
        lookupType = getType(node.getLookupFunctionCall());
      }

      if (baseType instanceof StdArrayType) {
        // Handle Arrays
        if (!(lookupType instanceof StdIntegerType)) {
          throw new RuntimeException("This should have been caught in semantic validation");
        }

        if (node.isLiteralLookup()) {
          // TODO: support non-literal indices
          int index = ((IntegerLiteral) node.getLookupLiteral()).getValue();
          context.addOperator(
              new ArrayIndexOperator(index, typeDataProvider.getStdFactory(), (StdArrayType) baseType), ((StdArrayType) baseType).elementType());
          return visit(node.getBase(), context);
        } else {
          context.addOperator(
              new ArrayIndexOperator(
                  (EnforcerFunctionCall) visit(node.getLookupFunctionCall(), context),
                  typeDataProvider.getStdFactory(),
                  (StdArrayType) baseType),
              ((StdArrayType) baseType).elementType());
          return visit(node.getBase(), context);
        }
      }

      if (baseType instanceof StdMapType) {
        // Handle maps
        StdMapType mapType = checkTypeAndCast(baseType, StdMapType.class);
        if (!mapType.keyType().underlyingType().equals(lookupType.underlyingType())) {
          throw new RuntimeException("This should've been caught in semantic validation");
        }

        EnforcerLiteral key = (EnforcerLiteral) visit(node.getLookupLiteral(), new Context());
        context.addOperator(new MapKeyLookup(key), mapType.valueType());
        return visit(node.getBase(), context);
      }

      throw new RuntimeException("This should've been caught in semantic validation");
    }

    private StdType getType(FieldPathNode base) {
      return types.getType(base);
    }

    @Override
    public EnforcerExpr visitFunctionCall(FunctionCall node, Context context) {
      return new EnforcerFunctionCall(
          types.getFunction(node),
          node.getParameters().stream()
              .map(param -> visit(param, new Context()))
              .collect(Collectors.toList()));
    }

    @Override
    public EnforcerExpr visitCollectionStreamReferenceExpression(
        CollectionStreamReferenceExpression node, Context context) {
      // Build an enforcer from the context we have so far, this will
      // be applied on elements of the array/map on which this streaming
      // operation is being performed.
      EnforcerFieldPathExpression elementEnforcerExpr = context.buildEnforcerExpr(PREDICATE_CURRENT_ELEMENT, getType(node));
      Context newContext = new Context();
      StdType baseLeafType = getType(node.getBase());

      if (baseLeafType instanceof StdArrayType) {
        newContext.addOperator(
            new ArrayTransformOperator(elementEnforcerExpr, typeDataProvider.getStdFactory(), (StdArrayType) baseLeafType),
            getArrayType(elementEnforcerExpr.getType(), typeDataProvider.getStdFactory()));
      } else if (baseLeafType instanceof StdMapType) {
        // TODO: implement row creation and MapTransformExpression Support
        throw new UnsupportedOperationException("unsupported so far");
      } else {
        throw new RuntimeException("Should've been caught in semantic validation");
      }
      return visit(node.getBase(), newContext);
    }

    @Override
    public EnforcerExpr visitPredicateCurrentVariable(PredicateCurrentVariable node, Context input) {
      return input.buildEnforcerExpr(PREDICATE_CURRENT_ELEMENT, getType(node));
    }

    @Override
    public EnforcerExpr visitContextVariable(ContextVariable node, Context input) {
      // We reached the root of the record, build and return the enforcer
      return input.buildEnforcerExpr(ROOT_ELEMENT, getType(node));
    }

    @Override
    public EnforcerExpr visitCollectionPredicateReferenceExpression(
        CollectionPredicateReferenceExpression node,
        Context input) {

      FieldPathNode base = node.getBase();
      StdType baseLeafType = getType(base);
      if (baseLeafType instanceof StdArrayType) {
        input.addOperator(
            new PredicatedArrayOperator(
                visit(node.getPredicate(), new Context()),
                (StdArrayType) baseLeafType,
                typeDataProvider.getStdFactory()),
            getType(node));
      } else if (baseLeafType instanceof StdMapType) {
        throw new UnsupportedOperationException("Predicates on maps not supported yet");
      }
      return visit(node.getBase(), input);
    }

    @Override
    public EnforcerExpr visitBinaryArithmeticExpression(BinaryArithmeticExpression node, Context input) {
      return new EnforcerBinaryArithmeticExpr(
          visit(node.getLeftExpression(), new Context()),
          visit(node.getRightExpression(), new Context()),
          node.getOperator());
    }

    @Override
    public EnforcerExpr visitComparisonExpression(ComparisonExpression node, Context input) {
      return new EnforcerComparisonExpr(
          visit(node.getLeftExpression(), new Context()),
          visit(node.getRightExpression(), new Context()),
          node.getOperator(),
          typeDataProvider.getStdFactory());
    }

    @Override
    public EnforcerExpr visitMembershipOperatorExpression(MembershipOperatorExpression node, Context input) {
      List<EnforcerExpr> rightExpressionList = new ArrayList<>();
      for (Expression expression: node.getRightExpressionList()) {
        rightExpressionList.add(visit(expression, input));
      }

      return new EnforcerMembershipOperatorExpr(
          visit(node.getLeftExpression(), new Context()),
          rightExpressionList,
          node.getOperator(),
          typeDataProvider.getStdFactory());
    }

    @Override
    public EnforcerExpr visitExpression(Expression node, Context input) {
      throw new RuntimeException("Unsupported expression: " + node);
    }

    @Override
    public EnforcerExpr visitLogicalOperatorExpression(BinaryLogicalOperatorExpression node, Context input) {
      return new EnforcerBinaryLogicalExpr(
          visit(node.getLeftOperand(), new Context()),
          visit(node.getRightOperand(), new Context()),
          node.getOperator(),
          typeDataProvider.getStdFactory());
    }

    @Override
    public EnforcerExpr visitNotExpression(NotExpression node, Context input) {
      return new EnforcerNotExpr(visit(node.getOperand(), new Context()), typeDataProvider.getStdFactory());
    }

    @Override
    public EnforcerExpr visitSignedExpression(SignedExpression node, Context input) {
      return new EnforcerSignedExpr(
          node.isNegate(),
          visit(node.getOperand(), new Context()));
    }

    @Override
    public EnforcerExpr visitLiteral(Literal node, Context input) {
      return new EnforcerLiteral(
          getType(node), node.getLiteralValue(), node.toStdData(typeDataProvider.getStdFactory()));
    }
  }

  private static class Context {

    // This is populated while we're going down a FieldReferenceExpression
    // chain.
    private final EnforcerBuilder enforcerBuilder;
    private Optional<StdType> resultantType;

    Context() {
      this.resultantType = Optional.empty();
      this.enforcerBuilder = new EnforcerBuilder();
    }

    void addOperator(Operator operator, StdType type) {
      enforcerBuilder.add(operator);
      if (!resultantType.isPresent()) {
        resultantType = Optional.of(requireNonNull(type));
      }
    }

    public EnforcerFieldPathExpression buildEnforcerExpr(EnforcerBaseElement baseElement, StdType type) {
      return new EnforcerFieldPathExpression(
          baseElement,
          enforcerBuilder.build(),
          resultantType.orElse(type));
    }
  }

  private static class EnforcerBuilder {
    private final ImmutableList.Builder<Operator> operators;

    EnforcerBuilder() {
      this.operators = ImmutableList.builder();
    }

    public void add(Operator operator) {
      operators.add(operator);
    }

    public Enforcer build() {
      return new Enforcer(operators.build().reverse());
    }
  }
}
