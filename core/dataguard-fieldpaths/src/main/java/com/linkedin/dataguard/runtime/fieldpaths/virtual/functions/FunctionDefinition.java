package com.linkedin.dataguard.runtime.fieldpaths.virtual.functions;

import com.linkedin.transport.typesystem.TypeSignature;
import java.util.List;

import static com.google.common.collect.ImmutableList.*;
import static java.util.Objects.*;


/**
 * Contains function signature: name, arguments and return type.
 *
 * The arguments and return types are represented as type signatures, because they may reference "generic" types.
 *
 * For example, `getFDSFunction1D` can take array(string) or array(int) as the first argument. e.g.
 * - getFDSFunction1D(indices0, 'fooFilter')       [where indices0 is a string array] OR
 * - getFDSFunction1D(indices0, 500)               [where indices0 is an integer array]
 *
 * Both the above function calls will map to the same function definition, but with different signatures. So they will
 * be defined commonly using "generic" argument types (i.e. [`array(dimensionType)`, `dimensionType`].
 */
public class FunctionDefinition {
  private final String functionName;
  private final List<String> argumentsType;
  private final String returnType;

  // cached parsed type signatures
  private final List<TypeSignature> argumentsTypeSignature;
  private final TypeSignature returnTypeSignature;

  public FunctionDefinition(String functionName, List<String> argumentsType, String returnType) {
    this.functionName = requireNonNull(functionName);
    this.argumentsType = requireNonNull(argumentsType);
    this.returnType = requireNonNull(returnType);

    this.argumentsTypeSignature = argumentsType.stream()
        .map(TypeSignature::parse)
        .collect(toImmutableList());
    this.returnTypeSignature = TypeSignature.parse(returnType);
  }

  public String getFunctionName() {
    return functionName;
  }

  public int argumentCount() {
    return argumentsType.size();
  }

  public List<String> getArgumentsType() {
    return argumentsType;
  }

  public String getReturnType() {
    return returnType;
  }

  public List<TypeSignature> getArgumentsTypeSignature() {
    return argumentsTypeSignature;
  }

  public TypeSignature getReturnTypeSignature() {
    return returnTypeSignature;
  }
}
