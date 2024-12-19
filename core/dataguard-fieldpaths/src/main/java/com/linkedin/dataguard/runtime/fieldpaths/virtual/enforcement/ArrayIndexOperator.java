package com.linkedin.dataguard.runtime.fieldpaths.virtual.enforcement;

import com.linkedin.transport.api.StdFactory;
import com.linkedin.transport.api.data.StdArray;
import com.linkedin.transport.api.data.StdData;
import com.linkedin.transport.api.data.StdInteger;
import com.linkedin.transport.api.types.StdArrayType;
import java.util.Objects;
import java.util.Optional;

import static com.linkedin.dataguard.runtime.fieldpaths.virtual.enforcement.StdDataUtils.*;

/**
 * This Operator represents the retrieval or transformation of the element at a given index in an array.
 */
public class ArrayIndexOperator extends Operator {

  private final Optional<Integer> index;
  private final Optional<EnforcerFunctionCall> functionCall;

  private final StdFactory factory;
  private final StdArrayType arrayType;

  public ArrayIndexOperator(int index, StdFactory factory, StdArrayType arrayType) {
    if (index < 0) {
      throw new RuntimeException("Invalid index " + index);
    }
    this.factory = factory;
    this.arrayType = arrayType;
    this.index = Optional.of(index);
    this.functionCall = Optional.empty();
  }

  public ArrayIndexOperator(
      EnforcerFunctionCall enforcerFunctionCall,
      StdFactory factory,
      StdArrayType arrayType) {
    this.factory = factory;
    this.arrayType = arrayType;
    this.index = Optional.empty();
    this.functionCall = Optional.of(enforcerFunctionCall);
  }

  /**
   * Extracts the value at {@code index} from the given {@code input}.
   */
  @Override
  public StdData extract(StdData input, StdData root) {
    StdArray inputArray = checkDataAndCast(input, StdArray.class);
    int evaluatedIndex = getIndex(root);
    if (evaluatedIndex < 0 || evaluatedIndex >= inputArray.size()) {
      // TODO: should we throw an error or return null
      return null;
    }
    return inputArray.get(evaluatedIndex);
  }

  private int getIndex(StdData root) {
    if (this.index.isPresent()) {
      return this.index.get();
    } else {
      StdData functionEvaluation = ExpressionEvaluator.evaluate(functionCall.get(), false, null, root, factory);
      return ((StdInteger) functionEvaluation).get();
    }
  }

  /**
   * If this is the last operator, applies the given {@code action} on the element at {@code index} in the array.
   * Otherwise, invokes the next operators on the element at {@code index}.
   */
  @Override
  public StdData applyAction(StdData input, Action action, Enforcer remainingEnforcerOperators, StdData root) {
    // TODO: should we have a SET api for arrays
    if (input == null) {
      return null;
    }

    int evaluatedIndex = getIndex(root);
    StdArray array = (StdArray) input;
    StdArray output = factory.createArray(arrayType);
    for (int i = 0; i < array.size(); i++) {
      StdData arrayElement = array.get(i);
      if (i == evaluatedIndex) {
        if (remainingEnforcerOperators.getOperators().size() == 0) {
          StdData replacementValue = action.getReplacementValue(arrayElement);
          output.add(replacementValue);
        } else {
          output.add(remainingEnforcerOperators.applyAction(arrayElement, action, root));
        }
      } else {
        output.add(arrayElement);
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
    ArrayIndexOperator that = (ArrayIndexOperator) o;
    return index.equals(that.index) && functionCall.equals(that.functionCall);
  }

  @Override
  public int hashCode() {
    return Objects.hash(index, functionCall);
  }

  @Override
  public String toString() {
    return "[" + index + ']';
  }
}
