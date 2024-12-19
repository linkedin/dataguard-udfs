package com.linkedin.dataguard.runtime.fieldpaths.virtual.enforcement;

import com.linkedin.transport.api.data.StdData;


public class EraseAction extends Action {

  private final StdData replacementValue;

  public EraseAction(StdData replacementValue) {
    super(ActionType.ERASE);
    this.replacementValue = replacementValue;
  }

  @Override
  public StdData getReplacementValue(StdData originalValue) {
    return replacementValue;
  }
}
