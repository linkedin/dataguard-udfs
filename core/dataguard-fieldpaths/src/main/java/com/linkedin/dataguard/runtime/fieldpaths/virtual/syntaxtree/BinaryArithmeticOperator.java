package com.linkedin.dataguard.runtime.fieldpaths.virtual.syntaxtree;

import static java.util.Objects.*;

public enum BinaryArithmeticOperator {

  MULTIPLICATION("*"),
  DIVISION("/"),
  MODULO("%"),
  ADDITION("+"),
  SUBTRACTION("-");

  private final String operator;

  BinaryArithmeticOperator(String operator) {
    this.operator = requireNonNull(operator);
  }

  public String getOperator() {
    return operator;
  }
}
