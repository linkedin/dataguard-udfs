package com.linkedin.dataguard.runtime.fieldpaths.tms.handler;

import com.linkedin.dataguard.runtime.fieldpaths.tms.enforcer.TMSEnforcer;
import com.linkedin.dataguard.runtime.fieldpaths.tms.selectors.TMSPathSelector;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.enforcement.Action;
import com.linkedin.transport.api.StdFactory;
import com.linkedin.transport.api.data.StdArray;
import com.linkedin.transport.api.data.StdData;
import com.linkedin.transport.api.types.StdArrayType;
import com.linkedin.transport.api.types.StdType;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.linkedin.dataguard.runtime.fieldpaths.tms.enforcer.TMSEnforcer.*;


public class ArrayElementHandler extends RedactHandler {

  private static final Logger LOG = LoggerFactory.getLogger(ArrayElementHandler.class);

  public ArrayElementHandler(TMSEnforcer tmsEnforcer, StdType stdType, int index) {
    super(tmsEnforcer, stdType, index);
  }
  @Override
  public StdData redact(StdData data) {
    Action action = tmsEnforcer.getAction();
    List<TMSPathSelector> tmsPathSelectors = tmsEnforcer.getSelectors();
    StdArray array = (StdArray) data;
    int size = array.size();
    StdFactory factory = tmsEnforcer.getTypeDataProvider().getStdFactory();
    StdArray redactedArray = factory.createArray(stdType, size);
    if (isLastSelector(pathIndex, tmsPathSelectors)) {
      if (LOG.isDebugEnabled()) {
        LOG.debug("last selector is array selector in {}, redacting all elements with default value", tmsPathSelectors);
      }
      for (int i = 0; i < size; i++) {
        redactedArray.add(action.getReplacementValue(array.get(i)));
      }
    } else {
      if (LOG.isDebugEnabled()) {
        LOG.debug("current selector is array selector in {}, recursively redacting all elements", tmsPathSelectors);
      }
      for (int i = 0; i < size; i++) {
        redactedArray.add(tmsEnforcer.redact(
            array.get(i),
            pathIndex + 1));
      }
    }
    return redactedArray;
  }

  @Override
  public StdType getChildStdType() {
    StdType currentType = getStdType();
    StdArrayType stdArrayType = (StdArrayType) currentType;
    return stdArrayType.elementType();
  }
}
