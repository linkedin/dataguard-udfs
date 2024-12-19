package com.linkedin.dataguard.runtime.fieldpaths.virtual.syntaxtree;

import static java.util.Objects.*;


public enum ComparisonOperator {

  EQUALS("=="),
  NOT_EQUALS("!="),
  LT("<"),
  GT(">"),
  LTE("<="),
  GTE(">=");

  private final String operator;

  ComparisonOperator(String operator) {
    this.operator = requireNonNull(operator);
  }

  public String getOperator() {
    return operator;
  }
}
