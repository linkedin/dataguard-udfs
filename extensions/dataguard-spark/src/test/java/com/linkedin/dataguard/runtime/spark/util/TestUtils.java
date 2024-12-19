package com.linkedin.dataguard.runtime.spark.util;

import com.linkedin.dataguard.runtime.fieldpaths.tms.enforcer.TMSEnforcer;
import com.linkedin.dataguard.runtime.spark.tms.handler.factory.SparkHandlerFactory;
import com.linkedin.dataguard.runtime.transport.common.FormatSpecificTypeDataProvider;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.enforcement.Action;
import com.linkedin.transport.api.types.StdType;


public class TestUtils {
  public static TMSEnforcer createSparkUnionTMSEnforcer(String tmsPath,
      FormatSpecificTypeDataProvider typeDataProvider, String typeSignature, Action action) {
    StdType stdType = typeDataProvider.getStdFactory().createStdType(typeSignature);
    return new TMSEnforcer(tmsPath, stdType, typeDataProvider, action, new SparkHandlerFactory());
  }
}
