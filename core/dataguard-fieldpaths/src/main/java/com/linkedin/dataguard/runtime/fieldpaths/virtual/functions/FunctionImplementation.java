package com.linkedin.dataguard.runtime.fieldpaths.virtual.functions;

import com.linkedin.dataguard.runtime.fieldpaths.virtual.TypedFunctionInfo;
import com.linkedin.transport.api.StdFactory;
import com.linkedin.transport.api.data.StdData;


public interface FunctionImplementation {
  /**
   * @param arguments value of arguments to the functioncall (ordered)
   * @param typedFunctionInfo resolved function signature information
   * @param stdFactory StdFactory implementation type and data manipulation
   * @return the return value of the function on the input arguments
   */
  StdData apply(StdData[] arguments, TypedFunctionInfo typedFunctionInfo, StdFactory stdFactory);
}
