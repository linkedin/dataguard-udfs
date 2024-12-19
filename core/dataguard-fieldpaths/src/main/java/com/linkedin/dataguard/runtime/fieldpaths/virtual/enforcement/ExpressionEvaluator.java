package com.linkedin.dataguard.runtime.fieldpaths.virtual.enforcement;

import com.linkedin.dataguard.runtime.fieldpaths.virtual.enforcement.expression.EnforcerBinaryArithmeticExpr;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.enforcement.expression.EnforcerBinaryLogicalExpr;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.enforcement.expression.EnforcerComparisonExpr;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.enforcement.expression.EnforcerExpr;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.enforcement.expression.EnforcerFieldPathExpression;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.enforcement.expression.EnforcerLiteral;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.enforcement.expression.EnforcerNotExpr;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.enforcement.expression.EnforcerSignedExpr;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.functions.FunctionRepository;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.syntaxtree.ComparisonOperator;
import com.linkedin.transport.api.StdFactory;
import com.linkedin.transport.api.data.StdBoolean;
import com.linkedin.transport.api.data.StdData;
import com.linkedin.transport.api.data.StdDouble;
import com.linkedin.transport.api.data.StdFloat;
import com.linkedin.transport.api.data.StdInteger;
import com.linkedin.transport.api.data.StdLong;
import com.linkedin.transport.api.data.StdString;
import com.linkedin.transport.api.types.StdDoubleType;
import com.linkedin.transport.api.types.StdFloatType;
import com.linkedin.transport.api.types.StdIntegerType;
import com.linkedin.transport.api.types.StdLongType;
import com.linkedin.transport.api.types.StdStringType;
import com.linkedin.transport.api.types.StdType;
import java.util.Optional;
import javax.annotation.Nullable;

import static com.linkedin.dataguard.runtime.fieldpaths.virtual.functions.FunctionRepository.*;
import static java.util.Objects.*;


/**
 * Provides utilities for evaluating expressions (for predicates in the path).
 */
public final class ExpressionEvaluator {

  private ExpressionEvaluator() {
  }

  /**
   * Given an expression (e.g. @.x.y == 6, $.f.y == @.t), returns the value of the expression on given input data.
   *
   * @param expression the input expression to be evaluated
   * @param hasCurrentElementReference does the currentElement parameter represent a reference to "@" value? e.g.
   *        if expression is being evaluated within a predicate, @ is the current element in the collection. However,
   *        @ is not considered valid within a subscript operation. This flag represents whether the caller provides a
   *       valid "@" reference.
   * @param currentElement the value for any reference to "@" (AKA current element) in {@code expression}
   * @param rootElement the value for any reference to "$" (AKA root element) in the {@code expression}
   * @param factory StdFactory object for data manipulation (or creation of primitives like "true")
   * @return the value of the expression, represented as StdData
   */
  public static StdData evaluate(
      EnforcerExpr expression,
      boolean hasCurrentElementReference,
      StdData currentElement,
      StdData rootElement,
      StdFactory factory) {
    return new Visitor(factory).visit(expression,
        new Context(rootElement, hasCurrentElementReference, currentElement));
  }

  public static class Visitor extends EnforcerExprVisitor<Context, StdData> {

    private final StdFactory factory;

    public Visitor(StdFactory factory) {
      this.factory = requireNonNull(factory);
    }

    @Override
    public StdData visitEnforcerExpr(EnforcerExpr expr, Context input) {
      return null;
    }

    @Override
    public StdData visitEnforcerBinaryArithmeticExpr(EnforcerBinaryArithmeticExpr node, Context input) {
      // TODO implement type-specific operations
      throw new UnsupportedOperationException("Not supported yet");
    }

    @Override
    public StdData visitEnforcerBinaryLogicalExpr(EnforcerBinaryLogicalExpr node, Context input) {
      StdBoolean left = (StdBoolean) visit(node.getLeft(), input);
      StdBoolean right = (StdBoolean) visit(node.getRight(), input);

      if (left == null && right == null) {
        // result is indeterminate if both are null
        return null;
      }

      switch (node.getOperator()) {
        case OR:
          if (left == null && right.get() || (right == null && left.get())) {
            // if atleast one is true, the result is true
            return factory.createBoolean(true);
          } else if (left == null || right == null) {
            // result is indeterminate
            return null;
          }
          // TODO: cache true and false objects
          return factory.createBoolean(left.get() || right.get());
        case AND:
          if (left == null && !right.get() || (right == null && !left.get())) {
            // if atleast one is false, the result is false
            return factory.createBoolean(false);
          } else if (left == null || right == null) {
            // result is indeterminate
            return null;
          }
          return factory.createBoolean(left.get() && right.get());
        default:
          throw new UnsupportedOperationException("Unsupported operator " + node.getOperator());
      }
    }

    @Override
    public StdData visitEnforcerComparisonExpr(EnforcerComparisonExpr node, Context input) {
      JavaPrimitive left = getJavaPrimitive(visit(node.getLeft(), input), node.getLeft().getType());
      JavaPrimitive right = getJavaPrimitive(visit(node.getRight(), input), node.getRight().getType());
      Optional<Boolean> result = evalComparisonPredicate(left, right, node.getOperator());
      return result.isPresent() ? factory.createBoolean(result.get()) : null;
    }

    @Override
    public StdData visitEnforcerFieldPathExpression(EnforcerFieldPathExpression node, Context input) {
      switch (node.getEnforcerBaseElement()) {
        case PREDICATE_CURRENT_ELEMENT:
          if (!input.isCurrentElementReferencePresent()) {
            // Reference to "@" is only allowed within a predicate or an array transform. Throw an
            // error if it's not found.
            throw new RuntimeException("Current element @ is not available in this context");
          }
          return node.getEnforcer().extract(input.getCurrentElement(), input.getRootElement());
        case ROOT_ELEMENT:
          return node.getEnforcer().extract(input.getRootElement(), input.getRootElement());
        default:
          throw new RuntimeException("unknown expression");
      }
    }

    @Override
    public StdData visitEnforcerLiteral(EnforcerLiteral node, Context input) {
      return node.getStdValue();
    }

    @Override
    public StdData visitEnforcerNotExpr(EnforcerNotExpr node, Context input) {
      JavaPrimitive base = getJavaPrimitive(visit(node.getOperand(), input), node.getOperand().getType());
      Boolean result = base != null ? (Boolean) base.getValue() : null;
      // TODO: handle nulls;
      return result != null ? factory.createBoolean(!result) : null;
    }

    @Override
    public StdData visitEnforcerSignedExpr(EnforcerSignedExpr node, Context input) {
      throw new UnsupportedOperationException("Not supported yet");
    }

    @Override
    public StdData visitEnforcerFunctionCall(EnforcerFunctionCall node, Context input) {
      if (node.getTypedFunctionInfo().getFunctionName().equals(GET_FDS_INDEX_1D.getFunctionName())) {
        StdData[] arguments = new StdData[node.getArguments().size()];
        for (int i = 0; i < arguments.length; i++) {
          arguments[i] = visit(node.getArguments().get(i), input);
        }
        // TODO: define a generic interface to resolve function implementation given a function definition
        return FunctionRepository.getFunctionImplementation(GET_FDS_INDEX_1D.getFunctionName())
            .apply(arguments, node.getTypedFunctionInfo(), factory);
      }
      throw new RuntimeException("Cannot find implementation for function " + node.getTypedFunctionInfo().getFunctionName());
    }
  }

  public static JavaPrimitive getJavaPrimitive(StdData stdData, StdType type) {
    if (stdData == null) {
      return null;
    }

    if (type instanceof StdIntegerType) {
      int intValue = ((StdInteger) stdData).get();
      return new JavaPrimitive(intValue, Integer.class, type);
    }
    if (type instanceof StdLongType) {
      long longValue = ((StdLong) stdData).get();
      return new JavaPrimitive(longValue, Long.class, type);
    }
    if (type instanceof StdFloatType) {
      float floatValue = ((StdFloat) stdData).get();
      return new JavaPrimitive(floatValue, Float.class, type);
    }
    if (type instanceof StdDoubleType) {
      double doubleValue = ((StdDouble) stdData).get();
      return new JavaPrimitive(doubleValue, Double.class, type);
    }
    if (type instanceof StdStringType) {
      String stringValue = ((StdString) stdData).get();
      return new JavaPrimitive(stringValue, String.class, type);
    }

    throw new RuntimeException("Unsupported primitive: " + stdData);
  }

  public static class Context {
    private final StdData rootElement;
    private final boolean currentElementReferencePresent;
    private final StdData currentElement;

    public Context(@Nullable StdData rootElement, boolean currentElementReferencePresent, @Nullable StdData currentElement) {
      this.rootElement = rootElement;
      this.currentElementReferencePresent = currentElementReferencePresent;
      this.currentElement = currentElement;
    }

    public StdData getRootElement() {
      return rootElement;
    }

    public boolean isCurrentElementReferencePresent() {
      return currentElementReferencePresent;
    }

    public StdData getCurrentElement() {
      return currentElement;
    }
  }

  /**
   * Performs comparison between two {@link JavaPrimitive} objects given the operator. Returns the result of comparison.
   * @param left left operand
   * @param right right operand
   * @param operator comparison operator
   * @return an {@link Optional} boolean. If any of the operands is null, the result is Optional.empty(). Otherwise, it
   * contains the result of comparison. We return an optional here instead of returning null to please
   * SpotBugs NP_BOOLEAN_RETURN_NULL.
   * @return
   */
  public static Optional<Boolean> evalComparisonPredicate(JavaPrimitive left, JavaPrimitive right, ComparisonOperator operator) {
    if (left == null || right == null) {
      return Optional.empty();
    }

    // TODO: handle floating point comparison
    switch (operator) {
      case EQUALS:
        return Optional.of(left.getValue().equals(right.getValue()));
      case NOT_EQUALS:
        return Optional.of(!left.getValue().equals(right.getValue()));
      case LT:
        return Optional.of(left.getValue().compareTo(right.getValue()) < 0);
      case LTE:
        return Optional.of(left.getValue().compareTo(right.getValue()) <= 0);
      case GT:
        return Optional.of(left.getValue().compareTo(right.getValue()) > 0);
      case GTE:
        return Optional.of(left.getValue().compareTo(right.getValue()) >= 0);
      default:
        throw new IllegalStateException("unrecognized operator");
    }
  }

  public final static class JavaPrimitive<T> {
    private final Comparable value;
    private final Class<? extends Comparable> clazz;
    private final StdType type;

    private JavaPrimitive(Comparable value, Class clazz, StdType type) {
      this.value = value;
      this.clazz = clazz;
      this.type = type;
    }

    public Comparable getValue() {
      return value;
    }

    // TODO: currently unused, we can remove this if we do not see any use of this information in near term. (e.g.
    //       for casting)
    public Class getClazz() {
      return clazz;
    }

    public StdType getType() {
      return type;
    }
  }
}
