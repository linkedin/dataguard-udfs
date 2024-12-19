package com.linkedin.dataguard.runtime.fieldpaths.tms.selectors;

import com.linkedin.dataguard.runtime.fieldpaths.tms.ParsedTMSPath;

import static com.google.common.base.Preconditions.*;
import static com.google.common.base.Strings.*;
import static java.util.Objects.*;


public class MapKeySelector implements TMSPathSelector {
  private final String keyType;

  public MapKeySelector(String keyType) {
    checkArgument(!isNullOrEmpty(keyType), "map key name cannot be null or empty");
    this.keyType = keyType;
  }

  public String getKeyType() {
    return keyType;
  }

  @Override
  public String toPegasusPathSpecSegment() {
    return ParsedTMSPath.PATHSPEC_MAP_KEY_SEGMENT;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MapKeySelector that = (MapKeySelector) o;
    return keyType.equals(that.keyType);
  }

  @Override
  public int hashCode() {
    return hash(keyType);
  }

  @Override
  public String toString() {
    return "key(" + keyType + ")";
  }
}
