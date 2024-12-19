package com.linkedin.dataguard.runtime.fieldpaths.virtual.enforcement;

import com.google.common.collect.ImmutableList;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.enforcement.expression.EnforcerExpr;
import com.linkedin.transport.api.StdFactory;
import com.linkedin.transport.api.data.StdBoolean;
import com.linkedin.transport.api.data.StdData;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.linkedin.dataguard.runtime.fieldpaths.virtual.enforcement.Action.ActionType.*;
import static java.lang.String.*;
import static java.util.Objects.*;


/**
 * Provides APIs to extract or transform input data.
 *
 * Operates on data using a list of operators. The list of operators indicate an ordered sequence of
 * projections to be applied on input data.
 *
 * e.g. a path like `$.x.y[:].z['k'].w[2]` will look like the following.
 * Note that for this path, `Enforcer T` is the main enforcer which
 * will be used on the input. It refers to another enforcer T' for
 * processing elements of the array `y`.
 *
 *       Enforcer T                 Enforcer T'
 * ┌────────────────────────┐     ┌───────────────────────┐
 * │    output StdData      │     │  output StdData       │
 * │          ▲             │     │          ▲            │
 * │          │             │     │          │            │
 * │ ArrayTransform (T')    │     │ ArrayIndexAccess 2    │
 * │          ▲             │     │          ▲            │
 * │          │             │     │          │            │
 * │ StructFieldAccess ".y" │     │ StructFieldAccess ".w"│
 * │          ▲             │     │          ▲            │
 * │          │             │     │          │            │
 * │ StructFieldAccess ".x" │     │ MapKeyLookup ["k"]    │
 * │          ▲             │     │          ▲            │
 * │          │             │     │          │            │
 * │     input StdData      │     │ StructFieldAccess ".z"│
 * └────────────────────────┘     │          ▲            │
 *                                │          │            │
 *                                │     input StdData     │
 *                                └───────────────────────┘
 * The enforcer exposes two methods
 * - {@code extract}  for retrieving the required data
 * - {@code transform} for transforming the input data using an action (like delete) on desired fields
 *
 * Each operator in the transformer implements its own custom logic for extraction and transformation.
 */
public class Enforcer {

  private final List<Operator> operators;
  private final Optional<EnforcerCondition> enforcerCondition;

  public Enforcer(List<Operator> operators) {
    this(operators, Optional.empty());
  }

  public Enforcer(List<Operator> operators, Optional<EnforcerCondition> enforcerCondition) {
    this.operators = requireNonNull(operators);
    this.enforcerCondition = requireNonNull(enforcerCondition);
  }

  public static Enforcer withEnforcerCondition(Enforcer enforcer, EnforcerExpr condition, StdFactory factory) {
    if (enforcer.getEnforcerCondition().isPresent()) {
      throw new IllegalArgumentException(
          "Cannot create an enforcer with a condition from an enforcer that already has a condition");
    }
    return new Enforcer(enforcer.getOperators(), Optional.of(new EnforcerCondition(condition, factory)));
  }

  public Enforcer(Operator... operators) {
    this(Arrays.asList(operators));
  }

  public Optional<EnforcerCondition> getEnforcerCondition() {
    return enforcerCondition;
  }

  public List<Operator> getOperators() {
    return operators;
  }

  /**
   * Retrieves the projected data from {@code source}, by applying projections represented by the operators.
   * The returned object is NOT a deep copy of the underlying data. Modifying it can cause the input
   * to change. This is done intentionally to avoid CPU cost while applying projections.
   *
   * @param current input data in {@link StdData} format (i.e. references to @)
   * @param root root element data in {@link StdData} format (i.e. references to $)
   * @return the retrieved StdData object corresponding to the element addressed by the enforcer.
   */
  public StdData extract(StdData current, StdData root) {
    if (enforcerCondition.isPresent() && !enforcerCondition.get().matchesCondition(current, root)) {
      // row selector is present and condition is false, returning null
      return null;
    }
    StdData result = current;
    for (Operator operator : operators) {
      if (result == null) {
        return null;
      }
      // TODO: what if root object is being modified? This has to
      //       be pre-evaluated before invoking transforms
      result = operator.extract(result, root);
    }

    return result;
  }

  /**
   * Transforms {@code input} data by applying {@code action} on the elements indicated
   * by the {@code operators} of the transformer.
   *
   * Currently only {@cocde Action.ERASE} is supported.
   *
   * @param input input data in {@link StdData} format (i.e. references to @)
   * @param root root element data in {@link StdData} format (i.e. references to $)
   * @param action the action to be performed on the elements addressed by the enforcer.
   * @return the transformed object after applying the given action on {@code input} or its subfields.
   */
  public StdData applyAction(StdData input, Action action, StdData root) {
    if (enforcerCondition.isPresent() && !enforcerCondition.get().matchesCondition(input, root)) {
      // row selector is present and condition is false, returning the input as is
      return input;
    }
    if (action.getActionType() != ERASE) {
      throw new RuntimeException(
          format("Enforcement is only supported for %s, found %s", ERASE, action.getActionType()));
    }

    if (this.getHead().isPresent()) {
      return this.getHead().get().applyAction(input, action, this.withoutHead(), root);
    }

    // No enforcers, so this is about replacing the input
    return action.getReplacementValue(input);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Enforcer that = (Enforcer) o;
    return operators.equals(that.operators);
  }

  @Override
  public int hashCode() {
    return hash(operators);
  }

  @Override
  public String toString() {
    return operators.toString();
  }

  private Optional<Operator> getHead() {
    return operators.size() == 0 ? Optional.empty() : Optional.of(operators.get(0));
  }

  private Enforcer withoutHead() {
    int size = operators.size();
    if (size < 1) {
      return new Enforcer(ImmutableList.of());
    }
    return new Enforcer(operators.subList(1, size));
  }

  private static class EnforcerCondition {

    private final EnforcerExpr enforcerExpr;
    private final StdFactory stdFactory;

    EnforcerCondition(EnforcerExpr enforcerExpr, StdFactory stdFactory) {
      this.enforcerExpr = enforcerExpr;
      this.stdFactory = stdFactory;
    }

    /**
     * Checks if the row selector condition is met for the input data.
     * If row selector condition is not met, the input data should be returned as is.
     */
    public boolean matchesCondition(StdData input, StdData root) {
      StdBoolean matchesCondition = (StdBoolean) ExpressionEvaluator.evaluate(enforcerExpr, true, input, root, stdFactory);
      if (matchesCondition == null) {
        // redact the input for safety
        return true;
      }
      return matchesCondition.get();
    }
  }
}
