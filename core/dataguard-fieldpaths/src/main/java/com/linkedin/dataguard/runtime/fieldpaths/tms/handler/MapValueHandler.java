package com.linkedin.dataguard.runtime.fieldpaths.tms.handler;

import com.linkedin.dataguard.runtime.fieldpaths.tms.enforcer.TMSEnforcer;
import com.linkedin.dataguard.runtime.fieldpaths.tms.selectors.TMSPathSelector;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.enforcement.Action;
import com.linkedin.transport.api.data.StdData;
import com.linkedin.transport.api.data.StdMap;
import com.linkedin.transport.api.types.StdMapType;
import com.linkedin.transport.api.types.StdType;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.linkedin.dataguard.runtime.fieldpaths.tms.enforcer.TMSEnforcer.*;


public class MapValueHandler extends RedactHandler {

  private static final Logger LOG = LoggerFactory.getLogger(MapValueHandler.class);

  public MapValueHandler(TMSEnforcer tmsEnforcer, StdType stdType, int index) {
    super(tmsEnforcer, stdType, index);
  }

  @Override
  public StdData redact(StdData data) {
    Action action = tmsEnforcer.getAction();
    List<TMSPathSelector> tmsPathSelectors = tmsEnforcer.getSelectors();
    StdMap dataMap = (StdMap) data;
    if (isLastSelector(pathIndex, tmsPathSelectors)) {
      if (LOG.isDebugEnabled()) {
        LOG.debug("last selector is map value selector in {}, redacting all values with default value", tmsPathSelectors);
      }
      for (StdData key: dataMap.keySet()) {
        StdData value = dataMap.get(key);
        dataMap.put(key, action.getReplacementValue(value));
      }
    } else {
      if (LOG.isDebugEnabled()) {
        LOG.debug("current selector is map value selector in {}, recursively redacting values", tmsPathSelectors);
      }
      for (StdData key: dataMap.keySet()) {
        dataMap.put(key, tmsEnforcer.redact(
            dataMap.get(key),
            pathIndex + 1
        ));
      }
    }
    return dataMap;
  }

  @Override
  public StdType getChildStdType() {
    StdType currentType = getStdType();
    StdMapType stdMapType = (StdMapType) currentType;
    return stdMapType.valueType();
  }
}
