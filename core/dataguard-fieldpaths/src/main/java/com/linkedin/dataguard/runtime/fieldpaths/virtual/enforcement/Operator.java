package com.linkedin.dataguard.runtime.fieldpaths.virtual.enforcement;

import com.linkedin.transport.api.data.StdData;


/**
 * Base class for projections supported by the grammar.
 */
public abstract class Operator {

  /**
   * Retrieves projected field from input {@code data} using operator-specific logic.
   *
   * The output will NOT be a deep copy of the contents in input {@code data}, so any
   * modifications to this output can modify the input as well. This is done
   * intentionally to avoid data copies during extraction.
   *
   * @param data the input to the operator from which some data needs to be extracted out
   * @param root the root element (represented by "$"). This object is carried over as some
   *             operations may need the value of "$" to evaluate expressions in predicates.
   * @return the value extracted based on the operator-specific logic
   */
  abstract StdData extract(StdData data, StdData root);

  /**
   * Transforms the input {@code data} by applying {@code Action} appropriately using the custom logic of the operator.
   *
   * In case {@code nextOperators} is not empty, this method's implementation should
   * - get the relevant parts of {@code data},
   * - invoke the transform on these parts using those operators
   * - stitch back the transformed parts and return the modified results.
   *
   * There are no guarantees on the mutation of input {@code data}, it can be modified in-place to avoid data copies.
   * However, the caller should use the returned {@code StdData} as the transformed version, because in some cases, new
   * structures need to be created (e.g. StdArray) to replace values of individual fields.
   */
  abstract StdData applyAction(StdData data, Action action, Enforcer nextOperators, StdData root);
}
