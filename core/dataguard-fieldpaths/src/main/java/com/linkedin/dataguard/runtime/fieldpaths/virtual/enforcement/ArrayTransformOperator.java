package com.linkedin.dataguard.runtime.fieldpaths.virtual.enforcement;

import com.linkedin.dataguard.runtime.fieldpaths.virtual.enforcement.expression.EnforcerFieldPathExpression;
import com.linkedin.transport.api.StdFactory;
import com.linkedin.transport.api.data.StdArray;
import com.linkedin.transport.api.data.StdData;
import com.linkedin.transport.api.types.StdArrayType;

import static com.linkedin.dataguard.runtime.fieldpaths.virtual.enforcement.EnforcerBaseElement.*;
import static java.util.Objects.*;


/**
 * This operator corresponds to [:] operation in the field path. Operations that follow [:] (in the fieldpath)
 * apply on every element of the array. This knowledge of the operations following [:] is embedded into the
 * {@code elementEnforcer}.
 *
 * This operator applies the transformation (indicated by {@code elementEnforcer}) on every element of input array.
 */
public class ArrayTransformOperator
    extends Operator {

  private final StdFactory factory;
  private final Enforcer elementEnforcer;
  private final StdArrayType arrayType;

  public ArrayTransformOperator(
      EnforcerFieldPathExpression elementEnforcerExpr,
      StdFactory factory,
      StdArrayType arrayType) {
    requireNonNull(elementEnforcerExpr);
    if (elementEnforcerExpr.getEnforcerBaseElement() != PREDICATE_CURRENT_ELEMENT) {
      throw new RuntimeException("array element enforcer expression must reference current element");
    }
    this.elementEnforcer = requireNonNull(elementEnforcerExpr.getEnforcer());
    this.factory = requireNonNull(factory);
    this.arrayType = requireNonNull(arrayType);
  }

  /**
   * Returns an array after applying extraction on every element of the array using {@code elementEnforcer}.
   *
   * For a path like $.x[:].y.z.w, the {@code elementEnforcer} for the array transformation will correspond to
   * "@.y.z.w". This method will extract the values corresponding to `.y.z.w` from every element, and return this
   * new array.
   */
  @Override
  public StdData extract(StdData input, StdData root) {
    StdArray array = (StdArray) input;
    StdArray output = factory.createArray(arrayType);
    for (int i = 0; i < array.size(); i++) {
      output.add(elementEnforcer.extract(array.get(i), root));
    }
    return output;
  }

  /**
   * For every element in the array, invokes {@code elementEnforcer#transform}.
   *
   * The array transform operation applies the enforcement on every element. In the implementation, the list of
   * operators within one enforcer do not contain any intermediate array/map explosions for simplicity. When creating
   * an operator for explosion operation [:], the enforcer is terminated with the last "array transform"
   * operator. And this explosion is stored an element enforcer within the {@code ArrayTransformOperator}. So the
   * {@code remainingEnforcerOperators} argument here is always expected to be empty. That's why we do a sanity check
   * on the size of remainingEnforcerOperators.
   *
   * For example, a path like `$.x.y[:].z.w[:].p`, we have two "explosions", one for `y` and another for `w`.
   * => the final enforcer T would contain operators representing `input -> .x -> .y -> transform(T') -> output`.
   * => T' would contain operators corresponding to `input -> .z -> .w -> transform(T'') -> output`.
   * => T'' would map to `input ->.p -> output`.
   *
   * @return the array made of these transformed objects.
   */
  @Override
  public StdData applyAction(StdData input, Action action, Enforcer remainingEnforcerOperators, StdData root) {
    if (remainingEnforcerOperators.getOperators().size() != 0) {
      throw new RuntimeException("Array transform should not be followed by any other operators");
    }

    if (input == null) {
      // If the array itself is null, there is no transformation to be applied
      return null;
    }

    StdArray array = (StdArray) input;
    StdArray output = factory.createArray(arrayType);

    for (int i = 0; i < array.size(); i++) {
      if (elementEnforcer.getOperators().size() == 0) {
        // if enforcer is empty, then we have a path like "$.x[:]". In that case, we should erase everything within x.
        output.add(action.getReplacementValue(array.get(i)));
      } else {
        output.add(elementEnforcer.applyAction(array.get(i), action, root));
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
    ArrayTransformOperator that = (ArrayTransformOperator) o;
    return elementEnforcer.equals(that.elementEnforcer);
  }

  @Override
  public int hashCode() {
    return hash(elementEnforcer);
  }

  @Override
  public String toString() {
    return "transform(" + elementEnforcer + ")";
  }
}
