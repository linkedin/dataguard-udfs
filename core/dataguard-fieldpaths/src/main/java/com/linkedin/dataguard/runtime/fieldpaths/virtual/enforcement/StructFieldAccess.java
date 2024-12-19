package com.linkedin.dataguard.runtime.fieldpaths.virtual.enforcement;

import com.linkedin.transport.api.data.StdData;
import com.linkedin.transport.api.data.StdStruct;

import static com.linkedin.dataguard.runtime.fieldpaths.virtual.enforcement.StdDataUtils.*;
import static java.util.Objects.*;


public class StructFieldAccess extends Operator {
  private final String identifier;

  public StructFieldAccess(String identifier) {
    this.identifier = requireNonNull(identifier);
  }

  @Override
  public StdData applyAction(StdData input, Action action, Enforcer nextOperators, StdData root) {
    if (input == null) {
      // if the struct is null, there is no enforcement on the field
       return null;
    }

    StdStruct inputStruct = (StdStruct) input;
    StdData field = null;
    try {
      field = inputStruct.getField(identifier);
    } catch (IllegalArgumentException e) {
      // if the field does not exist, there is no enforcement on the field. This is useful with schema evolution
      return input;
    }
    if (nextOperators.getOperators().size() == 0) {
      // TODO: What if the field is not nullable
      inputStruct.setField(identifier, action.getReplacementValue(field));
    } else {
      inputStruct.setField(identifier, nextOperators.applyAction(field, action, root));
    }
    return input;
  }

  @Override
  public StdData extract(StdData input, StdData root) {
    StdStruct stdStruct = checkDataAndCast(input, StdStruct.class);
    return stdStruct.getField(identifier);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    StructFieldAccess that = (StructFieldAccess) o;
    return identifier.equals(that.identifier);
  }

  @Override
  public int hashCode() {
    return hash(identifier);
  }

  @Override
  public String toString() {
    return "." + identifier;
  }
}
