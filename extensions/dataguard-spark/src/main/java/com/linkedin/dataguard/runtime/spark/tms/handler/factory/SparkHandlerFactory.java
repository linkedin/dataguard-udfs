package com.linkedin.dataguard.runtime.spark.tms.handler.factory;

import com.linkedin.dataguard.runtime.fieldpaths.tms.enforcer.TMSEnforcer;
import com.linkedin.dataguard.runtime.fieldpaths.tms.handler.UnionTypeHandler;
import com.linkedin.dataguard.runtime.fieldpaths.tms.handler.factory.HandlerFactory;
import com.linkedin.dataguard.runtime.spark.tms.handler.SparkUnionTypeHandler;
import com.linkedin.transport.api.types.StdType;


public class SparkHandlerFactory extends HandlerFactory {

  @Override
  public UnionTypeHandler createUnionTypeHandler(TMSEnforcer tmsEnforcer, StdType stdType, int index) {
    return new SparkUnionTypeHandler(tmsEnforcer, stdType, index);
  }
}
