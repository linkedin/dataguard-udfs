package com.linkedin.dataguard.runtime.fieldpaths.tms.handler;

import com.linkedin.dataguard.runtime.fieldpaths.tms.enforcer.TMSEnforcer;
import com.linkedin.transport.api.data.StdData;
import com.linkedin.transport.api.types.StdType;



public class UnionTypeHandler extends RedactHandler {

  public UnionTypeHandler(TMSEnforcer tmsEnforcer, StdType stdType, int index) {
    super(tmsEnforcer, stdType, index);
  }

  @Override
  public StdData redact(StdData data) {
    throw new UnsupportedOperationException("redact for Union type handler needs to be implemented by the child class"
        + "as it depends on the knowledge of children");
  }

  @Override
  public StdType getChildStdType() {
    throw new UnsupportedOperationException("getChildStdType for Union type handler needs to be implemented by the child class"
        + "as it depends on the knowledge of children");
  }
}
