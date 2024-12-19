package com.linkedin.dataguard.runtime.transport.java.data;

import com.linkedin.transport.api.data.PlatformData;
import com.linkedin.transport.api.data.StdBoolean;
import java.util.Objects;


public class JavaBoolean implements PlatformData, StdBoolean {
  private boolean _value;

  public JavaBoolean(boolean value) {
    Objects.requireNonNull(value, "value cannot be null");
    _value = value;
  }

  @Override
  public Boolean getUnderlyingData() {
    return _value;
  }

  @Override
  public void setUnderlyingData(Object value) {
    Objects.requireNonNull(value, "value cannot be null");
    _value = (boolean) value;
  }

  @Override
  public boolean get() {
    return _value;
  }
}
