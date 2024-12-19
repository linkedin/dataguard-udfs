package com.linkedin.dataguard.runtime.trino;

import io.trino.spi.Plugin;
import com.google.common.collect.ImmutableSet;
import java.util.Set;


/**
 * Entry class for Trino's plugin management framework to provide UDF definitions
 */
public class DataGuardFunctionsPlugin implements Plugin {

  @Override
  public Set<Class<?>> getFunctions() {
    try {
      return ImmutableSet.of(
          RedactFieldIf.class);
    } catch (Exception e) {
      throw new RuntimeException("Failed to load udf classes", e);
    }
  }
}
