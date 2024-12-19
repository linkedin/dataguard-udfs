package com.linkedin.dataguard.runtime.fieldpaths.virtual.enforcement;

import com.linkedin.dataguard.runtime.fieldpaths.virtual.enforcement.expression.EnforcerExpr;
import com.linkedin.transport.api.StdFactory;
import com.linkedin.transport.api.data.StdArray;
import com.linkedin.transport.api.data.StdBoolean;
import com.linkedin.transport.api.data.StdData;
import com.linkedin.transport.api.types.StdArrayType;

import static java.util.Objects.*;


public class PredicatedArrayOperator extends Operator {

  private final EnforcerExpr enforcerExpr;
  private final StdArrayType arrayType;
  private final StdFactory factory;

  public PredicatedArrayOperator(EnforcerExpr enforcerExpr, StdArrayType arrayType, StdFactory factory) {
    this.enforcerExpr = requireNonNull(enforcerExpr);
    this.arrayType = requireNonNull(arrayType);
    this.factory = requireNonNull(factory);
  }

  public EnforcerExpr getEnforcerExpr() {
    return enforcerExpr;
  }

  @Override
  StdData extract(StdData input, StdData root) {
    StdArray array = (StdArray) input;
    StdArray output = factory.createArray(arrayType);
    for (int i = 0; i < array.size(); i++) {
      StdBoolean data = (StdBoolean) ExpressionEvaluator.evaluate(enforcerExpr, true, array.get(i), root, factory);
      if (data != null && data.get()) {
        // If the predicate's result is indeterminate or false, it is not extracted out.
        output.add(array.get(i));
      }
    }
    return output;
  }

  @Override
  StdData applyAction(StdData input, Action action, Enforcer nextOperators, StdData root) {
    if (input == null) {
      // if the array itself is null, there is no transformation to be applied
      return input;
    }

    StdArray array = (StdArray) input;

    // #1 Identify the elements in the array which satisfy the predicate. Keep a mapping of their indices.
    StdArray furtherTransform = factory.createArray(arrayType);
    int[] inputToOutput = new int[array.size()];
    for (int i = 0; i < array.size(); i++) {
      StdBoolean data = (StdBoolean) ExpressionEvaluator.evaluate(enforcerExpr, true, array.get(i), root, factory);
      if (data != null && data.get()) {
        // If the predicate's result is indeterminate or false, the corresponding element is not transformed.
        furtherTransform.add(array.get(i));
        inputToOutput[i] = furtherTransform.size();
      }
    }

    // #2 Apply further operators (if present) or redaction on the identified elements
    StdArray transformedOutput;
    if (nextOperators.getOperators().size() == 0) {
      transformedOutput = factory.createArray(arrayType);
      for (int i = 0; i < furtherTransform.size(); i++) {
        transformedOutput.add(action.getReplacementValue(array.get(i)));
      }
    } else {
      transformedOutput = (StdArray) nextOperators.applyAction(furtherTransform, action, root);
    }

    // #3 Create the output array with updated values of elements that satisfy the predicate
    StdArray output = factory.createArray(arrayType);
    for (int i = 0; i < array.size(); i++) {
      if (inputToOutput[i] > 0) {
        output.add(transformedOutput.get(inputToOutput[i] - 1));
      } else {
        output.add(array.get(i));
      }
    }

    return output;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PredicatedArrayOperator that = (PredicatedArrayOperator) o;
    return enforcerExpr.equals(that.enforcerExpr);
  }

  @Override
  public int hashCode() {
    return hash(enforcerExpr);
  }
}
