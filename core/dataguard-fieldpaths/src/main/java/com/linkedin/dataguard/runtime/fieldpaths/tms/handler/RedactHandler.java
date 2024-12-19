package com.linkedin.dataguard.runtime.fieldpaths.tms.handler;

import com.linkedin.dataguard.runtime.fieldpaths.tms.enforcer.TMSEnforcer;
import com.linkedin.transport.api.data.StdData;
import com.linkedin.transport.api.types.StdType;

import static com.google.common.base.Preconditions.*;


public abstract class RedactHandler {
  protected final TMSEnforcer tmsEnforcer;
  protected final StdType stdType;
  protected final int pathIndex;

  public RedactHandler(TMSEnforcer tmsEnforcer, StdType stdType, int pathIndex) {
    this.tmsEnforcer = checkNotNull(tmsEnforcer);
    this.stdType = checkNotNull(stdType);
    this.pathIndex = pathIndex;
  }

  /*
   * Performs redaction on the given data object using the current handler.
   */
  public abstract StdData redact(StdData data);

  /*
   * Returns the child std type of the current type based on the current handler.
   */
  public abstract StdType getChildStdType();

  public TMSEnforcer getTmsEnforcer() {
    return tmsEnforcer;
  }

  public StdType getStdType() {
    return stdType;
  }
}
