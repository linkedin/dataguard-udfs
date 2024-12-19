package com.linkedin.dataguard.runtime.fieldpaths.tms.handler.factory;

import com.linkedin.dataguard.runtime.fieldpaths.tms.enforcer.TMSEnforcer;
import com.linkedin.dataguard.runtime.fieldpaths.tms.handler.ArrayElementHandler;
import com.linkedin.dataguard.runtime.fieldpaths.tms.handler.MapKeyHandler;
import com.linkedin.dataguard.runtime.fieldpaths.tms.handler.MapValueHandler;
import com.linkedin.dataguard.runtime.fieldpaths.tms.handler.StructFieldHandler;
import com.linkedin.dataguard.runtime.fieldpaths.tms.handler.UnionTypeHandler;
import com.linkedin.transport.api.types.StdType;


public class HandlerFactory {

  public ArrayElementHandler createArrayElementHandler(TMSEnforcer tmsEnforcer, StdType stdType, int index) {
    return new ArrayElementHandler(tmsEnforcer, stdType, index);
  }

  public MapKeyHandler createMapKeyHandler(TMSEnforcer tmsEnforcer, StdType stdType, int index) {
    return new MapKeyHandler(tmsEnforcer, stdType, index);
  }

  public MapValueHandler createMapValueHandler(TMSEnforcer tmsEnforcer, StdType stdType, int index) {
    return new MapValueHandler(tmsEnforcer, stdType, index);
  }

  public StructFieldHandler createStructFieldHandler(TMSEnforcer tmsEnforcer, StdType stdType, int index) {
    return new StructFieldHandler(tmsEnforcer, stdType, index);
  }

  public UnionTypeHandler createUnionTypeHandler(TMSEnforcer tmsEnforcer, StdType stdType, int index) {
    return new UnionTypeHandler(tmsEnforcer, stdType, index);
  }
}
