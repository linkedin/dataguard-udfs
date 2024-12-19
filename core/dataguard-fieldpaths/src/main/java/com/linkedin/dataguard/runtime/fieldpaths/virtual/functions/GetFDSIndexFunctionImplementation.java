package com.linkedin.dataguard.runtime.fieldpaths.virtual.functions;

import com.linkedin.dataguard.runtime.fieldpaths.virtual.TypedFunctionInfo;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.enforcement.ExpressionEvaluator;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.syntaxtree.ComparisonOperator;
import com.linkedin.transport.api.StdFactory;
import com.linkedin.transport.api.data.StdArray;
import com.linkedin.transport.api.data.StdData;
import java.util.Optional;

import static com.linkedin.dataguard.runtime.fieldpaths.virtual.enforcement.ExpressionEvaluator.*;


/**
 * The function implementation for `getFDSIndex1D`.
 */
public final class GetFDSIndexFunctionImplementation implements FunctionImplementation {

  public GetFDSIndexFunctionImplementation() {
  }

  /**
   * Performs a linear search for a value on a given array. The value must be primitive.
   *
   * arguments[0] represents the input array.
   * arguments[1] contains the value which needs to be searched in the input array.
   *
   * The assumption is that FDS sparse tensor arrays will contain the search value exactly once.
   *
   * @return array index of the element that matches the value in arguments[1]. If not found, returns -1.
   */
  @Override
  public StdData apply(StdData[] arguments, TypedFunctionInfo typedFunctionInfo, StdFactory factory) {
    StdArray array = (StdArray) arguments[0];
    StdData data = arguments[1];
    ExpressionEvaluator.JavaPrimitive dataObject = getJavaPrimitive(data, typedFunctionInfo.getArgumentTypes().get(1));

    if (dataObject == null) {
      throw new RuntimeException("The search term in getFDSIndex1D must be non-null");
    }

    for (int i = 0; i < array.size(); i++) {
      ExpressionEvaluator.JavaPrimitive arrayElement = getJavaPrimitive(array.get(i), typedFunctionInfo.getArgumentTypes().get(1));
      Optional<Boolean> comparisonResult = evalComparisonPredicate(dataObject, arrayElement, ComparisonOperator.EQUALS);
      if (arrayElement != null && comparisonResult.isPresent() && comparisonResult.get()) {
        return factory.createInteger(i);
      }
    }
    return factory.createInteger(-1);
  }
}
