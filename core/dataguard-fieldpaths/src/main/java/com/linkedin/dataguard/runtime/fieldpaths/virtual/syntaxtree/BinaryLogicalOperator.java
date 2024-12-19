package com.linkedin.dataguard.runtime.fieldpaths.virtual.syntaxtree;

import static java.util.Objects.*;


public enum BinaryLogicalOperator {

  AND("&&"),
  OR("||");

  private final String value;

  BinaryLogicalOperator(String value) {
    this.value = requireNonNull(value);
  }

  public String getValue() {
    return value;
  }
}
