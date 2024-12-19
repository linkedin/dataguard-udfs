package com.linkedin.dataguard.runtime.fieldpaths.virtual.enforcement;

import com.linkedin.dataguard.runtime.fieldpaths.virtual.enforcement.expression.EnforcerLiteral;
import com.linkedin.transport.api.data.StdData;
import com.linkedin.transport.api.data.StdMap;
import java.util.Objects;

import static java.util.Objects.*;


public class MapKeyLookup extends Operator {

  private final EnforcerLiteral key;

  public MapKeyLookup(EnforcerLiteral key) {
    this.key = requireNonNull(key);
  }

  @Override
  public StdData extract(StdData input, StdData root) {
    return ((StdMap) input).get(key.getStdValue());
  }

  @Override
  public StdData applyAction(StdData input, Action action, Enforcer remainingEnforcerOperators, StdData root) {
    StdMap map = (StdMap) input;
    if (map == null || !map.containsKey(key.getStdValue())) {
      // if the map itself is null or the key does not exist, there is no transformation to be applied
      return input;
    }

    StdData value = map.get(key.getStdValue());
    if (remainingEnforcerOperators.getOperators().size() == 0) {
      map.put(key.getStdValue(), action.getReplacementValue(value));
    } else {
      map.put(key.getStdValue(), remainingEnforcerOperators.applyAction(value, action, root));
    }
    return map;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MapKeyLookup that = (MapKeyLookup) o;
    return key.equals(that.key);
  }

  @Override
  public int hashCode() {
    return Objects.hash(key);
  }

  @Override
  public String toString() {
    return "[" + key + "]";
  }
}
