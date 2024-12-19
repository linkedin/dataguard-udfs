package com.linkedin.dataguard.runtime.fieldpaths.virtual.enforcement;

import com.linkedin.dataguard.runtime.fieldpaths.virtual.semantics.SemanticException;
import com.linkedin.transport.api.data.StdData;

import static java.lang.String.*;


public final class StdDataUtils {

  private StdDataUtils() {
  }

  public static <T> T checkDataAndCast(StdData data, Class<T> expectedClass) {
    if (!expectedClass.isInstance(data)) {
      throw new SemanticException(format("Expected %s, found %s", expectedClass.getName(), data.getClass()));
    }
    return expectedClass.cast(data);
  }

}
