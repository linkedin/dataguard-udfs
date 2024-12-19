package com.linkedin.dataguard.runtime.fieldpaths.tms.selectors;

import com.linkedin.dataguard.runtime.fieldpaths.tms.ParsedTMSPath;

import static com.google.common.base.Preconditions.*;
import static com.google.common.base.Strings.*;
import static java.util.Objects.*;


public class StructFieldSelector implements TMSPathSelector {
  private final String fieldName;

  public StructFieldSelector(String fieldName) {
    checkArgument(!isNullOrEmpty(fieldName), "struct field name cannot be null or empty");
    this.fieldName = fieldName;
  }

  public String getFieldName() {
    return fieldName;
  }

  @Override
  public String toPegasusPathSpecSegment() {
    if (ParsedTMSPath.TMS_COMPONENT_TOP_LEVEL_FIELDS.contains(fieldName)) {
      return ParsedTMSPath.PATHSPEC_IGNORE;
    } else {
      return fieldName;
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    StructFieldSelector that = (StructFieldSelector) o;
    return fieldName.equals(that.fieldName);
  }

  @Override
  public int hashCode() {
    return hash(fieldName);
  }

  @Override
  public String toString() {
    return "field(" + fieldName + ")";
  }
}
