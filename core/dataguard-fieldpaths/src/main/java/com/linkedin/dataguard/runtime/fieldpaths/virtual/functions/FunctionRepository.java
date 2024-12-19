package com.linkedin.dataguard.runtime.fieldpaths.virtual.functions;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.TypedFunctionInfo;
import com.linkedin.dataguard.runtime.transport.common.FormatSpecificTypeDataProvider;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.semantics.SemanticException;
import com.linkedin.transport.api.StdFactory;
import com.linkedin.transport.api.data.StdData;
import com.linkedin.transport.api.types.StdType;
import com.linkedin.transport.typesystem.AbstractBoundVariables;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static com.google.common.collect.ImmutableList.*;
import static java.lang.String.*;


/**
 * This is the source of truth for all functions supported by field enforcement.
 */
public final class FunctionRepository {

  private static final Function<String, FunctionImplementation> NOT_SUPPORTED = functionName -> new FunctionImplementation() {
    @Override
    public StdData apply(StdData[] arguments, TypedFunctionInfo typedFunctionInfo, StdFactory stdFactory) {
      throw new RuntimeException("Execution of function " + functionName + " is not supported");
    }
  };

  private FunctionRepository() {
  }

  public static final FunctionDefinition GET_FDS_INDEX_1D = new FunctionDefinition(
      "getFDSIndex1D",
      ImmutableList.of("array(dimensionType)", "dimensionType"),
      "integer");

  public static final FunctionDefinition CALLISTO_GET_PURPOSE_FOR_PAGEKEY = new FunctionDefinition(
      // Such custom usecase-specific function names are namespaced with `__` for future-proofing
      "callisto__getPurposeForPagekey",
      ImmutableList.of("varchar"),
      "varchar");

  // TODO: allow multiple functions with same name but different signature
  private static final Map<String, FunctionDefinition> FUNCTIONS;
  private static final Map<String, FunctionImplementation> IMPLEMENTATIONS;

  public static FunctionDefinition getFunctionDefinition(String functionName) {
    return FUNCTIONS.get(functionName);
  }

  public static FunctionImplementation getFunctionImplementation(String functionName) {
    return IMPLEMENTATIONS.get(functionName);
  }

  static {
    FUNCTIONS = ImmutableMap.of(
        GET_FDS_INDEX_1D.getFunctionName(), GET_FDS_INDEX_1D,
        CALLISTO_GET_PURPOSE_FOR_PAGEKEY.getFunctionName(), CALLISTO_GET_PURPOSE_FOR_PAGEKEY);

    IMPLEMENTATIONS = ImmutableMap.of(
        GET_FDS_INDEX_1D.getFunctionName(), new GetFDSIndexFunctionImplementation(),
        CALLISTO_GET_PURPOSE_FOR_PAGEKEY.getFunctionName(), NOT_SUPPORTED.apply(CALLISTO_GET_PURPOSE_FOR_PAGEKEY.getFunctionName()));
  }

  /**
   * Resolves a {@link FunctionDefinition} based on its name. This definition may be defined using generic type
   * signature (like `array(T)`). This method will use the provided argument types of the function call to resolve the
   * type signatures of parameters. Throws an exception if arguments can't be mapped to parameter typed signatures.
   *
   * @param typeDataProvider format-specific type data provider which will be used for getting platform-specific types
   * @param functionName unique identifier of the function definition
   * @param actualArgumentTypes arguments provided in the function call.
   * @return a {@link TypedFunctionInfo} which contains fully resolved argument and return types for the function call.
   */
  public static TypedFunctionInfo getTypedFunctionInfo(
      FormatSpecificTypeDataProvider typeDataProvider,
      String functionName,
      List<StdType> actualArgumentTypes) {
    FunctionDefinition definition = FunctionRepository.getFunctionDefinition(functionName);
    if (definition == null) {
      throw new SemanticException(format("Function %s not found", functionName));
    }

    if (definition.argumentCount() != actualArgumentTypes.size()) {
      // Parameter count do not match the argument count, so the function cannot be resolved.
      throw new SemanticException(format("Incorrect argument sizes: expected %s, found %s",
          definition.getArgumentsType(), actualArgumentTypes));
    }

    AbstractBoundVariables variables = typeDataProvider.createAbstractBoundVariables();
    for (int i = 0; i < definition.argumentCount(); i++) {
      // TODO: look at binding results and see if there is a type mismatch
      variables.bind(definition.getArgumentsTypeSignature().get(i), actualArgumentTypes.get(i).underlyingType());
    }

    StdFactory factory = typeDataProvider.getStdFactoryWithBinding(variables);

    StdType resolvedReturnType = factory.createStdType(definition.getReturnType());

    ImmutableList<StdType> resolvedArgumentTypes = definition.getArgumentsType().stream()
        .map(factory::createStdType)
        .collect(toImmutableList());

    return new TypedFunctionInfo(functionName, resolvedArgumentTypes, resolvedReturnType);
  }
}
