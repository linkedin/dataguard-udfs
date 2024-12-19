package com.linkedin.dataguard.runtime.fieldpaths.tms.handler;

import com.linkedin.dataguard.runtime.fieldpaths.tms.enforcer.TMSEnforcer;
import com.linkedin.dataguard.runtime.fieldpaths.tms.selectors.StructFieldSelector;
import com.linkedin.dataguard.runtime.fieldpaths.tms.selectors.TMSPathSelector;
import com.linkedin.dataguard.runtime.fieldpaths.virtual.enforcement.Action;
import com.linkedin.transport.api.data.StdData;
import com.linkedin.transport.api.data.StdStruct;
import com.linkedin.transport.api.types.StdStructType;
import com.linkedin.transport.api.types.StdType;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.linkedin.dataguard.runtime.fieldpaths.tms.enforcer.TMSEnforcer.*;
import static com.linkedin.dataguard.runtime.fieldpaths.virtual.semantics.FieldPathTypes.*;


public class StructFieldHandler extends RedactHandler {
  private static final Logger LOG = LoggerFactory.getLogger(StructFieldHandler.class);
  private final int fieldIndex;

  public StructFieldHandler(TMSEnforcer tmsEnforcer, StdType stdType, int index) {
    super(tmsEnforcer, stdType, index);
    boolean hasCaseSensitiveFieldNames = tmsEnforcer.getTypeDataProvider().hasCaseSensitiveFieldNames();
    StdStructType stdStructType = (StdStructType) stdType;
    StructFieldSelector structFieldSelector = (StructFieldSelector) tmsEnforcer.getSelectors().get(index);
    String fieldName = structFieldSelector.getFieldName();
    fieldIndex = getStdStructTypeFieldIndex(stdStructType, fieldName, hasCaseSensitiveFieldNames);
  }

  @Override
  public StdData redact(StdData data) {
    Action action = tmsEnforcer.getAction();
    List<TMSPathSelector> tmsPathSelectors = tmsEnforcer.getSelectors();
    StructFieldSelector selector = (StructFieldSelector) tmsPathSelectors.get(pathIndex);
    String fieldName = selector.getFieldName();
    StdStruct stdStruct = (StdStruct) data;
    StdData field = stdStruct.getField(fieldIndex);
    StdData defaultValue = action.getReplacementValue(field);
    if (isLastSelector(pathIndex, tmsPathSelectors)) {
      if (LOG.isDebugEnabled()) {
        LOG.debug("last selector is struct element selector in {} redacting field: {} with default value: {}",
            tmsPathSelectors, fieldName, defaultValue);
      }
      stdStruct.setField(fieldIndex, field == null ? null : defaultValue);
    } else {
      StdData redactedField = tmsEnforcer.redact(field, pathIndex + 1);
      stdStruct.setField(fieldIndex, redactedField);
    }
    if (LOG.isDebugEnabled()) {
      LOG.debug("current selector is struct element selector in {}, recursively redacting field: {}", tmsPathSelectors,
          fieldName);
    }
    return stdStruct;
  }

  @Override
  public StdType getChildStdType() {
    if (fieldIndex < 0) {
      return null;
    }
    StdType currentType = getStdType();
    StdStructType stdStructType = (StdStructType) currentType;
    return stdStructType.fieldTypes().get(fieldIndex);
  }
}
