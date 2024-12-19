package com.linkedin.dataguard.runtime.fieldpaths.virtual.semantics;

import com.google.common.collect.ImmutableMap;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.TypedFunctionInfo;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.syntaxtree.MembershipOperator;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.syntaxtree.MembershipOperatorExpression;
import com.linkedin.dataguard.runtime.transport.common.FormatSpecificTypeDataProvider;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.functions.FunctionRepository;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.syntaxtree.BinaryArithmeticExpression;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.syntaxtree.BinaryArithmeticOperator;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.syntaxtree.BinaryLogicalOperatorExpression;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.syntaxtree.BooleanLiteral;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.syntaxtree.CollectionLookupReferenceExpression;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.syntaxtree.CollectionPredicateReferenceExpression;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.syntaxtree.CollectionStreamReferenceExpression;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.syntaxtree.ComparisonExpression;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.syntaxtree.ComparisonOperator;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.syntaxtree.ContextVariable;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.syntaxtree.DecimalLiteral;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.syntaxtree.Expression;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.syntaxtree.FieldPathNode;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.syntaxtree.FieldReferenceExpression;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.syntaxtree.FunctionCall;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.syntaxtree.Identifier;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.syntaxtree.IntegerLiteral;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.syntaxtree.Literal;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.syntaxtree.MemberReferenceExpression;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.syntaxtree.NodeAddress;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.syntaxtree.NotExpression;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.syntaxtree.PredicateCurrentVariable;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.syntaxtree.RowSelectorAwareFieldPath;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.syntaxtree.SignedExpression;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.syntaxtree.StringLiteral;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.syntaxtree.SyntaxTreeVisitor;
import com.linkedin.transport.api.types.StdArrayType;
import com.linkedin.transport.api.types.StdBooleanType;
import com.linkedin.transport.api.types.StdDoubleType;
import com.linkedin.transport.api.types.StdFloatType;
import com.linkedin.transport.api.types.StdIntegerType;
import com.linkedin.transport.api.types.StdLongType;
import com.linkedin.transport.api.types.StdMapType;
import com.linkedin.transport.api.types.StdStringType;
import com.linkedin.transport.api.types.StdStructType;
import com.linkedin.transport.api.types.StdType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.linkedin.dataguard.runtime.fieldpaths.virtual.semantics.FieldPathTypes.*;
import static java.lang.String.*;
import static java.util.Objects.*;

public final class SemanticValidator {

  private SemanticValidator() {
  }

  public static StdType validatePathOnInputType(
      RowSelectorAwareFieldPath syntaxTree,
      StdType inputType,
      FormatSpecificTypeDataProvider typeDataProvider) {
    return syntaxTree.accept(
        new SemanticValidatorImpl(typeDataProvider, new HashMap<>(), new HashMap<>()),
        new Context(inputType, Optional.empty()));
  }

  /**
   * Validates {@code syntaxTree} on the {@code inputType}, while building a mapping from every node in the
   * tree to corresponding {@link StdType}.
   */
  public static TypeInfo getTypeMapping(FieldPathNode syntaxTree, StdType inputType, FormatSpecificTypeDataProvider typeDataProvider) {
    Map<NodeAddress, StdType> typeMapping = new HashMap<>();
    Map<NodeAddress, TypedFunctionInfo> functions = new HashMap<>();
    syntaxTree.accept(
        new SemanticValidatorImpl(typeDataProvider, typeMapping, functions),
        new Context(inputType, Optional.empty()));
    return new TypeInfo(ImmutableMap.copyOf(typeMapping), ImmutableMap.copyOf(functions));
  }

  /**
   * <p> Visitor class that traverses a field path syntax tree using a root schema
   * and validates whether operands for every operation have valid types allowed
   * for that operation. </p>
   *
   * <p> A virtual field path is an ordered sequence of operations (say p[1]->p[2]->p[3]...p[n]) on the root of a record.
   * <ul>
   * <li>A `.` operation projects a field out of a struct. </li>
   * <li>A `[?(predicate)]` operation projects a filtered array or map.</li>
   * <li>A `[literal]` operation projects an element out of an array/map.</li>
   * <li>A [:] operation indicates a "transform" projection (similar to Spark array transform), which says that further
   * projections are applied on every element of the current array/map.</li>
   * </ul>
   *
   * An operation performs a projection (i.e. 1,2,3 above), or starts a transform operation (i.e. 4 above). In either
   * case, it indicates a change in the "Type" that becomes the operand for the following operations in the sequence. e.g.
   * in "$.x.y", it is understood that ".y" is performed on the already projected "$.x" field. A sequence
   * `p[1]->...p[k-1]->[:]->p[k+1]...p[n]` indicates that we project an array/map `V` out the root record according to
   * `p[1]...p[k-1]`, and then transform every element of V using sequence `p[k+1]...p[n]`. So p[k+1] applies on the
   * element of the collection.
   * </p>
   *
   * <p> Throughout the validation of a field path, the leaf type for every prefix p[1]...p[i] is recursively determined
   * and used for checking the validity of next operation p[i+1]. The leaf type for the full sequence is sent to DH as the
   * "type" of a field, to be consistent with how types are determined for primary schema fields. </p>
   *
   */
  public static class SemanticValidatorImpl
      extends SyntaxTreeVisitor<Context, StdType> {

    private final FormatSpecificTypeDataProvider typeDataProvider;
    private final Map<NodeAddress, StdType> typeMapping;
    private final Map<NodeAddress, TypedFunctionInfo> functions;

    public SemanticValidatorImpl(
        FormatSpecificTypeDataProvider typeDataProvider,
        Map<NodeAddress, StdType> typeMapping,
        Map<NodeAddress, TypedFunctionInfo> functions) {
      this.typeDataProvider = requireNonNull(typeDataProvider);
      this.typeMapping = requireNonNull(typeMapping);
      this.functions = requireNonNull(functions);
    }

    @Override
    public StdType visitFieldPathNode(FieldPathNode node, Context input) {
      throw new RuntimeException("Unsupported field path node for visitor: " + node);
    }

    @Override
    public StdType visitRowSelectorAwareFieldPath(RowSelectorAwareFieldPath node, Context input) {
      StdType fieldPathType = visit(node.getFieldPath(), input);
      if (node.getRowSelectorPredicate().isPresent()) {
        Expression predicate = node.getRowSelectorPredicate().get();
        StdType predicateType = visit(predicate, new Context(input.getRootType(), Optional.of(input.getRootType())));
        checkTypeAndCast(predicateType, StdBooleanType.class);
      }

      addToTypeMapping(node, fieldPathType);
      return fieldPathType;
    }

    @Override
    public StdType visitFieldReferenceExpression(FieldReferenceExpression node, Context input) {
      throw new RuntimeException("Unsupported FieldReferenceExpression: " + node);
    }

    @Override
    public StdType visitMemberReferenceExpression(MemberReferenceExpression node, Context input) {
      StdType baseType = visit(node.getBase(), input);
      StdStructType structType = checkTypeAndCast(baseType, StdStructType.class);
      Identifier identifier = node.getFieldName();
      int index = getStdStructTypeFieldIndex(structType, identifier.getValue(), typeDataProvider.hasCaseSensitiveFieldNames());

      if (index < 0) {
        throw new SemanticException((format("No field with name %s found in %s", identifier.getValue(), structType)));
      }

      StdType elementType = structType.fieldTypes().get(index);
      typeMapping.put(new NodeAddress(node), elementType);
      return elementType;
    }

    @Override
    public StdType visitCollectionLookupReferenceExpression(CollectionLookupReferenceExpression node, Context input) {
      StdType validatedBaseType = visit(node.getBase(), input);

      boolean isLookupLiteral = node.isLiteralLookup();

      if (!isLookupLiteral && validatedBaseType instanceof StdMapType) {
        throw new UnsupportedOperationException("non-literal lookup for maps is not supported");
      }

      StdType lookupType;
      if (isLookupLiteral) {
        lookupType = visit(node.getLookupLiteral(), input);
      } else {
        lookupType = visit(node.getLookupFunctionCall(), input);
      }

      if (validatedBaseType instanceof StdArrayType) {
        // Handle Arrays
        if (!(lookupType instanceof StdIntegerType)) {

          String found = isLookupLiteral ? node.getLookupLiteral().toString() : node.getLookupFunctionCall().toString();
          throw new SemanticException(
              format("Array index reference only allowed with integer subscript. Found %s (%s) instead.", lookupType, found));
        }

        StdArrayType arrayType = checkTypeAndCast(validatedBaseType, StdArrayType.class);
        StdType elementType = arrayType.elementType();
        typeMapping.put(new NodeAddress(node), elementType);
        return elementType;
      }

      if (validatedBaseType instanceof StdMapType) {
        // Handle maps
        StdMapType mapType = checkTypeAndCast(validatedBaseType, StdMapType.class);
        if (!mapType.keyType().underlyingType().equals(lookupType.underlyingType())) {
          throw new SemanticException(format("Incorrect type for lookup key %s (%s). Expected %s.", node.getLookupLiteral(),
              lookupType.underlyingType(), mapType.keyType().underlyingType()));
        }
        StdType valueType = mapType.valueType();
        typeMapping.put(new NodeAddress(node), valueType);
        return valueType;
      }

      throw new SemanticException(
          format("Lookup operation only allowed on arrays or maps. Found %s instead.", validatedBaseType));
    }

    @Override
    public StdType visitCollectionStreamReferenceExpression(CollectionStreamReferenceExpression node, Context input) {

      if (input.withinPredicate()) {
        // We do not have a mechanism to validate arithmetic/logical expressions on paths that involve
        // [:]. For now, we disallow such operations within predicates. For supporting this, we need
        // something like "retrieval type".
        // For more reference, see https://linkedin.ghe.com/multiproduct/virtual-fields-representation/pull/13
        throw new SemanticException("[:] operation is not supported within a predicate");
      }

      StdType validatedBaseType = visit(node.getBase(), input);

      if (validatedBaseType instanceof StdArrayType) {
        StdArrayType arrayType = checkTypeAndCast(validatedBaseType, StdArrayType.class);
        // [:] changes the leaf type, and adds an array to the nesting. Overall "retrieval" type remains the same.
        StdType elementType = arrayType.elementType();
        typeMapping.put(new NodeAddress(node), elementType);
        return elementType;
      }

      if (validatedBaseType instanceof StdMapType) {
        StdMapType mapType = checkTypeAndCast(validatedBaseType, StdMapType.class);
        // [:] changes the leaf type, and adds a map to the nesting. Overall "retrieval" type remains the same.
        StdType mapEntryType = getMapEntryType(mapType, typeDataProvider);
        typeMapping.put(new NodeAddress(node), mapEntryType);
        return mapEntryType;
      }

      throw new SemanticException(
          format("Only array and map typed columns can be streamed. Found %s with type %s.", node.getBase(),
              validatedBaseType));
    }

    @Override
    public StdType visitPredicateCurrentVariable(PredicateCurrentVariable node, Context input) {
      if (!input.getPredicateCurrentVariableType().isPresent()) {
        throw new IllegalArgumentException("Current variable not present");
      }
      StdType type = input.getPredicateCurrentVariableType().get();
      typeMapping.put(new NodeAddress(node), type);
      return type;
    }

    @Override
    public StdType visitContextVariable(ContextVariable node, Context input) {
      typeMapping.put(new NodeAddress(node), input.getRootType());
      return input.getRootType();
    }

    @Override
    public StdType visitCollectionPredicateReferenceExpression(CollectionPredicateReferenceExpression node,
        Context input) {
      StdType baseType = visit(node.getBase(), input);

      // Determine the type of @ inside the predicate before invoking validation
      StdType predicateRootType = null;
      if (baseType instanceof StdArrayType) {
        StdArrayType arrayType = (StdArrayType) baseType;
        predicateRootType = arrayType.elementType();
      } else if (baseType instanceof StdMapType) {
        StdMapType mapType = (StdMapType) baseType;
        predicateRootType = getMapEntryType(mapType, typeDataProvider);
      } else {
        throw new SemanticException(format("Predicates can only be applied on arrays or maps. Found %s.", baseType));
      }

      StdType predicateType = visit(node.getPredicate(), new Context(input.getRootType(), Optional.of(predicateRootType)));

      if (!(predicateType instanceof StdBooleanType)) {
        throw new SemanticException(format("Predicates should have boolean type. Found %s.", predicateType));
      }

      // Predicate does not change the type, it just filters out elements.
      typeMapping.put(new NodeAddress(node), baseType);
      return baseType;
    }

    @Override
    public StdType visitBinaryArithmeticExpression(BinaryArithmeticExpression node, Context input) {
      StdType leftType = visit(node.getLeftExpression(), input);
      StdType rightType = visit(node.getRightExpression(), input);
      StdType outputType = checkAndGetArithmeticExprType(leftType, rightType, node.getOperator());
      typeMapping.put(new NodeAddress(node), outputType);
      return outputType;
    }

    @Override
    public StdType visitComparisonExpression(ComparisonExpression node, Context input) {
      StdType leftType = visit(node.getLeftExpression(), input);
      StdType rightType = visit(node.getRightExpression(), input);
      validateComparisonTypes(leftType, rightType, node.getOperator());
      StdType booleanType = typeDataProvider.createConcreteStdType("boolean");
      addToTypeMapping(node, booleanType);
      return booleanType;
    }

    @Override
    public StdType visitMembershipOperatorExpression(MembershipOperatorExpression node, Context input) {
      StdType leftType = visit(node.getLeftExpression(), input);
      List<Expression> rightExpressionList = node.getRightExpressionList();
      List<StdType> rightTypes = new ArrayList<>();

      for (Expression rightExpression: rightExpressionList) {
        rightTypes.add(visit(rightExpression, input));
      }

      validateMembershipOperatorTypes(leftType, rightTypes, node.getOperator());
      StdType booleanType = typeDataProvider.createConcreteStdType("boolean");
      addToTypeMapping(node, booleanType);
      return booleanType;
    }

    public static void validateMembershipOperatorTypes(StdType operandOne, List<StdType> operandTwoList, MembershipOperator operator) {
      if (!validateListOfSameType(operandTwoList)) {
        throw new SemanticException(
            format("The list operand %s for the IN operator does not contain the same type of elements", operandTwoList));
      }

      if ((operandOne instanceof StdIntegerType && operandTwoList.get(0) instanceof StdIntegerType)
          || (operandOne instanceof StdDoubleType && operandTwoList.get(0) instanceof StdDoubleType)
          || (operandOne instanceof StdLongType && operandTwoList.get(0) instanceof StdLongType)
          || (operandOne instanceof StdFloatType && operandTwoList.get(0) instanceof StdFloatType)
          || (operandOne instanceof StdStringType && operandTwoList.get(0) instanceof StdStringType)) {
        return;
      }
      throw new SemanticException(
          format("Cannot perform operation '%s' between operands of type %s and %s", operator, operandOne, operandTwoList));
    }

    public static boolean validateListOfSameType(List<StdType> stdTypes) {
      Class<?> type = stdTypes.get(0).getClass();
      for (StdType stdType: stdTypes) {
        if (stdType.getClass() != type) {
          return false;
        }
      }

      return true;
    }

    @Override
    public StdType visitExpression(Expression node, Context input) {
      throw new RuntimeException("Unsupported expression: " + node);
    }

    @Override
    public StdType visitLogicalOperatorExpression(BinaryLogicalOperatorExpression node, Context input) {
      StdType leftType = visit(node.getLeftOperand(), input);
      StdType rightType = visit(node.getRightOperand(), input);
      checkTypeAndCast(leftType, StdBooleanType.class);
      checkTypeAndCast(rightType, StdBooleanType.class);
      addToTypeMapping(node, leftType);
      return leftType;
    }

    @Override
    public StdType visitNotExpression(NotExpression node, Context input) {
      StdType baseType = visit(node.getOperand(), input);
      checkTypeAndCast(baseType, StdBooleanType.class);
      addToTypeMapping(node, baseType);
      return baseType;
    }

    @Override
    public StdType visitSignedExpression(SignedExpression node, Context input) {
      StdType baseType = visit(node.getOperand(), input);

      addToTypeMapping(node, baseType);
      return baseType;
    }

    @Override
    public StdType visitFunctionCall(FunctionCall node, Context input) {
      String functionCallName = node.getFunctionName().getValue();
      List<StdType> argumentTypes = new ArrayList<>();
      for (Expression parameter : node.getParameters()) {
        argumentTypes.add(visit(parameter, input));
      }

      TypedFunctionInfo typedFunctionInfo = FunctionRepository.getTypedFunctionInfo(typeDataProvider, functionCallName, argumentTypes);
      addToTypeMapping(node, typedFunctionInfo.getReturnType());
      addToFunctions(node, typedFunctionInfo);
      return typedFunctionInfo.getReturnType();
    }

    @Override
    public StdType visitLiteral(Literal node, Context input) {
      throw new RuntimeException("Unsupported literal");
    }

    @Override
    public StdType visitBooleanLiteral(BooleanLiteral node, Context input) {
      // TODO: there should be a clean interface to get the typesignature string rather
      //       than hardcoding it. Atleast cache/reuse base types.
      StdType booleanType = typeDataProvider.createConcreteStdType("boolean");
      addToTypeMapping(node, booleanType);
      return booleanType;
    }

    @Override
    public StdType visitDecimalLiteral(DecimalLiteral node, Context input) {
      StdType doubleType = typeDataProvider.createConcreteStdType("double");
      addToTypeMapping(node, doubleType);
      return doubleType;
    }

    @Override
    public StdType visitIntegerLiteral(IntegerLiteral node, Context input) {
      StdType intType = typeDataProvider.createConcreteStdType("integer");
      addToTypeMapping(node, intType);
      return intType;
    }

    @Override
    public StdType visitStringLiteral(StringLiteral node, Context input) {
      StdType strType = typeDataProvider.createConcreteStdType("varchar");
      addToTypeMapping(node, strType);
      return strType;
    }

    public void addToTypeMapping(FieldPathNode node, StdType type) {
      typeMapping.put(new NodeAddress(node), type);
    }

    private void addToFunctions(FunctionCall node, TypedFunctionInfo typedFunctionInfo) {
      functions.put(new NodeAddress(node), typedFunctionInfo);
    }
  }

  // TODO: Support coercion between types, OR support representing longs and doubles
  //       explicitly.
  public static StdType checkAndGetArithmeticExprType(StdType operandOne, StdType operandTwo,
      BinaryArithmeticOperator operator) {
    if ((operandOne instanceof StdIntegerType && operandTwo instanceof StdIntegerType)
        || (operandOne instanceof StdDoubleType && operandTwo instanceof StdDoubleType)
        || (operandOne instanceof StdLongType && operandTwo instanceof StdLongType)
        || (operandOne instanceof StdFloatType && operandTwo instanceof StdFloatType)) {
      return operandOne;
    }
    throw new SemanticException(
        format("Cannot perform operation '%s' between operands of type %s and %s", operator, operandOne, operandTwo));
  }

  // TODO: Support coercion between types, OR support representing longs and doubles
  //       explicitly.
  public static void validateComparisonTypes(StdType operandOne, StdType operandTwo, ComparisonOperator operator) {
    if ((operandOne instanceof StdIntegerType && operandTwo instanceof StdIntegerType)
        || (operandOne instanceof StdDoubleType && operandTwo instanceof StdDoubleType)
        || (operandOne instanceof StdLongType && operandTwo instanceof StdLongType)
        || (operandOne instanceof StdFloatType && operandTwo instanceof StdFloatType)
        || (operandOne instanceof StdStringType && operandTwo instanceof StdStringType)) {
      return;
    }
    throw new SemanticException(
        format("Cannot perform operation '%s' between operands of type %s and %s", operator, operandOne, operandTwo));
  }

  public static void validatePrimitiveType(StdType type) {
    if (type instanceof StdMapType || type instanceof StdStructType || type instanceof StdArrayType) {
      throw new SemanticException("Expected primitive type, found " + type);
    }
  }

  public static void validateTypeForNumericExpression(StdType type) {
    if (type instanceof StdIntegerType || type instanceof StdLongType
        || type instanceof StdFloatType || type instanceof StdDoubleType) {
      return;
    }
    throw new SemanticException("Not a numeric type: " + type.underlyingType());
  }

  public static class Context {

    private final StdType rootType;
    private final Optional<StdType> predicateCurrentVariableType;

    public Context(StdType rootType, Optional<StdType> predicateCurrentVariableType) {
      this.rootType = requireNonNull(rootType);
      this.predicateCurrentVariableType = requireNonNull(predicateCurrentVariableType);
    }

    public StdType getRootType() {
      return rootType;
    }

    public Optional<StdType> getPredicateCurrentVariableType() {
      return predicateCurrentVariableType;
    }

    public boolean withinPredicate() {
      return predicateCurrentVariableType.isPresent();
    }
  }

  public static class TypeInfo {
    private final Map<NodeAddress, StdType> types;
    private final Map<NodeAddress, TypedFunctionInfo> functions;

    public TypeInfo(Map<NodeAddress, StdType> types, Map<NodeAddress, TypedFunctionInfo> functions) {
      this.types = requireNonNull(types);
      this.functions = requireNonNull(functions);
    }

    public Map<NodeAddress, StdType> getTypes() {
      return types;
    }

    public Map<NodeAddress, TypedFunctionInfo> getFunctions() {
      return functions;
    }

    public StdType getType(FieldPathNode node) {
      StdType type = types.get(new NodeAddress(node));
      if (type == null) {
        throw new RuntimeException("Type mapping does not exist for: " + node);
      }
      return type;
    }

    public TypedFunctionInfo getFunction(FieldPathNode node) {
      TypedFunctionInfo function = functions.get(new NodeAddress(node));
      if (function == null) {
        throw new RuntimeException("function mapping does not exist for: " + node);
      }
      return function;
    }
  }
}
