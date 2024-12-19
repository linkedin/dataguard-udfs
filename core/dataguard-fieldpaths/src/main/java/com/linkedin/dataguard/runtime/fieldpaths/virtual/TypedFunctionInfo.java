package com.linkedin.dataguard.runtime.fieldpaths.virtual;

import com.linkedin.transport.api.types.StdType;
import java.util.List;


/**
 * Represents the function signature after the argument and return types have been fully resolved.
 */
public class TypedFunctionInfo {
  private final String functionName;
  private final List<? extends StdType> argumentTypes;
  private final StdType returnType;

  public TypedFunctionInfo(String functionName, List<? extends StdType> argumentTypes, StdType returnType) {
    this.functionName = functionName;
    this.argumentTypes = argumentTypes;
    this.returnType = returnType;
  }

  public String getFunctionName() {
    return functionName;
  }

  public List<? extends StdType> getArgumentTypes() {
    return argumentTypes;
  }

  public StdType getReturnType() {
    return returnType;
  }

  @Override
  public String toString() {
    return "TypedFunctionInfo{" + "functionName='" + functionName + '\'' + ", argumentTypes=" + argumentTypes
        + ", returnType=" + returnType + '}';
  }
}
