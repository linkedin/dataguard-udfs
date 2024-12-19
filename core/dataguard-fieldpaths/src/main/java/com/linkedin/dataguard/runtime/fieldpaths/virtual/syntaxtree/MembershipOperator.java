package com.linkedin.dataguard.runtime.fieldpaths.virtual.syntaxtree;

import static java.util.Objects.*;


public enum MembershipOperator {
  IN("IN"),
  NOT_IN("NOT_IN");

  private final String value;

  MembershipOperator(String value) {
    this.value = requireNonNull(value);
  }

  public String getValue() {
    return value;
  }
}
