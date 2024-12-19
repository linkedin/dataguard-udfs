package com.linkedin.dataguard.runtime.fieldpaths.tms.selectors;

import com.linkedin.dataguard.runtime.fieldpaths.tms.ParsedTMSPath;

import static com.google.common.base.Preconditions.*;
import static com.google.common.base.Strings.*;
import static java.util.Objects.*;


public class ArrayElementSelector implements TMSPathSelector {
  private final String elementType;

  public ArrayElementSelector(String elementType) {
    checkArgument(!isNullOrEmpty(elementType), "array element name cannot be null or empty");
    this.elementType = elementType;
  }

  public String getElementType() {
    return elementType;
  }

  @Override
  public String toPegasusPathSpecSegment() {
    return ParsedTMSPath.PATHSPEC_ARRAY_SEGMENT;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ArrayElementSelector that = (ArrayElementSelector) o;
    return elementType.equals(that.elementType);
  }

  @Override
  public int hashCode() {
    return hash(elementType);
  }

  @Override
  public String toString() {
    return "array(" + elementType + ")";
  }
}
