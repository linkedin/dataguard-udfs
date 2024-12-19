package com.linkedin.dataguard.runtime.fieldpaths.tms.selectors;

import com.linkedin.dataguard.runtime.fieldpaths.tms.ParsedTMSPath;
import java.util.Objects;

import static com.google.common.base.Preconditions.*;
import static com.google.common.base.Strings.*;
import static java.util.Objects.*;


public class MapValueSelector implements TMSPathSelector {
  private final String valueType;

  public MapValueSelector(String valueType) {
    checkArgument(!isNullOrEmpty(valueType), "map value name cannot be null or empty");
    this.valueType = valueType;
  }

  public String getValueType() {
    return valueType;
  }

  @Override
  public String toPegasusPathSpecSegment() {
    return ParsedTMSPath.PATHSPEC_MAP_VALUE_SEGMENT;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MapValueSelector that = (MapValueSelector) o;
    return Objects.equals(valueType, that.valueType);
  }

  @Override
  public int hashCode() {
    return hash(valueType);
  }

  @Override
  public String toString() {
    return "value(" + valueType + ")";
  }
}
