package com.linkedin.dataguard.runtime.fieldpaths.tms.selectors;

import java.util.Objects;

import static com.google.common.base.Preconditions.*;
import static com.google.common.base.Strings.*;
import static java.util.Objects.*;


public class UnionTypeSelector implements TMSPathSelector {

  private final String unionTypeSelected;

  public UnionTypeSelector(String unionTypeSelected) {
    checkArgument(!isNullOrEmpty(unionTypeSelected), "union type name cannot be null or empty");
    this.unionTypeSelected = unionTypeSelected;
  }

  public String getUnionTypeSelected() {
    return unionTypeSelected;
  }

  /**
   * Note:
   * In unaliased union cases, this will be the type of one of the union elements.
   * In aliased union cases, this could be the alias of the SI path.
   */
  @Override
  public String toPegasusPathSpecSegment() {
    return unionTypeSelected;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UnionTypeSelector that = (UnionTypeSelector) o;
    return Objects.equals(unionTypeSelected, that.unionTypeSelected);
  }

  @Override
  public int hashCode() {
    return hash(unionTypeSelected);
  }

  @Override
  public String toString() {
    return "unionType(" + unionTypeSelected + ")";
  }
}
