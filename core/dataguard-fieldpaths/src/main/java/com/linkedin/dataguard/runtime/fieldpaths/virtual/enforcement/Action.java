package com.linkedin.dataguard.runtime.fieldpaths.virtual.enforcement;

import com.linkedin.transport.api.data.StdData;


/**
 * Represents the action to be performed on the data addressed by a field path.
 */
public abstract class Action {
  private final ActionType actionType;

  Action(ActionType actionType) {
    this.actionType = actionType;
  }

  public ActionType getActionType() {
    return actionType;
  }

  /**
   * Provides a value to replace the original value on which the action is being applied.
   *
   * @param originalValue given original value, may or may not be used by action implementations. e.g. erase action
   *                      does not need reference to original value, whereas a truncating action would.
   * @return value to be used as replacement
   */
  public abstract StdData getReplacementValue(StdData originalValue);

  public enum ActionType {
    ERASE
  }
}
