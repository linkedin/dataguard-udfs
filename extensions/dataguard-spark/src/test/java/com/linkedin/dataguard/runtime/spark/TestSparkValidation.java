package com.linkedin.dataguard.runtime.spark;

import com.linkedin.dataguard.runtime.fieldpaths.virtual.TestValidation;
import com.linkedin.dataguard.runtime.transport.common.FormatSpecificTypeDataProvider;
import com.linkedin.dataguard.runtime.transport.spark.NullableSparkTypeDataProvider;


public class TestSparkValidation extends TestValidation {
  @Override
  protected FormatSpecificTypeDataProvider typeDataProvider() {
    return new NullableSparkTypeDataProvider();
  }
}
