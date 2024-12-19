package com.linkedin.dataguard.runtime.trino.handler.factory;

import com.linkedin.dataguard.runtime.fieldpaths.tms.enforcer.TMSEnforcer;
import com.linkedin.dataguard.runtime.fieldpaths.tms.handler.UnionTypeHandler;
import com.linkedin.dataguard.runtime.fieldpaths.tms.handler.factory.HandlerFactory;
import com.linkedin.dataguard.runtime.trino.handler.TrinoUnionTypeHandler;
import com.linkedin.transport.api.types.StdType;


public class TrinoHandlerFactory extends HandlerFactory {

  private static final TrinoHandlerFactory INSTANCE = new TrinoHandlerFactory();

  public static TrinoHandlerFactory getInstance() {
    return INSTANCE;
  }

  @Override
  public UnionTypeHandler createUnionTypeHandler(TMSEnforcer tmsEnforcer, StdType stdType, int index) {
    return new TrinoUnionTypeHandler(tmsEnforcer, stdType, index);
  }
}
