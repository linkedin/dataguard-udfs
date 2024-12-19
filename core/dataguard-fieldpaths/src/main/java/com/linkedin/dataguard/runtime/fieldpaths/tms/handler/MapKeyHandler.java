package com.linkedin.dataguard.runtime.fieldpaths.tms.handler;

import com.linkedin.dataguard.runtime.fieldpaths.tms.enforcer.TMSEnforcer;
import com.linkedin.dataguard.runtime.fieldpaths.tms.selectors.TMSPathSelector;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.enforcement.Action;
import com.linkedin.transport.api.data.StdData;
import com.linkedin.transport.api.types.StdMapType;
import com.linkedin.transport.api.types.StdType;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.linkedin.dataguard.runtime.fieldpaths.tms.enforcer.TMSEnforcer.*;


public class MapKeyHandler extends RedactHandler {

  private static final Logger LOG = LoggerFactory.getLogger(MapKeyHandler.class);

  public MapKeyHandler(TMSEnforcer tmsEnforcer, StdType stdType, int index) {
    super(tmsEnforcer, stdType, index);
  }

  @Override
  public StdData redact(StdData data) {
    Action action = tmsEnforcer.getAction();
    List<TMSPathSelector> tmsPathSelectors = tmsEnforcer.getSelectors();
    // map key should always be primitive and thus the last selector,
    // if not then we throw an exception
    if (!isLastSelector(pathIndex, tmsPathSelectors)) {
      throw new RuntimeException("map key redaction should always be on primitive key type");
    }
    // Redact the entire map as map key must be unique
    if (LOG.isDebugEnabled()) {
      LOG.debug("identified map key selector in {}, redacting entire map", tmsPathSelectors);
    }
    return action.getReplacementValue(data);
  }

  @Override
  public StdType getChildStdType() {
    StdType currentType = getStdType();
    StdMapType stdMapType = (StdMapType) currentType;
    return stdMapType.keyType();
  }
}
